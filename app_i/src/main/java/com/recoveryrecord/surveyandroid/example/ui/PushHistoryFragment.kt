package com.recoveryrecord.surveyandroid.example.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.recoveryrecord.surveyandroid.example.R
import com.recoveryrecord.surveyandroid.example.adapter.NewsRecycleViewAdapter
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_HISTORY_LIMIT_PER_PAGE
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS_COLLECTION
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS_DEVICE_ID
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS_RECEIEVE_TIME
import com.recoveryrecord.surveyandroid.example.model.News
import com.recoveryrecord.surveyandroid.util.fetchRemote
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PushHistoryFragment : Fragment() {
    private lateinit var courseRV: RecyclerView
    private lateinit var dataRVAdapter: NewsRecycleViewAdapter

    private val dataModalArrayList: ArrayList<News> = ArrayList()

    @Inject
    lateinit var db: FirebaseFirestore

    private lateinit var deviceId: String

    companion object {
        fun newInstance(): PushHistoryFragment {
            return PushHistoryFragment()
        }
    }

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deviceId = Settings.Secure.getString(activity?.contentResolver, Settings.Secure.ANDROID_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.nested_layer2_readinghistory, container, false)
        courseRV = view.findViewById(R.id.idRVItems)
        initRecyclerView()
        if (dataModalArrayList.isEmpty()) {
            lifecycleScope.launch { fetchInitialData() }
        }
        return view
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

    private suspend fun fetchInitialData() {
        val query = db
            .collection(PUSH_NEWS_COLLECTION)
            .whereEqualTo(PUSH_NEWS_DEVICE_ID, deviceId)
            .orderBy(PUSH_NEWS_RECEIEVE_TIME, Query.Direction.DESCENDING)
            .limit(PUSH_HISTORY_LIMIT_PER_PAGE)
        fetchRemote(query) { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                val list = querySnapshot.documents
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
            }
        }
    }
}