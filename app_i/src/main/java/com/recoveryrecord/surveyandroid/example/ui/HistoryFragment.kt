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
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.recoveryrecord.surveyandroid.example.R
import com.recoveryrecord.surveyandroid.example.adapter.NewsRecycleViewAdapter
import com.recoveryrecord.surveyandroid.example.config.Constants.NO_VALUE
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_HISTORY_LIMIT_PER_PAGE
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS_COLLECTION
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS_DEVICE_ID
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS_RECEIEVE_TIME
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_ALL
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_COLLECTION
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_DEVICE_ID
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_IN_TIME_LONG
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_OUT_TIME
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_DAILY
import com.recoveryrecord.surveyandroid.example.config.Constants.READ_HISTORY_LIMIT_PER_PAGE
import com.recoveryrecord.surveyandroid.example.model.News
import com.recoveryrecord.surveyandroid.example.util.fetchRemoteAll
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class HistoryFragment : Fragment() {
    private lateinit var courseRV: RecyclerView
    private lateinit var dataRVAdapter: NewsRecycleViewAdapter
    private var type: String = ""
    private var day: Int = -1

    private val dataModalArrayList: ArrayList<News> = ArrayList()

    @Inject
    lateinit var db: FirebaseFirestore

    private lateinit var deviceId: String

    companion object {
        private const val TYPE = "type"
        private const val DAY = "day"
        private val ZERO_TIME = Timestamp(0, 0)

        fun newInstance(
            type: String,
            day: Int = -1,
        ): HistoryFragment {
            return HistoryFragment().apply {
                arguments =
                    Bundle().apply {
                        putString(TYPE, type)
                        putInt(DAY, day)
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
            day = it.getInt(DAY, -1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.nested_layer_history, container, false)
        courseRV = view.findViewById(R.id.idRVItems)
        initRecyclerView()
        lifecycleScope.launch { fetchData() }
        return view
    }

    private fun initRecyclerView() {
        dataRVAdapter =
            NewsRecycleViewAdapter(
                dataModalArrayList,
                requireActivity(),
                type != PUSH_NEWS,
            )
        dataRVAdapter.setHasStableIds(true)
        courseRV.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = dataRVAdapter
        }
    }

    private fun createQuery(): Query? {
        return when {
            type == PUSH_NEWS ->
                db
                    .collection(PUSH_NEWS_COLLECTION)
                    .whereEqualTo(PUSH_NEWS_DEVICE_ID, deviceId)
                    .orderBy(PUSH_NEWS_RECEIEVE_TIME, Query.Direction.DESCENDING)
                    .limit(PUSH_HISTORY_LIMIT_PER_PAGE)

            type == READING_ALL ->
                db
                    .collection(READING_BEHAVIOR_COLLECTION)
                    .whereEqualTo(READING_BEHAVIOR_DEVICE_ID, deviceId)
                    .orderBy(READING_BEHAVIOR_OUT_TIME, Query.Direction.DESCENDING)
                    .limit(READ_HISTORY_LIMIT_PER_PAGE)

            (type == READING_DAILY) && day != -1 -> {
                val range = countRange()
                db.collection(READING_BEHAVIOR_COLLECTION)
                    .whereEqualTo(READING_BEHAVIOR_DEVICE_ID, deviceId)
                    .whereGreaterThanOrEqualTo(READING_BEHAVIOR_IN_TIME_LONG, range.first)
                    .whereLessThanOrEqualTo(READING_BEHAVIOR_IN_TIME_LONG, range.second)
                    .orderBy(READING_BEHAVIOR_IN_TIME_LONG, Query.Direction.DESCENDING)
            }

            else -> null
        }
    }

    private fun countRange(): Pair<Long, Long> {
        val startTime = Calendar.getInstance()
        val endTime = Calendar.getInstance()
        startTime.add(Calendar.DAY_OF_YEAR, -(day - 1))
        endTime.add(Calendar.DAY_OF_YEAR, -(day - 1))
        startTime.set(Calendar.HOUR_OF_DAY, 0)
        startTime.set(Calendar.MINUTE, 0)
        startTime.set(Calendar.SECOND, 0)
        endTime.set(Calendar.HOUR_OF_DAY, 23)
        endTime.set(Calendar.MINUTE, 59)
        endTime.set(Calendar.SECOND, 59)
        val startLong = startTime.timeInMillis / 1000
        val endLong = endTime.timeInMillis / 1000
        return Pair(startLong, endLong)
    }

    private suspend fun fetchData() {
        Timber.d("$type $day")

        val query = createQuery() ?: return
        fetchRemoteAll(query) { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                val list = querySnapshot.documents
                val insertStartPosition = dataModalArrayList.size
                Timber.d("$type list size ${list.size}")

                for (d in list) {
                    Timber.d(d.toString())
                    val curPubdate =
                        try {
                            d.getTimestamp("pubdate")
                        } catch (e: Exception) {
                            null // Handle the case where "pubdate" is not in timestamp format
                        }
                    News(
                        title = d.getString("title"),
                        media = d.getString("media"),
                        id = d.getString("id"),
                        pubDate = curPubdate ?: ZERO_TIME,
//                        pubDate = d.getTimestamp("pubdate") ?: ZERO_TIME,
                        image = d.getString("image") ?: NO_VALUE,
                    ).takeIf { it.isValid }?.apply {
                        Timber.d(this.toString())
                        dataModalArrayList.add(this)
                    }
                }
                dataRVAdapter.notifyItemRangeInserted(insertStartPosition, list.size)
            } else {
                Timber.d("document is empty")
            }
        }
    }
}
