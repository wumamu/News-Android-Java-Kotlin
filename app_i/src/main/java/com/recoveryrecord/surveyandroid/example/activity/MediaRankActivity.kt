package com.recoveryrecord.surveyandroid.example.activity

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.recoveryrecord.surveyandroid.example.Constants
import com.recoveryrecord.surveyandroid.example.Constants.MEDIA_BAR_ORDER
import com.recoveryrecord.surveyandroid.example.Constants.MEDIA_ORDER
import com.recoveryrecord.surveyandroid.example.R
import com.recoveryrecord.surveyandroid.example.SimpleItemTouchHelperCallback
import com.recoveryrecord.surveyandroid.example.adapter.MediaRankRecycleViewAdapter
import com.recoveryrecord.surveyandroid.example.model.Media
import com.recoveryrecord.surveyandroid.example.ui.MediaType
import com.recoveryrecord.surveyandroid.util.parseTabArray
import com.recoveryrecord.surveyandroid.util.parseToString
import com.recoveryrecord.surveyandroid.util.showToast
import com.recoveryrecord.surveyandroid.util.updateRemote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

class MediaRankActivity : AppCompatActivity() {

    private lateinit var courseRV: RecyclerView
    private val dataModalArrayList: ArrayList<Media> = ArrayList()
    private lateinit var dataRVAdapter: MediaRankRecycleViewAdapter
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var mEditor: SharedPreferences.Editor
    private lateinit var deviceId: String
    private lateinit var ref: DocumentReference
    private var remoteMediaOrder: MutableList<String> = mutableListOf()


    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deviceId = Settings.Secure.getString(
            applicationContext.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        ref = db.collection(Constants.USER_COLLECTION).document(deviceId)
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        mEditor = sharedPrefs.edit()
        title = "首頁媒體偏好排序"
        setContentView(R.layout.activity_media_rank)
        initView()
        loadRecyclerViewData()
        lifecycleScope.launch { getRemoteMediaOrder() }
    }

    private fun initView() {
        dataRVAdapter = MediaRankRecycleViewAdapter(dataModalArrayList, this@MediaRankActivity)
        courseRV = findViewById(R.id.mediaItems)
        courseRV.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = dataRVAdapter
        }

        // Drag and drop
        val callback: ItemTouchHelper.Callback = SimpleItemTouchHelperCallback(dataRVAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(courseRV)

        showToast(this, "長按並拖移物件重新排序")
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadRecyclerViewData() {
        val rankingString =
            sharedPrefs.getString(MEDIA_ORDER, MediaType.DEFAULT_MEDIA_ORDER)
                ?: MediaType.DEFAULT_MEDIA_ORDER
        val tabs = parseTabArray(rankingString)
        tabs.forEach { t ->
            dataModalArrayList.add(Media(t))
        }
        dataRVAdapter.notifyDataSetChanged()
    }

    @SuppressLint("HardwareIds")
    override fun onPause() {
        super.onPause()
        val newMediaOrder = parseToString(dataModalArrayList)
        mEditor.putString(MEDIA_ORDER, newMediaOrder).apply()
        lifecycleScope.launch {
            withContext(Dispatchers.IO) { updateRemoteMediaOrder(newMediaOrder) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        showToast(this, "可能要重啟app設定才會生效")
    }

    private suspend fun updateRemoteMediaOrder(newMediaOrder: String) {
        if (remoteMediaOrder.isEmpty() || remoteMediaOrder.last() != newMediaOrder) {
            val updateData =
                hashMapOf<String, Any>(MEDIA_BAR_ORDER to remoteMediaOrder + newMediaOrder)
            updateRemote(ref, updateData)
        }
    }


    private suspend fun getRemoteMediaOrder() {
        try {
            val documentSnapshot = withContext(Dispatchers.IO) { ref.get().await() }
            if (documentSnapshot.exists()) {
                remoteMediaOrder =
                    documentSnapshot[MEDIA_BAR_ORDER] as MutableList<String>
            }
        } catch (e: Exception) {
            Timber.d("get failed with " + e)
        }
    }
}
