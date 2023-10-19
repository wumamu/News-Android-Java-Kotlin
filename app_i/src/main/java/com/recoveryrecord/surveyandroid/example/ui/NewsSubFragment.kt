package com.recoveryrecord.surveyandroid.example.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.recoveryrecord.surveyandroid.example.R
import com.recoveryrecord.surveyandroid.example.adapter.NewsRecycleViewAdapter
import com.recoveryrecord.surveyandroid.example.config.Constants
import com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_CATEGORY
import com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_COLLECTION
import com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_ID
import com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_IMAGE
import com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_MEDIA
import com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_PUBDATE
import com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_TITLE
import com.recoveryrecord.surveyandroid.example.model.MediaType
import com.recoveryrecord.surveyandroid.example.model.News
import com.recoveryrecord.surveyandroid.example.util.fetchRemoteAll
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class NewsSubFragment : Fragment() {
    private lateinit var courseRV: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var noDataTextView: TextView


    private lateinit var fabScrollToTop: FloatingActionButton
    private lateinit var dataRVAdapter: NewsRecycleViewAdapter

    @Inject
    lateinit var db: FirebaseFirestore

    private val dataModalArrayList: ArrayList<News> = ArrayList()

    private var initFetching = false
    private var source: String = ""
    private var category: String = ""
    private var lastVisibleDocument: DocumentSnapshot? = null
    private var isFetchingData: Boolean = false

    companion object {
        private const val NEWS_SOURCE_PARA = "news_source"
        private const val NEWS_CATEGORY_PARA = "news_category"
        fun newInstance(source: String, category: String) =
            NewsSubFragment().apply {
                arguments = Bundle().apply {
                    putString(NEWS_SOURCE_PARA, source)
                    putString(NEWS_CATEGORY_PARA, category)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            source = it.getString(NEWS_SOURCE_PARA, "")
            category = it.getString(NEWS_CATEGORY_PARA, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.nested_layer2_category, container, false)
        courseRV = view.findViewById(R.id.idRVItems)
        fabScrollToTop = view.findViewById(R.id.fab)
        progressBar = view.findViewById(R.id.progressBar)
        noDataTextView = view.findViewById(R.id.noDataTextView)


        fabScrollToTop.hide()
        initRecyclerView()
        // Check if data is already cached
        if (!initFetching) {
            lifecycleScope.launch { fetchInitialData() }
        } else if (dataModalArrayList.isEmpty()) {
            noDataTextView.visibility = View.VISIBLE  // Show "No data available" message
//            progressBar.visibility = View.GONE
        } else {
            // Data is in the cache, use it to populate UI
            Timber.d("do nothing $source $category ${this.hashCode()}")
            noDataTextView.visibility = View.GONE  // Hide the message
        }



        fabScrollToTop.setOnClickListener {
            // Scroll to the top of the RecyclerView
            courseRV.smoothScrollToPosition(0)
        }

        // Add a scroll listener to implement pagination
        courseRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isFetchingData && lastVisibleDocument != null) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0
                    ) {
                        lifecycleScope.launch { fetchMoreData() }
                    }
                }
                if (firstVisibleItemPosition > 0) {
                    fabScrollToTop.show()
                } else {
                    fabScrollToTop.hide()
                }
            }
        })

        return view
    }

    override fun onDestroyView() {
        // called multiple times
        Timber.d("onDestroyView " + this.hashCode())
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    private fun initRecyclerView() {
        dataRVAdapter = NewsRecycleViewAdapter(dataModalArrayList, requireActivity())
        dataRVAdapter.setHasStableIds(true)
        courseRV.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = dataRVAdapter
        }
    }

    private fun createQuery(): Query {
        return if (source == MediaType.Storm.englishId) {
            db.collection(NEWS_COLLECTION)
                .whereEqualTo(NEWS_MEDIA, source)
                .whereArrayContains(NEWS_CATEGORY, category)
                .orderBy(NEWS_PUBDATE, Query.Direction.DESCENDING)
        } else {
            db.collection(NEWS_COLLECTION)
                .whereEqualTo(NEWS_MEDIA, source)
                .whereEqualTo(NEWS_CATEGORY, category)
                .orderBy(NEWS_PUBDATE, Query.Direction.DESCENDING)
        }
    }

    private suspend fun fetchInitialData() {
        initFetching = true
        val query = createQuery()
            .limit(Constants.NEWS_LIMIT_PER_PAGE)
        isFetchingData = true
        progressBar.visibility = View.VISIBLE
        fetchRemoteAll(query) { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                val list = querySnapshot.documents
                lastVisibleDocument = querySnapshot.documents.lastOrNull()
                val insertStartPosition = dataModalArrayList.size

                for (d in list) {
                    News(
                        title = d.getString(NEWS_TITLE),
                        media = d.getString(NEWS_MEDIA),
                        id = d.getString(NEWS_ID),
                        pubDate = d.getTimestamp(NEWS_PUBDATE),
                        image = d.getString(NEWS_IMAGE)
                    ).takeIf { it.isValid }?.apply {
                         dataModalArrayList.add(this)
                     }
                }
                dataRVAdapter.notifyItemRangeInserted(insertStartPosition, list.size)
            } else {
                noDataTextView.visibility = View.VISIBLE
            }
            progressBar.visibility = View.GONE
            isFetchingData = false
        }
    }

    private suspend fun fetchMoreData() {
        val query = createQuery()
            .startAfter(lastVisibleDocument?.getTimestamp(NEWS_PUBDATE))
            .limit(Constants.NEWS_LIMIT_PER_PAGE)
        isFetchingData = true
        fetchRemoteAll(query) { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                val list = querySnapshot.documents
                lastVisibleDocument = querySnapshot.documents.lastOrNull()
                val insertStartPosition = dataModalArrayList.size

                for (d in list) {
                    News(
                        title = d.getString(NEWS_TITLE),
                        media = d.getString(NEWS_MEDIA),
                        id = d.getString(NEWS_ID),
                        pubDate = d.getTimestamp(NEWS_PUBDATE),
                        image = d.getString(NEWS_IMAGE)
                    ).takeIf { it.isValid }?.apply {
                        dataModalArrayList.add(this)
                    }
                }
                dataRVAdapter.notifyItemRangeInserted(insertStartPosition, list.size)
            }
            isFetchingData = false
        }
    }
}