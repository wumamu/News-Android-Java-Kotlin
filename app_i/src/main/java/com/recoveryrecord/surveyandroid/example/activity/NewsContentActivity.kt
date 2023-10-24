package com.recoveryrecord.surveyandroid.example.activity

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.recoveryrecord.surveyandroid.example.R
import com.recoveryrecord.surveyandroid.example.config.Constants
import com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_COLLECTION
import com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_CONTENT
import com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_ID_KEY
import com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_IMAGE
import com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_MEDIA_KEY
import com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_PUBDATE
import com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_TITLE
import com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_URL
import com.recoveryrecord.surveyandroid.example.config.Constants.NO_VALUE
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS_COLLECTION
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS_OPEN_TIME
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_BYTE_PER_LINE
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_COLLECTION
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_CONTENT_LENGTH
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_DEVICE_ID
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_DISPLAY_HEIGHT
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_DISPLAY_WIDTH
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_DOC_ID
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_DRAG_NUM
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_DRAG_RECORD
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_FLING_NUM
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_FLING_RECORD
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_FONT_SIZE
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_HAS_IMAGE
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_IMAGE_URL
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_IN_TIME
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_IN_TIME_LONG
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_MEDIA
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_NEWS_ID
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_OUT_TIME
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_OUT_TIME_LONG
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_PAUSE_COUNT
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_PUBDATE
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_ROW_SPACING
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_SHARE
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_TIME_ON_PAGE
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_TIME_SERIES
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_TITLE
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_TRIGGER_BY
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_TRIGGER_BY_NOTIFICATION
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_USER_ID
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_VIEWPORT_NUM
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_VIEWPORT_RECORD
import com.recoveryrecord.surveyandroid.example.config.Constants.SHARE_PREFERENCE_TEST_SIZE
import com.recoveryrecord.surveyandroid.example.config.Constants.SHARE_PREFERENCE_USER_ID
import com.recoveryrecord.surveyandroid.example.config.Constants.TMP_ACCESS_DOC_ID
import com.recoveryrecord.surveyandroid.example.config.Constants.TRIGGER_BY_KEY
import com.recoveryrecord.surveyandroid.example.config.Constants.UNKNOWN_USER_ID
import com.recoveryrecord.surveyandroid.example.model.MediaType.Companion.getChinese
import com.recoveryrecord.surveyandroid.example.model.ReadingBehavior
import com.recoveryrecord.surveyandroid.example.receiver.ShareResultReceiver
import com.recoveryrecord.surveyandroid.example.sqlite.DragObj
import com.recoveryrecord.surveyandroid.example.sqlite.FlingObj
import com.recoveryrecord.surveyandroid.example.ui.GestureListener
import com.recoveryrecord.surveyandroid.example.util.SimpleGestureListener
import com.recoveryrecord.surveyandroid.example.util.fetchRemoteOne
import com.recoveryrecord.surveyandroid.example.util.insertRemote
import com.recoveryrecord.surveyandroid.example.util.loadImageWithGlide
import com.recoveryrecord.surveyandroid.example.util.showToast
import com.recoveryrecord.surveyandroid.example.util.updateRemote
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Calendar
import java.util.Date
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import javax.inject.Inject

@AndroidEntryPoint
class NewsContentActivity : AppCompatActivity(), SimpleGestureListener {
    private lateinit var sharedPrefs: SharedPreferences

    private var deviceId = ""

    @Inject
    lateinit var db: FirebaseFirestore

    private lateinit var rbRef: DocumentReference

    var activityStopped = false
    var activityEnd = false

    private var documentCreate = false
    private var inTimeLong = System.currentTimeMillis()
    private var firstIn = true

    private var enterTimestamp: Timestamp? = null
    private var tmp_time_series = "" // time series
    private var tmp_record = "" // viewport
    private var mediaChinese: String? = null

    private var mUrl: String = "NA"
    private var mImg: String? = null
    private var mTitle: String = "NA"
    private var mPubdate: Timestamp? = null
    private var mDate = "NA"
    private var mSource = "NA"
    private var mContent = ArrayList<String>()

    private lateinit var detector: GestureListener
    lateinit var viewPortLayout: LinearLayout
    lateinit var myTextViewsTitle: TextView
    lateinit var myTextViewsDate: TextView
    lateinit var myTextViewsSrc: TextView
    lateinit var imageView: ImageView
    lateinit var myTextViews: Array<TextView?>

    private var dragObjArrayListArray: MutableList<DragObj> = ArrayList() // drag gesture
    private var myReadingBehavior = ReadingBehavior()
    private var selfTrigger = true

    private val divList: MutableList<String> = ArrayList()

    private val executor = Executors.newFixedThreadPool(1)

    @SuppressLint("HardwareIds", "UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.news_content)
        viewPortLayout = findViewById(R.id.layout_inside)

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        deviceId =
            Settings.Secure.getString(
                applicationContext.contentResolver,
                Settings.Secure.ANDROID_ID,
            )
        enterTimestamp = Timestamp.now()

        myReadingBehavior.inTimestamp = enterTimestamp?.seconds ?: 0L
        myReadingBehavior.docId = deviceId + " " + myReadingBehavior.inTimestamp
        myReadingBehavior.deviceId = deviceId
        myReadingBehavior.userId =
            (sharedPrefs.getString(SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號")) ?: UNKNOWN_USER_ID
        myReadingBehavior.fontSize = sharedPrefs.getString(SHARE_PREFERENCE_TEST_SIZE, "1") ?: "1"

        rbRef = db.collection(READING_BEHAVIOR_COLLECTION).document(myReadingBehavior.docId)

        intent.extras?.let { bundle ->
            myReadingBehavior.newsId = bundle.getString(NEWS_ID_KEY) ?: NO_VALUE
            myReadingBehavior.media = bundle.getString(NEWS_MEDIA_KEY) ?: NO_VALUE
            mediaChinese = getChinese(myReadingBehavior.media)
            myReadingBehavior.triggerBy = bundle.getString(TRIGGER_BY_KEY) ?: NO_VALUE
            if (myReadingBehavior.triggerBy == READING_BEHAVIOR_TRIGGER_BY_NOTIFICATION) {
                val pushNewsRef =
                    db.collection(PUSH_NEWS_COLLECTION)
                        .document(deviceId + myReadingBehavior.newsId)
                val updateData =
                    hashMapOf<String, Any>(
                        PUSH_NEWS_OPEN_TIME to Timestamp.now(),
                    )
                CoroutineScope(Dispatchers.IO).launch { updateRemote(pushNewsRef, updateData) }
                selfTrigger = false
            }
        } ?: run {
            errorLoading()
        }
        detector = GestureListener(this, this)
        calculateDisplayMatrix()
        lifecycleScope.launch {
            fetchRemoteNews()
            try {
                calculateLayout()
                initLayout()
                calculateContentLength()
            } catch (e: Exception) {
                Timber.w("Something wrong $e")
            }
            visibilityCheck(myReadingBehavior.viewPortNum)
        }
    }

    private suspend fun fetchRemoteNews() {
        val newsDocRef = db.collection(NEWS_COLLECTION).document((myReadingBehavior.newsId))
        Toast.makeText(applicationContext, "努力loading中!!", Toast.LENGTH_SHORT).show()
        fetchRemoteOne(
            newsDocRef,
            onFailed = { errorLoading() },
            onError = { errorLoading() },
        ) { documentSnapshot ->
            try {
                mUrl = documentSnapshot.getString(NEWS_URL)!!
                mTitle = documentSnapshot.getString(NEWS_TITLE)!!
                mImg = documentSnapshot.getString(NEWS_IMAGE)
                mPubdate = documentSnapshot.getTimestamp(NEWS_PUBDATE)
                mPubdate?.toDate()?.let { mDate = displayDateFormat.format(it) }
                mSource = mediaChinese ?: NO_VALUE
                val content = documentSnapshot.get(NEWS_CONTENT)
                if (content is ArrayList<*>) {
                    mContent =
                        content.filterIsInstance<String>().map { it.trim() }
                            .filter { it.isNotEmpty() } as ArrayList<String>
                }
                if (mContent.isEmpty()) {
                    // No content
                    mContent.add("內容載入失敗，不好意思~")
                    mContent.add("有空麻煩請向我們回報是哪一篇(截圖即可)")
                }
                myReadingBehavior.title = mTitle
                myReadingBehavior.pubdate = mPubdate?.seconds ?: 0L
                myReadingBehavior.hasImg = if (mImg != null) 1 else 0
            } catch (e: Exception) {
                // Handle any exceptions that may occur
                mContent.add("內容載入失敗，不好意思~")
                mContent.add("有空麻煩請向我們回報是哪一篇(截圖即可)")
            }
        }
    }

    private fun visibilityCheck(n: Int): Future<Int> {
        return executor.submit(
            object : Callable<Int> {
                var count_running = 0f
                val count = IntArray(n)
                val count_top = IntArray(4)
                val old_flag = BooleanArray(n)
                val new_flag = BooleanArray(n)
                val old_flag_top = BooleanArray(4)
                val new_flag_top = BooleanArray(4)

                override fun call(): Int {
                    Arrays.fill(old_flag, java.lang.Boolean.FALSE)
                    Arrays.fill(new_flag, java.lang.Boolean.FALSE)
                    Arrays.fill(count, 0)
//                while (!activityEnd) {
                    while (!isFinishing && !isDestroyed) {
                        if (activityStopped && !activityEnd) {
                            tmp_record = ""
                            if (myReadingBehavior.hasImage().not()) {
                                tmp_record += (count_top[0] / 10).toString() + "/"
                                tmp_record += (count_top[1] / 10).toString() + "/"
                                tmp_record += (count_top[2] / 10).toString() + "#"
                            } else {
                                tmp_record += (count_top[0] / 10).toString() + "/"
                                tmp_record += (count_top[1] / 10).toString() + "/"
                                tmp_record += (count_top[2] / 10).toString() + "/"
                                tmp_record += (count_top[3] / 10).toString() + "#"
                            }
                            for (i in 0 until n) {
                                tmp_record += (count[i] / 10).toString() + "#"
                            }
                            myReadingBehavior.viewPortRecord = tmp_record
                            while (activityStopped) {
                                Thread.sleep(100)
                            }
                        }
                        val scrollBounds = Rect()
                        var first_view = -100
                        var last_view = -100 // initial -1
                        var initial_start = -3
                        if (myReadingBehavior.hasImage()) {
                            initial_start = -4
                        }
                        // title ################################################
                        if (!myTextViewsTitle.getLocalVisibleRect(scrollBounds)) {
                            new_flag_top[0] = false
                        } else {
                            new_flag_top[0] = true
                            // -1
                            first_view = initial_start
                        }
                        if (old_flag_top[0] && new_flag_top[0]) {
                            count_top[0]++
                        } else if (old_flag_top[0]) {
                            old_flag_top[0] = false
                        } else if (new_flag_top[0]) {
                            count_top[0]++
                            old_flag_top[0] = true
                        }
                        // time #################################################
                        if (!myTextViewsDate.getLocalVisibleRect(scrollBounds)) {
                            new_flag_top[1] = false
                        } else {
                            new_flag_top[1] = true
                            if (first_view == -100) { // -1
                                first_view = initial_start + 1
                            } else {
                                last_view = initial_start + 1
                            }
                        }
                        if (old_flag_top[1] && new_flag_top[1]) {
                            count_top[1]++
                        } else if (old_flag_top[1]) {
                            old_flag_top[1] = false
                        } else if (new_flag_top[1]) {
                            count_top[1]++
                            old_flag_top[1] = true
                        }
                        // media ###############################################
                        if (!myTextViewsSrc.getLocalVisibleRect(scrollBounds)) {
                            new_flag_top[2] = false
                        } else {
                            new_flag_top[2] = true
                            if (first_view == -100) { // -1
                                first_view = initial_start + 2
                            } else {
                                last_view = initial_start + 2
                            }
                        }
                        if (old_flag_top[2] && new_flag_top[2]) {
                            count_top[2]++
                        } else if (old_flag_top[2]) {
                            old_flag_top[2] = false
                        } else if (new_flag_top[2]) {
                            count_top[2]++
                            old_flag_top[2] = true
                        }
                        // img ##################################################
                        if (myReadingBehavior.hasImage()) {
                            if (!imageView.getLocalVisibleRect(scrollBounds)) {
                                new_flag_top[3] = false
                            } else {
                                new_flag_top[3] = true
                                if (first_view == -100) { // -1
                                    first_view = initial_start + 3
                                } else {
                                    last_view = initial_start + 3
                                }
                            }
                        }
                        if (old_flag_top[3] && new_flag_top[3]) {
                            count_top[3]++
                        } else if (old_flag_top[3]) {
                            old_flag_top[3] = false
                        } else if (new_flag_top[3]) {
                            count_top[3]++
                            old_flag_top[3] = true
                        }
                        // content ##############################################
                        for (i in 0 until n) {
                            if (myTextViews[i]?.getLocalVisibleRect(scrollBounds)?.not() == true) {
                                new_flag[i] = false
                            } else {
                                new_flag[i] = true
                                if (first_view == -100) {
                                    first_view = i
                                } else {
                                    last_view = i
                                }
                            }
                            if (old_flag[i] && new_flag[i]) {
                                // still visible
                                count[i]++
                            } else if (old_flag[i] && !new_flag[i]) {
                                // no longer visible
                                old_flag[i] = new_flag[i]
                            } else if (!old_flag[i] && new_flag[i]) {
                                // start visible
                                count[i]++
                                old_flag[i] = new_flag[i]
                            } // still not visible
                        }
                        Thread.sleep(100)
                        count_running++
                        val temp = count_running / 10
                        val output_string =
                            temp.toString() + "," + (first_view + 1) + "," + (last_view + 1) + "#"
                        tmp_time_series += output_string
                        tmp_record = ""
                        if (myReadingBehavior.hasImage()) {
                            tmp_record += (count_top[0] / 10).toString() + "/"
                            tmp_record += (count_top[1] / 10).toString() + "/"
                            tmp_record += (count_top[2] / 10).toString() + "/"
                            tmp_record += (count_top[3] / 10).toString() + "#"
                        } else {
                            tmp_record += (count_top[0] / 10).toString() + "/"
                            tmp_record += (count_top[1] / 10).toString() + "/"
                            tmp_record += (count_top[2] / 10).toString() + "#"
                        }
                        for (i in 0 until n) {
                            tmp_record += (count[i] / 10).toString() + "#"
                        }
                        myReadingBehavior.viewPortRecord = tmp_record
                    }
                    var finish_record = ""
                    for (i in 0 until n) {
                        finish_record += (count[i] / 10).toString() + "#"
                    }
                    myReadingBehavior.viewPortRecord = (finish_record)
                    return 1
                }
            },
        )
    }

    private fun calculateLayout() {
        val dividedBy =
            when (myReadingBehavior.fontSize) {
                "1" -> 22
                "0" -> 18
                else -> 25
            }
        val cutSize = (myReadingBehavior.displayWidth / dividedBy).toInt()
        myReadingBehavior.bytePerLine = cutSize * 2
        // loop for each paragraph
        for (i in mContent.indices) {
            if (mContent[i].isEmpty() || TextUtils.isEmpty(mContent[i])) {
                continue
            } else if (mContent[i].contains("\n")) {
                continue // should not happen
            } // 全形空白 it works
            // full blank to half
            var str = mContent[i].replace("　".toRegex(), " ")
            var front = 0
            var iter_char_para = 0
            var last_line_in_p = false
            str = str.replace("　".toRegex(), " ")
            val para = str.toCharArray()
            // one paragraph split to line
            while (true) {
                var iter_char_line = 0
                while (true) {
                    // remove line with space first
                    if (iter_char_line == 0 && para[iter_char_para] == ' ') {
                        iter_char_para += 1
                        front += 1
                        continue
                    }
                    if (isChineseChar(para[iter_char_para]) || isExcept(para[iter_char_para]) ||
                        isEmoji(
                            para[iter_char_para],
                        )
                    ) {
                        // is chinese
                        // first check line space
                        val tmp_iter_char_line = iter_char_line + 2 // tmp cursor
                        val tmp_iter_char_para = iter_char_para + 1 // tmp cursor
                        if (tmp_iter_char_line <= (cutSize * 2)) {
                            iter_char_line = tmp_iter_char_line
                            // second check if last char in para
                            if (tmp_iter_char_para < para.size) {
                                // not last one
                                iter_char_para = tmp_iter_char_para
                            } else {
                                // is last char in para
                                last_line_in_p = true
                                break
                            }
                        } else {
                            // line space not enough
                            break
                        }
                    } else if (isFullEnglish(para[iter_char_para])) {
                        // is number word
                        // find word
                        var tmp_iter_char_line = iter_char_line + 2
                        var tmp_iter_char_para = iter_char_para + 1
                        while (tmp_iter_char_para < para.size) {
                            if (isFullEnglish(para[tmp_iter_char_para])) {
                                tmp_iter_char_line += 2
                            } else {
                                tmp_iter_char_para--
                                break
                            }
                            tmp_iter_char_para++
                        }
                        // check line space
                        if (tmp_iter_char_line <= (cutSize * 2)) {
                            iter_char_line = tmp_iter_char_line
                            // second check if last char in para
                            if (tmp_iter_char_para < para.size) {
                                iter_char_para = tmp_iter_char_para + 1
                            } else {
                                // is last char in para
                                iter_char_para = tmp_iter_char_para - 1
                                last_line_in_p = true
                                break
                            }
                        } else {
                            break
                        }
                    } else if (isFullNum(para[iter_char_para])) {
                        // is number word
                        // find word
                        var tmp_iter_char_line = iter_char_line + 2
                        var tmp_iter_char_para = iter_char_para + 1
                        while (tmp_iter_char_para < para.size) {
                            if (isFullNum(para[tmp_iter_char_para])) {
                                tmp_iter_char_line += 2
                            } else {
                                tmp_iter_char_para--
                                break
                            }
                            tmp_iter_char_para++
                        }
                        // check line space
                        if (tmp_iter_char_line <= (cutSize * 2)) {
                            iter_char_line = tmp_iter_char_line
                            // second check if last char in para
                            if (tmp_iter_char_para < para.size) {
                                iter_char_para = tmp_iter_char_para + 1
                            } else {
                                // is last char in para
                                iter_char_para = tmp_iter_char_para - 1
                                last_line_in_p = true
                                break
                            }
                        } else {
                            break
                        }
                    } else if ((para[iter_char_para] in '0'..'9')) {
                        // is number word
                        // find word
                        var tmp_iter_char_line = iter_char_line + 1
                        var tmp_iter_char_para = iter_char_para + 1
                        while (tmp_iter_char_para < para.size) {
                            if ((para[tmp_iter_char_para] >= '0' && para[tmp_iter_char_para] <= '9')) {
                                tmp_iter_char_line += 1
                            } else {
                                tmp_iter_char_para--
                                break
                            }
                            tmp_iter_char_para++
                        }
                        // check line space
                        if (tmp_iter_char_line <= (cutSize * 2)) {
                            iter_char_line = tmp_iter_char_line
                            // second check if last char in para
                            if (tmp_iter_char_para < para.size) {
                                iter_char_para = tmp_iter_char_para + 1
                            } else {
                                // is last char in para
                                iter_char_para = tmp_iter_char_para - 1
                                last_line_in_p = true
                                break
                            }
                        } else {
                            break
                        }
                    } else if ((para[iter_char_para] in 'a'..'z') || (para[iter_char_para] in 'A'..'Z') ||
                        isLatin(
                            para[iter_char_para],
                        )
                    ) {
                        var tmp_iter_char_line = iter_char_line + 1
                        var tmp_iter_char_para = iter_char_para + 1
                        while (tmp_iter_char_para < para.size) {
                            if ((para[tmp_iter_char_para] in 'a'..'z') || (para[tmp_iter_char_para] in 'A'..'Z')) {
                                tmp_iter_char_line += 1
                            } else {
                                tmp_iter_char_para--
                                break
                            }
                            tmp_iter_char_para++
                        }
                        // check line space
                        if (tmp_iter_char_line <= (cutSize * 2)) {
                            iter_char_line = tmp_iter_char_line
                            // second check if last char in para
                            if (tmp_iter_char_para < para.size) {
                                iter_char_para = tmp_iter_char_para + 1
                            } else {
                                iter_char_para = tmp_iter_char_para - 1
                                last_line_in_p = true
                                break
                            }
                        } else {
                            break
                        }
                    } else {
                        // other symbol
                        Timber.d("symbol %s", para[iter_char_para])
                        val tmp_iter_char_line = iter_char_line + 1 // tmp cursor
                        val tmp_iter_char_para = iter_char_para + 1 // tmp cursor
                        if (tmp_iter_char_line <= (cutSize * 2)) {
                            iter_char_line = tmp_iter_char_line
                            // second check if last char in para
                            if (tmp_iter_char_para < para.size) {
                                iter_char_para = tmp_iter_char_para
                            } else {
                                // is last char in para
                                last_line_in_p = true
                                break
                            }
                        } else {
                            // line space not enough
                            break
                        }
                    }
                }
                // end of line do some thing;
                if (last_line_in_p) {
                    val childStr = str.substring(front, iter_char_para + 1)
                    val spacesToAdd = cutSize * 2 - iter_char_line
                    if (spacesToAdd > 0) {
                        val stringBuilder = StringBuilder(childStr)
                        var j = 0
                        while (j < spacesToAdd) {
                            stringBuilder.append("　")
                            j += 2
                        }
                        val finalString = stringBuilder.toString()
                        divList.add(finalString)
                    } else {
                        divList.add(childStr)
                    }
                    divList.add("\n")
                    break
                } else {
                    val childStr = str.substring(front, iter_char_para)
                    divList.add(childStr)
                    front = iter_char_para
                }
            }
        }
    }

    private fun initLayout() {
        myReadingBehavior.viewPortNum = divList.size
        if (divList.isEmpty()) return
        val textSize =
            when (myReadingBehavior.fontSize) {
                "1" -> 20
                "0" -> 17
                else -> 23
            }.toFloat()
        myTextViewsTitle = createTextView(mTitle, 28f, defaultLayoutParams)
        myTextViewsDate = createTextView(mDate, 18f, defaultLayoutParams)
        myTextViewsSrc = createTextView(mSource, 18f, defaultLayoutParams)
        imageView = ImageView(viewPortLayout.context)
        addTextViewToLayout(viewPortLayout, myTextViewsTitle)
        addTextViewToLayout(viewPortLayout, myTextViewsDate)
        addTextViewToLayout(viewPortLayout, myTextViewsSrc)
        addImageViewToLayout(viewPortLayout, mImg)
        Timber.d("${myReadingBehavior.viewPortNum}")
        myTextViews = Array(myReadingBehavior.viewPortNum) { null }
        for (i in divList.indices) {
            val rowTextView = createTextView(divList[i], textSize, defaultTextLayoutParams)
            addTextViewToLayout(viewPortLayout, rowTextView)
            myTextViews[i] = rowTextView
        }
//        myTextViews[0]?.measure(0, 0)
        myTextViews[0]?.let {
            myReadingBehavior.rowSpacing = pxToDp(it.measuredHeight, it.context)
        }
    }

    private fun createTextView(
        text: String,
        textSize: Float,
        layoutParams: LinearLayout.LayoutParams,
    ): TextView {
        val textView = TextView(this)
        textView.text = text
        textView.setTextColor(Color.parseColor("black"))
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize)
        textView.gravity = Gravity.CENTER
        textView.layoutParams = layoutParams
        return textView
    }

    private fun addTextViewToLayout(
        layout: LinearLayout,
        textView: TextView,
    ) {
        layout.addView(textView)
    }

    private fun addImageViewToLayout(
        layout: LinearLayout,
        imgUrl: String?,
    ) {
        if (!imgUrl.isNullOrBlank() && imgUrl != NO_VALUE) {
            loadImageWithGlide(imageView.context, imgUrl, imageView, null)
            layout.addView(imageView)
        }
    }

    private fun calculateContentLength() {
        val viewTreeObserver = viewPortLayout.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(
                object :
                    ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        viewPortLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        val viewHeight = viewPortLayout.height
                        myReadingBehavior.contentLength = pxToDp(viewHeight, viewPortLayout.context)
                    }
                },
            )
        }
    }

    private fun errorLoading() {
        showToast(this, "Oops 出了點錯")
        onBackPressed()
    }

    private fun calculateDisplayMatrix() {
        val display = windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val density = resources.displayMetrics.density
        val dpHeight = outMetrics.heightPixels / density
        val dpWidth = outMetrics.widthPixels / density
        myReadingBehavior.displayHeight = dpHeight
        myReadingBehavior.displayWidth = dpWidth
    }

    private fun updateLocalReadingHistory() {
        val media_eng = myReadingBehavior.media
        val editor = sharedPrefs.edit()
        val read_sum = sharedPrefs.getInt(Constants.READ_TOTAL + media_eng, 0)
        editor.putInt(Constants.READ_TOTAL + media_eng, read_sum + 1)
        val calendar = Calendar.getInstance()
        val day_index = calendar[Calendar.DAY_OF_YEAR]
        val day_sum = sharedPrefs.getInt(Constants.READ_TOTAL + day_index + media_eng, 0)
        editor.putInt(Constants.READ_TOTAL + day_index + media_eng, day_sum + 1)
        editor.apply()
    }

    override fun onStart() {
        super.onStart()
        Timber.d("onStart")
        // update reading history
        updateLocalReadingHistory()
        if (firstIn) {
            addReadingBehavior()
            firstIn = false
        }
        activityStopped = false
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")
        activityStopped = false
        inTimeLong = System.currentTimeMillis()
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause")
        activityStopped = true
        myReadingBehavior.pauseOnPage = myReadingBehavior.pauseOnPage + 1
        val tmp = myReadingBehavior.timeOnPage + (System.currentTimeMillis() - inTimeLong) / 1000
        myReadingBehavior.timeOnPage = tmp
        myReadingBehavior.timeSeries = tmp_time_series
        Timber.d(myReadingBehavior.timeSeries)
        var drag_str = ""
        var drag_count = 0
        var drag_x_1 = 0f
        var drag_y_1 = 0f
        var drag_x_2 = 0f
        var drag_y_2 = 0f
        var time_one: Long = 0
        var time_two: Long = 0
        var tmp_long: Long = 0
        var duration = 0.0
        for (iter in dragObjArrayListArray.indices) {
            if (drag_x_1 == 0f && drag_y_1 == 0f) {
                time_one = dragObjArrayListArray[iter].TIME_ONE
                drag_x_1 = dragObjArrayListArray[iter].POINT_ONE_X
                drag_y_1 = dragObjArrayListArray[iter].POINT_ONE_Y
                drag_x_2 = dragObjArrayListArray[iter].POINT_TWO_X
                drag_y_2 = dragObjArrayListArray[iter].POINT_TWO_Y
            } else if (drag_x_1 ==
                dragObjArrayListArray[iter]
                    .POINT_ONE_X && drag_y_1 == dragObjArrayListArray[iter].POINT_ONE_Y
            ) {
                time_two = dragObjArrayListArray[iter].TIME_ONE
                drag_x_2 = dragObjArrayListArray[iter].POINT_TWO_X
                drag_y_2 = dragObjArrayListArray[iter].POINT_TWO_Y
            } else {
                // find end
                drag_count += 1
                if (time_one != time_two) {
                    tmp_long = time_two - time_one
                    duration = tmp_long.toDouble() / 1000
                } else {
                    duration = 0.0
                }
                var d1: Date = Date(time_one)
                var d2: Date = Date(time_two)
                try {
                    d1 = dateFormat.parse(time_one.toString())!!
                    d2 = dateFormat.parse(time_two.toString())!!
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                val my_d1: List<String> = convertDragDataToList(d1)
                val my_d2: List<String> = convertDragDataToList(d2)
                drag_str += "$duration/"
                drag_str += my_d1[2] + "/" + my_d2[2] + "/"
                drag_str += "($drag_x_1,$drag_y_1)/"
                drag_str += "($drag_x_2,$drag_y_2)/"
                var direction = ""
                val velocity_x = (Math.abs(drag_x_1 - drag_x_2) / duration)
                val velocity_y = (Math.abs(drag_y_1 - drag_y_2) / duration)
                drag_str += "$velocity_x/$velocity_y/"
                direction +=
                    if (drag_y_1 < drag_y_2) {
                        "N"
                    } else if (drag_y_1 > drag_y_2) {
                        "S"
                    } else {
                        ""
                    }
                direction +=
                    if (drag_x_1 < drag_x_2) {
                        "E"
                    } else if (drag_x_1 > drag_x_2) {
                        "W"
                    } else {
                        ""
                    }
                drag_str += "$direction#"
                drag_x_1 = 0f
                drag_y_1 = 0f
                drag_x_2 = 0f
                drag_y_2 = 0f
            }
        }
        // last drag
        if ((drag_x_1 + drag_y_1 + drag_x_2 + drag_y_2) == 0f) {
            // end at final else
//            Log.d("log: drag_str", "123");
        } else {
            drag_count += 1
            if (time_one != time_two) {
                tmp_long = time_two - time_one
                duration = tmp_long.toDouble() / 1000
            } else {
                duration = 0.0
            }
            var d1 = Date(time_one)
            var d2 = Date(time_two)
            try {
                d1 = dateFormat.parse(time_one.toString())!!
                d2 = dateFormat.parse(time_two.toString())!!
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val my_d1: List<String> = convertDragDataToList(d1)
            val my_d2: List<String> = convertDragDataToList(d2)
            drag_str += "$duration/"
            drag_str += my_d1[2] + "/" + my_d2[2] + "/"
            drag_str += "($drag_x_1,$drag_y_1)/"
            drag_str += "($drag_x_2,$drag_y_2)/"
            var direction = ""
            val velocity_x = (Math.abs(drag_x_1 - drag_x_2) / duration)
            val velocity_y = (Math.abs(drag_y_1 - drag_y_2) / duration)
            drag_str += "$velocity_x/$velocity_y/"
            direction +=
                if (drag_y_1 < drag_y_2) {
                    "N"
                } else if (drag_y_1 > drag_y_2) {
                    "S"
                } else {
                    ""
                }
            direction +=
                if (drag_x_1 < drag_x_2) {
                    "E"
                } else if (drag_x_1 > drag_x_2) {
                    "W"
                } else {
                    ""
                }
            drag_str += "$direction#"
        }
        myReadingBehavior.dragNum = drag_count
        myReadingBehavior.dragRecord = (drag_str)
        updateReadingBehavior()
    }

    override fun onStop() {
        super.onStop()
        Timber.d("onStop")
        activityStopped = true
    }

    override fun onRestart() {
        super.onRestart()
        Timber.d("onRestart")
        activityStopped = false
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy")
        activityEnd = true
    }

//    override fun onBackPressed() {
//        if (selfTrigger) {
//            super.onBackPressed()
//            supportFinishAfterTransition()
//        } else {
//            val intent = Intent(this@NewsContentActivity, NewsHybridActivity::class.java)
//            startActivity(intent)
//        }
//    }

    fun pxToDp(
        px: Int,
        context: Context,
    ): Int {
        val displayMetrics = context.resources.displayMetrics
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_sample_news, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.share) {
            CoroutineScope(Dispatchers.IO).launch {
                var remoteShareData = mutableListOf<String>()
                fetchRemoteOne(rbRef) { documentSnapshot ->
                    remoteShareData =
                        documentSnapshot.get(READING_BEHAVIOR_SHARE) as MutableList<String>
                    remoteShareData.add("none")
                }
                val updateData =
                    hashMapOf<String, Any>(
                        READING_BEHAVIOR_SHARE to remoteShareData,
                    )
                updateRemote(rbRef, updateData)
            }
            sharedPrefs.edit().putString(TMP_ACCESS_DOC_ID, myReadingBehavior.docId).apply()
            try {
                knowReceiver(mUrl)
            } catch (e: Exception) {
                Toast.makeText(this, "Hmm.. Sorry, \nCannot be share", Toast.LENGTH_SHORT).show()
            }
        } else if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun knowReceiver(url: String) {
        val sendIntent =
            ShareCompat.IntentBuilder(this)
                .setType("text/plain")
                .setText(url)
                .intent
        // We can set up a BroadcastReceiver to be notified who has received the data we shared.
        val sender =
            PendingIntent.getBroadcast(
                this,
                1,
                Intent(this, ShareResultReceiver::class.java),
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
            )
        val chooserIntent = Intent.createChooser(sendIntent, null, sender.intentSender)
        startActivity(chooserIntent)
    }

    override fun dispatchTouchEvent(me: MotionEvent): Boolean {
        detector.onTouchEvent(me)
        return super.dispatchTouchEvent(me)
    }

    override fun onSwipe(
        direction: Int,
        flingObj: FlingObj,
    ) {
        myReadingBehavior.flingNum = myReadingBehavior.flingNum + 1
        flingObj.apply {
            FLING_ID = myReadingBehavior.flingNum
            var strFling = myReadingBehavior.flingRecord
            if (strFling == NO_VALUE) {
                strFling = ""
            }
            strFling += "($POINT_ONE_X,$POINT_ONE_Y)/"
            strFling += "($POINT_TWO_X,$POINT_TWO_Y)/"
            strFling += "$DISTANCE_X/"
            strFling += "$DISTANCE_Y/"
            strFling += "$VELOCITY_X/"
            strFling += "$VELOCITY_Y/"
            var directionF = ""
            directionF +=
                if (POINT_ONE_Y < POINT_TWO_Y) {
                    "N"
                } else if (POINT_ONE_Y > POINT_TWO_Y) {
                    "S"
                } else {
                    ""
                }
            directionF +=
                if (POINT_ONE_X < POINT_TWO_X) {
                    "E"
                } else if (POINT_ONE_X > POINT_TWO_X) {
                    "W"
                } else {
                    ""
                }
            strFling += "$directionF#"
            myReadingBehavior.flingRecord = strFling
        }
    }

    override fun onOnePoint(dragObj: DragObj) {
        dragObjArrayListArray.add(dragObj)
    }

    private fun addReadingBehavior() {
        val newData =
            hashMapOf(
                READING_BEHAVIOR_DOC_ID to myReadingBehavior.docId,
                READING_BEHAVIOR_DEVICE_ID to myReadingBehavior.deviceId,
                READING_BEHAVIOR_USER_ID to myReadingBehavior.userId,
                READING_BEHAVIOR_TRIGGER_BY to myReadingBehavior.triggerBy,
                READING_BEHAVIOR_NEWS_ID to myReadingBehavior.newsId,
                READING_BEHAVIOR_TITLE to myReadingBehavior.title,
                READING_BEHAVIOR_MEDIA to myReadingBehavior.media,
                READING_BEHAVIOR_HAS_IMAGE to myReadingBehavior.hasImg,
                READING_BEHAVIOR_IMAGE_URL to (mImg ?: NO_VALUE),
                READING_BEHAVIOR_ROW_SPACING to NO_VALUE,
                READING_BEHAVIOR_BYTE_PER_LINE to NO_VALUE,
                READING_BEHAVIOR_FONT_SIZE to myReadingBehavior.fontSize,
                READING_BEHAVIOR_CONTENT_LENGTH to NO_VALUE,
                READING_BEHAVIOR_DISPLAY_WIDTH to myReadingBehavior.displayWidth,
                READING_BEHAVIOR_DISPLAY_HEIGHT to myReadingBehavior.displayHeight,
                READING_BEHAVIOR_IN_TIME to (enterTimestamp ?: Timestamp.now()),
                READING_BEHAVIOR_IN_TIME_LONG to myReadingBehavior.inTimestamp,
                READING_BEHAVIOR_OUT_TIME_LONG to Timestamp.now().seconds,
                READING_BEHAVIOR_OUT_TIME to Timestamp.now(),
                READING_BEHAVIOR_TIME_ON_PAGE to myReadingBehavior.timeOnPage,
                READING_BEHAVIOR_PAUSE_COUNT to myReadingBehavior.pauseOnPage,
                READING_BEHAVIOR_VIEWPORT_NUM to NO_VALUE,
                READING_BEHAVIOR_VIEWPORT_RECORD to listOf<String>(),
                READING_BEHAVIOR_FLING_NUM to myReadingBehavior.flingNum,
                READING_BEHAVIOR_FLING_RECORD to listOf<String>(),
                READING_BEHAVIOR_DRAG_NUM to myReadingBehavior.dragNum,
                READING_BEHAVIOR_DRAG_RECORD to listOf<String>(),
                READING_BEHAVIOR_SHARE to listOf<String>(),
                READING_BEHAVIOR_TIME_SERIES to listOf<String>(),
            )
        CoroutineScope(Dispatchers.IO).launch {
            insertRemote(rbRef, newData)
            documentCreate = true
        }
    }

    private fun updateReadingBehavior() {
        val updateData =
            hashMapOf(
                READING_BEHAVIOR_NEWS_ID to myReadingBehavior.newsId,
                READING_BEHAVIOR_TITLE to myReadingBehavior.title,
                READING_BEHAVIOR_MEDIA to myReadingBehavior.media,
                READING_BEHAVIOR_HAS_IMAGE to myReadingBehavior.hasImg,
                READING_BEHAVIOR_IMAGE_URL to (mImg ?: NO_VALUE),
                READING_BEHAVIOR_PUBDATE to (mPubdate ?: 0L),
                READING_BEHAVIOR_ROW_SPACING to myReadingBehavior.rowSpacing,
                READING_BEHAVIOR_BYTE_PER_LINE to myReadingBehavior.bytePerLine,
                READING_BEHAVIOR_CONTENT_LENGTH to myReadingBehavior.contentLength,
                READING_BEHAVIOR_OUT_TIME_LONG to Timestamp.now().seconds,
                READING_BEHAVIOR_OUT_TIME to Timestamp.now(),
                READING_BEHAVIOR_TIME_ON_PAGE to myReadingBehavior.timeOnPage,
                READING_BEHAVIOR_PAUSE_COUNT to myReadingBehavior.pauseOnPage,
                READING_BEHAVIOR_VIEWPORT_NUM to myReadingBehavior.viewPortNum,
                READING_BEHAVIOR_VIEWPORT_RECORD to myReadingBehavior.viewPortRecord.convertToRemoteDataType(),
                READING_BEHAVIOR_FLING_NUM to myReadingBehavior.flingNum,
                READING_BEHAVIOR_FLING_RECORD to myReadingBehavior.flingRecord.convertToRemoteDataType(),
                READING_BEHAVIOR_DRAG_NUM to myReadingBehavior.dragNum,
                READING_BEHAVIOR_DRAG_RECORD to myReadingBehavior.dragRecord.convertToRemoteDataType(),
                READING_BEHAVIOR_TIME_SERIES to myReadingBehavior.timeSeries.convertToRemoteDataType(),
            )
        CoroutineScope(Dispatchers.IO).launch {
            updateRemote(rbRef, updateData)
        }
    }

    companion object {
        val defaultLayoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            ).apply {
                setMargins(50, 10, 50, 10)
            }

        val defaultTextLayoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            ).apply {
                setMargins(10, 10, 10, 10)
            }

        @SuppressLint("SimpleDateFormat")
        val dateFormat = SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z")

        @SuppressLint("SimpleDateFormat")
        val displayDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        private val ch_except =
            hashSetOf(
                '，',
                '、',
                '。',
                '．',
                '‧',
                '；',
                '：',
                '▶',
                '→',
                '？',
                '！',
                '（',
                '）',
                '｛',
                '｝',
                '〔',
                '〕',
                '【',
                '】',
                '《',
                '》',
                '〈',
                '〉',
                '「',
                '」',
                '『',
                '』',
                '＃',
                '＆',
                '※',
                '／',
                '～',
                '＋',
                '★',
                '﹔',
            )
        private val full_num = hashSetOf('０', '１', '２', '３', '４', '５', '６', '７', '８', '９')
        private val latin =
            hashSetOf(
                'À',
                'Á',
                'Â',
                'Ã',
                'Ä',
                'Å',
                'Æ',
                'Ç',
                'È',
                'É',
                'Ê',
                'Ë',
                'Ì',
                'Í',
                'Î',
                'Ï',
                'Ð',
                'Ñ',
                'Ò',
                'Ó',
                'Ô',
                'Õ',
                'Ö',
                '×',
                'Ø',
                'Ù',
                'Ú',
                'Û',
                'Ü',
                'Ý',
                'Þ',
                'ß',
                'à',
                'á',
                'â',
                'ã',
                'ä',
                'å',
                'æ',
                'ç',
                'è',
                'é',
                'ê',
                'ë',
                'ì',
                'í',
                'î',
                'ï',
                'ð',
                'ñ',
                'ò',
                'ó',
                'ô',
                'õ',
                'ö',
                'ø',
                'ù',
                'ú',
                'û',
                'ü',
                'ý',
                'þ',
                'ÿ',
                'µ',
                'Ž',
                'Š',
                'Ÿ',
                'ž',
            )
        private val full_en =
            hashSetOf(
                'ｑ',
                'ｗ',
                'ｅ',
                'ｒ',
                'ｔ',
                'ｙ',
                'ｕ',
                'ｉ',
                'ｏ',
                'ｐ',
                'ａ',
                'ｓ',
                'ｄ',
                'ｆ',
                'ｇ',
                'ｈ',
                'ｊ',
                'ｋ',
                'ｌ',
                'ｚ',
                'ｘ',
                'ｃ',
                'ｖ',
                'ｂ',
                'ｎ',
                'ｍ',
                'Ｑ',
                'Ｗ',
                'Ｅ',
                'Ｒ',
                'Ｔ',
                'Ｙ',
                'Ｕ',
                'Ｉ',
                'Ｏ',
                'Ｐ',
                'Ａ',
                'Ｓ',
                'Ｄ',
                'Ｆ',
                'Ｇ',
                'Ｈ',
                'Ｊ',
                'Ｋ',
                'Ｌ',
                'Ｚ',
                'Ｘ',
                'Ｃ',
                'Ｖ',
                'Ｂ',
                'Ｎ',
                'Ｍ',
            )

        // / 32    空格
        // / 33-47    標點
        // / 48-57    0~9
        // / 58-64    標點
        // / 65-90    A~Z
        // / 91-96    標點
        // / 97-122    a~z
        // / 123-126  標點
        // 全形字元 - 65248 = 半形字元
        // Initialize vowels hashSet to contain vowel characters

        fun isExcept(c: Char): Boolean = ch_except.contains(c)

        fun isFullNum(c: Char): Boolean = full_num.contains(c)

        fun isLatin(c: Char): Boolean = latin.contains(c)

        fun isFullEnglish(c: Char): Boolean = full_en.contains(c)

        fun isEmoji(char: Char): Boolean {
            val type = Character.getType(char)
            return (type.toByte() == Character.SURROGATE || type.toByte() == Character.OTHER_SYMBOL)
        }

        fun isChineseChar(c: Char): Boolean {
            val ub = Character.UnicodeBlock.of(c)
            return (
                ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
            ) || (
                ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
            ) || (
                ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
            ) || (
                ub === Character.UnicodeBlock.GENERAL_PUNCTUATION
            ) || (ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION)
        }

        fun String.convertToRemoteDataType(): List<String> {
            return this.split("#").filter { it.isNotEmpty() }
        }

        fun convertDragDataToList(date: Date): List<String> {
            return dateFormat.format(date)
                .split(" ".toRegex())
                .filter { it.isNotEmpty() }
        }
    }
}
