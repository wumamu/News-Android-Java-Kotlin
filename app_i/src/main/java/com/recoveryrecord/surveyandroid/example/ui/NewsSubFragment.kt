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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.recoveryrecord.surveyandroid.example.Constants
import com.recoveryrecord.surveyandroid.example.NewsRecycleViewAdapter
import com.recoveryrecord.surveyandroid.example.R
import com.recoveryrecord.surveyandroid.example.model.News

const val NEWS_SOURCE = "news_source"
const val NEWS_CATEGORY = "news_category"

class NewsSubFragment : Fragment() {
    private var courseRV: RecyclerView? = null
    private var dataModalArrayList: ArrayList<News>? = null
    private var dataRVAdapter: NewsRecycleViewAdapter? = null
    private var db: FirebaseFirestore? = null
    private var source: String = ""
    private var category: String = ""
//    private val viewModel by viewModel()
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


        db = FirebaseFirestore.getInstance()

        dataModalArrayList = ArrayList<News>()
        courseRV?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(
                activity,
                LinearLayoutManager.VERTICAL,
                false
            )
        }

        // adding our array list to our recycler view adapter class.
        dataRVAdapter = activity?.let { NewsRecycleViewAdapter(dataModalArrayList!!, it) }

        // setting adapter to our recycler view.
        courseRV?.apply { adapter = dataRVAdapter }
        if (source == "storm") {
            fetchNewsByContain(source, category)
        } else {
            fetchNewsByEqual(source, category)
        }

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchNewsByContain(source: String, category: String) {
        db!!.collection("news")
            .whereEqualTo("media", source)
            .whereArrayContains("category", category)
            .orderBy("pubdate", Query.Direction.DESCENDING)
            .limit(Constants.NEWS_LIMIT_PER_PAGE.toLong())
            .get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                Log.d("logpager", "sss")
                if (!queryDocumentSnapshots.isEmpty) {
                    val list = queryDocumentSnapshots.documents
                    for (d in list) {;
                        val dataModal = News(
                            title = d.getString("title"),
                            media = d.getString("media"),
                            id = d.getString("id"),
                            pubDate = d.getTimestamp("pubdate"),
                            image = d.getString("image")
                        )
                        dataModalArrayList?.add(dataModal)
                    }
                    dataRVAdapter!!.notifyDataSetChanged()
                }
            }.addOnFailureListener { e ->
                Log.d("logpager", "Fail to get the data.$e")
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchNewsByEqual(source: String, category: String) {
        db!!.collection("news")
            .whereEqualTo("media", source)
            .whereEqualTo("category", category)
            .orderBy("pubdate", Query.Direction.DESCENDING)
            .limit(Constants.NEWS_LIMIT_PER_PAGE.toLong())
            .get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                Log.d("logpager", "sss")
                if (!queryDocumentSnapshots.isEmpty) {
                    val list = queryDocumentSnapshots.documents
                    for (d in list) {;
                        val dataModal = News(
                            title = d.getString("title"),
                            media = d.getString("media"),
                            id = d.getString("id"),
                            pubDate = d.getTimestamp("pubdate"),
                            image = d.getString("image")
                        )
                        dataModalArrayList?.add(dataModal)
                    }
                    dataRVAdapter!!.notifyDataSetChanged()
                }
            }.addOnFailureListener { e ->
                Log.d("logpager", "Fail to get the data.$e")
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