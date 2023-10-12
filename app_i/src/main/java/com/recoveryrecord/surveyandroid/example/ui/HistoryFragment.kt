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
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS_COLLECTION
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS_DEVICE_ID
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS_RECEIEVE_TIME
import com.recoveryrecord.surveyandroid.example.config.Constants.READING
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_COLLECTION
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_DEVICE_ID
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_OUT_TIME
import com.recoveryrecord.surveyandroid.example.config.Constants.READ_HISTORY_LIMIT_PER_PAGE
import com.recoveryrecord.surveyandroid.example.model.News
import com.recoveryrecord.surveyandroid.util.fetchRemote
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class HistoryFragment : Fragment() {
    private lateinit var courseRV: RecyclerView
    private lateinit var dataRVAdapter: NewsRecycleViewAdapter
    private var type: String = ""

    private val dataModalArrayList: ArrayList<News> = ArrayList()

    @Inject
    lateinit var db: FirebaseFirestore

    private lateinit var deviceId: String

    companion object {
        private const val TYPE = "type"
        fun newInstance(type: String): HistoryFragment {
            return HistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(TYPE, type)
                }
            }
        }
    }

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deviceId = Settings.Secure.getString(activity?.contentResolver, Settings.Secure.ANDROID_ID)
        arguments?.let {
            type = it.getString(TYPE, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.nested_layer_history, container, false)
        courseRV = view.findViewById(R.id.idRVItems)
        initRecyclerView()
        lifecycleScope.launch { fetchData() }
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

    private fun createQuery(): Query? {
        return when (type) {
            PUSH_NEWS -> db
                .collection(PUSH_NEWS_COLLECTION)
                .whereEqualTo(PUSH_NEWS_DEVICE_ID, deviceId)
                .orderBy(PUSH_NEWS_RECEIEVE_TIME, Query.Direction.DESCENDING)
                .limit(PUSH_HISTORY_LIMIT_PER_PAGE)

            READING -> db
                .collection(READING_BEHAVIOR_COLLECTION)
                .whereEqualTo(READING_BEHAVIOR_DEVICE_ID, deviceId)
                .orderBy(READING_BEHAVIOR_OUT_TIME, Query.Direction.DESCENDING)
                .limit(READ_HISTORY_LIMIT_PER_PAGE)

            else -> null
        }
    }

    private suspend fun fetchData() {
        Timber.d(type)

        val query = createQuery() ?: return
        fetchRemote(query) { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                val list = querySnapshot.documents
                val insertStartPosition = dataModalArrayList.size

                for (d in list) {
                    val dataModal = News(
                        title = d.getString("title"),
                        media = d.getString("media"),
                        id = d.getString("id"),
                        pubDate = d.getTimestamp("pubdate"),
                        image = d.getString("image")
                    )
                    Timber.d(dataModal.toString())
                    dataModalArrayList.add(dataModal)
                }
                dataRVAdapter.notifyItemRangeInserted(insertStartPosition, list.size)
            } else {
                Timber.d("document is empty")
            }
        }
    }
}