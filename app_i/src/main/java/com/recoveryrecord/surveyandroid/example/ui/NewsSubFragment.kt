package com.recoveryrecord.surveyandroid.example.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.recoveryrecord.surveyandroid.example.NewsRecycleViewAdapter
import com.recoveryrecord.surveyandroid.example.R
import com.recoveryrecord.surveyandroid.example.model.News

const val NEWS_SOURCE = "news_source"
const val NEWS_CATEGORY = "news_category"

class NewsSubFragment : Fragment() {
    private lateinit var courseRV: RecyclerView
    private lateinit var fabScrollToTop: FloatingActionButton
    private val dataModalArrayList: ArrayList<News> = ArrayList()
    private lateinit var dataRVAdapter: NewsRecycleViewAdapter
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var source: String = ""
    private var category: String = ""
    private val pageSize: Long = 30L //Constants.NEWS_LIMIT_PER_PAGE.toLong()
    private var lastVisibleDocument: DocumentSnapshot? = null
    private var isFetchingData: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            source = it.getString(NEWS_SOURCE, "")
            category = it.getString(NEWS_CATEGORY, "")
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
        fabScrollToTop.hide()
        initRecyclerView()
        // Check if data is already cached
        if (dataModalArrayList.isEmpty()) {
            // Data not cached, make a network request
            fetchInitialData()
        } else {
            // Data is in the cache, use it to populate UI
            Log.d("recycle", "do nothing $source $category ${this.hashCode()}")
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
                        fetchMoreData()
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
        Log.d("recycle", "onDestroyView ${this.hashCode()}")
        super.onDestroyView()
//        dataModalArrayList.clear()
    }

    override fun onDestroy() {
//        Log.d("recycle", "onDestroyView ${this.hashCode()}")
        super.onDestroy()
    }


    private fun initRecyclerView() {
        courseRV.setHasFixedSize(true)
        courseRV.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        dataRVAdapter = NewsRecycleViewAdapter(dataModalArrayList, requireActivity())
        dataRVAdapter.setHasStableIds(true)
        courseRV.adapter = dataRVAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchInitialData() {
        val query = createQuery()
        query.limit(pageSize)
            .get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                if (!queryDocumentSnapshots.isEmpty) {
                    val list = queryDocumentSnapshots.documents
                    lastVisibleDocument = queryDocumentSnapshots.documents.lastOrNull()

                    for (d in list) {
                        val dataModal = News(
                            title = d.getString("title"),
                            media = d.getString("media"),
                            id = d.getString("id"),
                            pubDate = d.getTimestamp("pubdate"),
                            image = d.getString("image")
                        )
                        dataModalArrayList.add(dataModal)
                    }
                    dataRVAdapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { e ->
                Log.d("logpager", "Fail to get the data.$e")
            }
    }

    private fun createQuery(): Query {
        return if (source == "storm") {
            db.collection("news")
                .whereEqualTo("media", source)
                .whereArrayContains("category", category)
                .orderBy("pubdate", Query.Direction.DESCENDING)
        } else {
            db.collection("news")
                .whereEqualTo("media", source)
                .whereEqualTo("category", category)
                .orderBy("pubdate", Query.Direction.DESCENDING)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchMoreData() {
        val query = createQuery()
        isFetchingData = true
        query
            .startAfter(lastVisibleDocument?.getTimestamp("pubdate"))
            .limit(pageSize)
            .get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                if (!queryDocumentSnapshots.isEmpty) {
                    val list = queryDocumentSnapshots.documents
                    lastVisibleDocument = queryDocumentSnapshots.documents.lastOrNull()
                    val insertStartPosition =
                        dataModalArrayList.size // Get the position where new items will be inserted

                    for (d in list) {
                        val dataModal = News(
                            title = d.getString("title"),
                            media = d.getString("media"),
                            id = d.getString("id"),
                            pubDate = d.getTimestamp("pubdate"),
                            image = d.getString("image")
                        )
                        dataModalArrayList.add(dataModal)
                    }
                    dataRVAdapter.notifyItemRangeInserted(insertStartPosition, list.size)
                }
                isFetchingData = false
            }
            .addOnFailureListener { e ->
                Log.d("logpager", "Fail to get more data.$e")
                isFetchingData = false
            }
    }

    companion object {
        @JvmStatic
        fun newInstance(source: String, category: String) =
            NewsSubFragment().apply {
                arguments = Bundle().apply {
                    putString(NEWS_SOURCE, source)
                    putString(NEWS_CATEGORY, category)
                }
            }

    }
}