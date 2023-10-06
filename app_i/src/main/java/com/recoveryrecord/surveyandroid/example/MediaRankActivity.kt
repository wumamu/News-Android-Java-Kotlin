package com.recoveryrecord.surveyandroid.example

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.recoveryrecord.surveyandroid.example.model.Media

class MediaRankActivity : AppCompatActivity() {
    private lateinit var courseRV: RecyclerView
    private val dataModalArrayList: ArrayList<Media> = ArrayList()
    private lateinit var dataRVAdapter: MediaRankRecycleViewAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var mSharedPreferences: SharedPreferences
    private lateinit var mEditor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        mEditor = mSharedPreferences.edit()
        title = "首頁媒體偏好排序"
        setContentView(R.layout.activity_media_rank)

        courseRV = findViewById(R.id.mediaItems)
        db = FirebaseFirestore.getInstance()

        courseRV.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        dataRVAdapter = MediaRankRecycleViewAdapter(dataModalArrayList, this)
        // setting adapter to our recycler view.
        courseRV.apply {
            adapter = dataRVAdapter
        }
        //drag and drop
        val callback: ItemTouchHelper.Callback = SimpleItemTouchHelperCallback(dataRVAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(courseRV)
        Toast.makeText(this, "長按並拖移物件重新排序", Toast.LENGTH_SHORT).show()
        loadRecyclerViewData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadRecyclerViewData() {
        val ranking =
            mSharedPreferences.getStringSet(Constants.SHARE_PREFERENCE_MAIN_PAGE_MEDIA_ORDER, null)
        for (i in 1..9) {
            ranking?.forEach { r ->
                val out: List<String> = r.split(" ").filter { it.isNotEmpty() }
                if (out.size == 2 && out[1].toIntOrNull() == i) {
                    dataModalArrayList.add(Media(out[0]))
                }
            }
        }
        dataRVAdapter.notifyDataSetChanged()
    }

    override fun onStop() {
        val new_set: MutableSet<String> = HashSet()
        for (i in dataModalArrayList.indices) {
            val tmp = dataModalArrayList[i].media + " " + (i + 1)
            println(tmp)
            new_set.add(tmp)
        }

        mEditor.putStringSet(Constants.SHARE_PREFERENCE_MAIN_PAGE_MEDIA_ORDER, new_set).commit()
        mEditor.commit()
        @SuppressLint("HardwareIds") val device_id = Settings.Secure.getString(
            applicationContext.contentResolver, Settings.Secure.ANDROID_ID
        )
        val rbRef = db.collection(Constants.USER_COLLECTION).document(device_id)
        val tmpset: Set<String> = new_set
        rbRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                document?.let { snapshot ->
                    if (snapshot.exists()) {
                        Log.d("log: firebase", "Success")
                        val mediaOrder = snapshot[Constants.MEDIA_BAR_ORDER] as MutableList<String>?
                        mediaOrder?.add(tmpset.toString())
                        rbRef.update(Constants.MEDIA_BAR_ORDER, mediaOrder)
                            .addOnSuccessListener {
                                Log.d(
                                    "log: firebase share",
                                    "DocumentSnapshot successfully updated!"
                                )
                            }
                            .addOnFailureListener { e ->
                                Log.w("log: firebase share", "Error updating document", e)
                            }
                    } else {
                        Log.d("log: firebase share", "No such document")
                    }
                }
            } else {
                Log.d("log: firebase share", "get failed with ", task.exception)
            }
        }
        super.onStop()
    }

    public override fun onDestroy() {
        Toast.makeText(this, "可能要重啟app設定才會生效", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }
}