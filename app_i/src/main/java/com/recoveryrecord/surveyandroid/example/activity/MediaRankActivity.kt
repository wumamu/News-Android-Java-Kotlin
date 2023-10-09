package com.recoveryrecord.surveyandroid.example.activity

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.recoveryrecord.surveyandroid.example.Constants
import com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_MEDIA_ORDER
import com.recoveryrecord.surveyandroid.example.Constants.MEDIA_ORDER
import com.recoveryrecord.surveyandroid.example.R
import com.recoveryrecord.surveyandroid.example.SimpleItemTouchHelperCallback
import com.recoveryrecord.surveyandroid.example.adapter.MediaRankRecycleViewAdapter
import com.recoveryrecord.surveyandroid.example.model.Media
import com.recoveryrecord.surveyandroid.util.parseTabArray
import com.recoveryrecord.surveyandroid.util.parseToString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MediaRankActivity : AppCompatActivity() {

    private lateinit var courseRV: RecyclerView
    private val dataModalArrayList: ArrayList<Media> = ArrayList()
    private lateinit var dataRVAdapter: MediaRankRecycleViewAdapter
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var mEditor: SharedPreferences.Editor
    private lateinit var deviceId: String
    private lateinit var ref: DocumentReference
    private var remoteMediaOrder: MutableList<String>? = null


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

        Toast.makeText(this, "長按並拖移物件重新排序", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadRecyclerViewData() {
        val rankingString =
            sharedPrefs.getString(MEDIA_ORDER, DEFAULT_MEDIA_ORDER) ?: DEFAULT_MEDIA_ORDER
        val tabs = parseTabArray(rankingString)
        tabs.forEach { t ->
            dataModalArrayList.add(Media(t))
        }
        dataRVAdapter.notifyDataSetChanged()
    }

    @SuppressLint("HardwareIds")
    override fun onPause() {
        val newMediaOrder = parseToString(dataModalArrayList)
        mEditor.putString(MEDIA_ORDER, newMediaOrder).apply()
        lifecycleScope.launch { updateMediaOrder(newMediaOrder) }
        super.onPause()
    }

    override fun onDestroy() {
        Toast.makeText(this, "可能要重啟app設定才會生效", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }

    private suspend fun updateMediaOrder(newMediaOrder: String) {
        try {
            remoteMediaOrder?.apply {
                add(newMediaOrder)
                withContext(Dispatchers.IO) {
                    ref.update(Constants.MEDIA_BAR_ORDER, this@apply).await()
                }
            }
        } catch (e: Exception) {
            Log.d("MediaRankActivity", "get failed with $e")

        }
    }

    private suspend fun getRemoteMediaOrder() {
        try {
            val documentSnapshot = withContext(Dispatchers.IO) { ref.get().await() }
            if (documentSnapshot.exists()) {
                remoteMediaOrder =
                    documentSnapshot[Constants.MEDIA_BAR_ORDER] as MutableList<String>?
            }
        } catch (e: Exception) {
            Log.d("MediaRankActivity:", "get failed with $e")
        }
    }
}
