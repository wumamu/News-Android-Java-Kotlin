package com.recoveryrecord.surveyandroid.example

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.recoveryrecord.surveyandroid.example.Constants.CATEGORY_POST_FIX
import com.recoveryrecord.surveyandroid.example.Constants.CONFIG
import com.recoveryrecord.surveyandroid.example.Constants.LAST_UPDATE_TIME
import com.recoveryrecord.surveyandroid.example.Constants.MEDIA_ORDER
import com.recoveryrecord.surveyandroid.example.Constants.NEWS_CATEGORY
import com.recoveryrecord.surveyandroid.example.activity.PushHistoryActivity
import com.recoveryrecord.surveyandroid.example.activity.ReadHistoryActivity
import com.recoveryrecord.surveyandroid.example.activity.SettingsActivity
import com.recoveryrecord.surveyandroid.example.adapter.SectionsPagerAdapter
import com.recoveryrecord.surveyandroid.example.receiever.LightSensorReceiver
import com.recoveryrecord.surveyandroid.example.receiever.NetworkChangeReceiver
import com.recoveryrecord.surveyandroid.example.receiever.RingModeReceiver
import com.recoveryrecord.surveyandroid.example.receiever.ScreenStateReceiver
import com.recoveryrecord.surveyandroid.example.service.FirebaseService
import com.recoveryrecord.surveyandroid.example.ui.MediaType
import com.recoveryrecord.surveyandroid.util.parseTabArray
import com.recoveryrecord.surveyandroid.util.showToast
import com.recoveryrecord.surveyandroid.util.updateRemote
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

//private const val TOPIC = "/topics/news"

@AndroidEntryPoint
class NewsHybridActivity
    : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OnRefreshListener {
    private var signature: String? = null
    private var drawerLayout: DrawerLayout? = null

    private var swipeRefreshLayout: CustomSwipeRefreshLayout? = null
    private var context: Context? = null
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var mViewPager: ViewPager? = null
    private var tabLayout: TabLayout? = null
    private var doubleBackToExitPressedOnce = false
    private var mServiceIntent: Intent? = null
    var deviceId = ""
    private var _NetworkChangeReceiver: NetworkChangeReceiver? = null
    private var _ScreenStateReceiver: ScreenStateReceiver? = null
    private var _RingModeReceiver: RingModeReceiver? = null
    private var _LightSensorReceiver: LightSensorReceiver? = null
    lateinit var sharedPrefs: SharedPreferences
    private var rankingString = MediaType.DEFAULT_MEDIA_ORDER

    @Inject
    lateinit var db: FirebaseFirestore

    @Inject
    lateinit var auth: FirebaseAuth

    companion object {
        val TAG = "NewsHybridActivity"
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("HardwareIds", "LongLogTag", "ApplySharedPref", "RestrictedApi", "BatteryLife")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = applicationContext
        setContentView(R.layout.activity_news_hybrid)
        deviceId = Settings.Secure.getString(
            applicationContext.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        rankingString =
            sharedPrefs.getString(MEDIA_ORDER, MediaType.DEFAULT_MEDIA_ORDER)
                ?: MediaType.DEFAULT_MEDIA_ORDER
        loadCategoryFromLocal()

        FirebaseService.sharedPref =
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        FirebaseService.ref = db.collection("FCMToken").document(deviceId)
//        FirebaseMessaging.getInstance().apply { subscribeToTopic(TOPIC) }


        FirebaseCrashlytics.getInstance().setUserId(deviceId)

        //first in app
        val clear = sharedPrefs.getBoolean(Constants.SHARE_PREFERENCE_CLEAR_CACHE, true)
        val userData: MutableMap<String, Any?> = HashMap()
        signature = intent.extras?.getString(Constants.USER_NUM) ?: run {
            sharedPrefs.getString(Constants.SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號")
        }
        if (clear) {
            val editor = sharedPrefs.edit()
            editor.putBoolean(Constants.SHARE_PREFERENCE_CLEAR_CACHE, false)
            editor.putString(MEDIA_ORDER, rankingString)
            editor.apply()

            val mediaPushResult: MutableList<String> = ArrayList()

            mediaPushResult.add(
                java.lang.String.join(
                    ",",
                    sharedPrefs.getStringSet(
                        Constants.SHARE_PREFERENCE_PUSH_NEWS_MEDIA_LIST_SELECTION,
                        emptySet()
                    ).toString()
                )
            )
            userData[Constants.MEDIA_BAR_ORDER] = ArrayList<String>().apply { add(rankingString) }
            userData[Constants.USER_DEVICE_ID] = deviceId

            auth.currentUser?.apply {
                userData[Constants.USER_FIRESTORE_ID] = uid
            }
            userData[Constants.USER_SURVEY_NUMBER] =
                sharedPrefs.getString(Constants.SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號")
            userData[Constants.USER_PHONE_ID] = Build.MODEL
            userData[Constants.USER_ANDROID_SDK] = Build.VERSION.SDK_INT
            userData[Constants.USER_ANDROID_RELEASE] = Build.VERSION.RELEASE
            userData[Constants.UPDATE_TIME] = Timestamp.now()
            userData[Constants.APP_VERSION_KEY] = Constants.APP_VERSION_VALUE
            userData[Constants.PUSH_MEDIA_SELECTION] = mediaPushResult
            userData["check_last_service"] = Timestamp(0, 0)
            userData["check_last_schedule"] = Timestamp(0, 0)
            userData["check_last_news"] = Timestamp(0, 0)
//            first["check_last_diary"] = Timestamp(0, 0)
//            first["check_last_esm"] = Timestamp(0, 0)
            db.collection(Constants.USER_COLLECTION)
                .document(deviceId)
                .set(userData)
//            db.collection(Constants.TEST_USER_COLLECTION)
//                .document(deviceId)
//                .set(first)
        } else {
            val rbRef = db.collection(Constants.USER_COLLECTION).document(deviceId)
            rbRef.get().addOnCompleteListener { task: Task<DocumentSnapshot?> ->
                if (task.isSuccessful) {
                    val document = task.result!!
                    if (document.exists()) {
                        val mediaPushResult =
                            (document[Constants.PUSH_MEDIA_SELECTION] as MutableList<String>?)
                                ?: mutableListOf()
                        val tmp = (sharedPrefs.getStringSet(
                            Constants.SHARE_PREFERENCE_PUSH_NEWS_MEDIA_LIST_SELECTION,
                            emptySet()
                        ) ?: emptySet()).joinToString(",")

                        if (mediaPushResult.isEmpty() || mediaPushResult[mediaPushResult.size - 1] != tmp) {
                            mediaPushResult.add(tmp)
                        }

                        val dataMap = hashMapOf(
                            Constants.PUSH_MEDIA_SELECTION to mediaPushResult,
                            Constants.USER_SURVEY_NUMBER to
                                    (sharedPrefs.getString(
                                        Constants.SHARE_PREFERENCE_USER_ID,
                                        "尚未設定實驗編號"
                                    ) ?: "尚未設定實驗編號"),
                            Constants.UPDATE_TIME to Timestamp.now(),
                            Constants.APP_VERSION_KEY to Constants.APP_VERSION_VALUE,
                            "check_last_service" to Timestamp.now(),
                            Constants.USER_DEVICE_ID to deviceId,
                            Constants.USER_ANDROID_SDK to Build.VERSION.SDK_INT,
                            Constants.USER_ANDROID_RELEASE to Build.VERSION.RELEASE
                        )
                        lifecycleScope.launch {
                            withContext(Dispatchers.IO) { updateRemote(rbRef, dataMap) }
                        }
                    } else {
                        Timber.d("No such document")
                    }
                } else {
                    Timber.d(task.exception, "get failed with ")
                }
            }
        }
        swipeRefreshLayout = findViewById(R.id.mainSwipeContainer)
        swipeRefreshLayout?.apply {
            setOnRefreshListener(this@NewsHybridActivity)
            setDistanceToTriggerSync(200)
            setColorSchemeResources(R.color.blue, R.color.red, R.color.black)
        }

        //navi
        val toolbar = findViewById<Toolbar>(R.id.main_toolbar_hy)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawer_layout_hy)
        val navigationView = findViewById<NavigationView>(R.id.nav_view_hy)
        navigationView.setNavigationItemSelectedListener(this)
        val header = navigationView.getHeaderView(0)
        val userPhone = header.findViewById<TextView>(R.id.textView_user_phone)
        userPhone.text = Build.MODEL
        val userId = header.findViewById<TextView>(R.id.textView_user_id)
        userId.text = deviceId
        val userName = header.findViewById<TextView>(R.id.textView_user_name)
        userName.text = signature
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.openNavDrawer,
            R.string.closeNavDrawer
        )
        drawerLayout?.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        title = "新聞列表"

//        //check notification service running
//        val mYourService = NewsNotificationService()
//        mServiceIntent = Intent(this, NewsNotificationService::class.java)
//        val log_service: MutableMap<String, Any> = HashMap()
//        if (!isMyServiceRunning(mYourService.javaClass)) {
//            log_service[Constants.NEWS_SERVICE_STATUS_KEY] =
//                Constants.NEWS_SERVICE_STATUS_VALUE_RESTART
//            log_service[Constants.NEWS_SERVICE_CYCLE_KEY] =
//                Constants.NEWS_SERVICE_CYCLE_VALUE_FAILED_RESTART
//            startService(mServiceIntent)
//        } else {
//            log_service[Constants.NEWS_SERVICE_STATUS_KEY] =
//                Constants.NEWS_SERVICE_STATUS_VALUE_RUNNING
//            log_service[Constants.NEWS_SERVICE_CYCLE_KEY] =
//                Constants.NEWS_SERVICE_CYCLE_VALUE_MAIN_PAGE
//        }
//        log_service[Constants.NEWS_SERVICE_TIME] = Timestamp.now()
//        log_service[Constants.NEWS_SERVICE_DEVICE_ID] = deviceId
//        val date = Date(System.currentTimeMillis())
//        @SuppressLint("SimpleDateFormat") val formatter =
//            SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z")
//        db.collection(Constants.NEWS_SERVICE_COLLECTION)
//            .document(deviceId + " " + formatter.format(date))
//            .set(log_service)

        mSectionsPagerAdapter = SectionsPagerAdapter(
            supportFragmentManager, parseTabArray(rankingString)
        )
        mViewPager = findViewById(R.id.container_hy)
        tabLayout = findViewById(R.id.tabs_hy)
        mViewPager?.adapter = mSectionsPagerAdapter
        tabLayout?.setupWithViewPager(mViewPager)

        //Network
        _NetworkChangeReceiver = NetworkChangeReceiver()
        _NetworkChangeReceiver?.registerNetworkReceiver(this)

        //Screen
        _ScreenStateReceiver = ScreenStateReceiver()
        _ScreenStateReceiver?.registerScreenStateReceiver(this)

        //RingMode
        _RingModeReceiver = RingModeReceiver()
        _RingModeReceiver?.registerRingModeReceiver(this)

        //LightSensor
        _LightSensorReceiver = LightSensorReceiver()
        _LightSensorReceiver?.registerLightSensorReceiver(this)
    }

    override fun onDestroy() {
        _NetworkChangeReceiver?.unregisterNetworkReceiver(this)
        _ScreenStateReceiver?.unregisterScreenStateReceiver(this)
        _RingModeReceiver?.unregisterBluetoothReceiver(this)
        _LightSensorReceiver?.unregisterLightSensorReceiver(this)
        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NonConstantResourceId", "QueryPermissionsNeeded")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val intent: Intent? = when (item.itemId) {
            R.id.nav_setting -> Intent(this@NewsHybridActivity, SettingsActivity::class.java)
            R.id.nav_history -> Intent(this@NewsHybridActivity, ReadHistoryActivity::class.java)
            R.id.nav_reschedule -> Intent(this@NewsHybridActivity, PushHistoryActivity::class.java)
            R.id.nav_contact -> {
//                FirebaseService.token?.let { token ->
//                    PushNotification(
//                        NotificationData(
//                            title = "去影帝告別式被發掘！謝祖武20歲帥兒正式出道　獲林心如、吳慷仁青睞", //messageBody["title"],
//                            media = "setn",
//                            newsId = "00005448a87e8688fd3323099455bc6b092a3fab"
//                        ),
//                        token
//                    ).also {
//                        sendNotification(it)
//                    }
//                }
                val selectorIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                }
                Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(Constants.OUR_EMAIL))
                    putExtra(Intent.EXTRA_SUBJECT, "NewsMoment App 問題回報")
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "Hi, 我的 user id 是$signature，\ndevice id 是$deviceId，\n我有問題要回報(以文字描述發生的問題)：\n以下是相關問題截圖(如有截圖或是錄影，可以幫助我們更快了解問題)："
                    )
                    selector = selectorIntent
                }
            }

            else -> null
        }

        intent?.let {
            if (it.resolveActivity(packageManager) != null) {
                startActivity(Intent.createChooser(it, "Send email..."))
            } else {
                Toast.makeText(this, "Gmail App is not installed", Toast.LENGTH_LONG).show()
            }
        }

        drawerLayout?.closeDrawer(GravityCompat.START)
        return intent != null
    }

//    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
//        val manager = (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
//        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
//            if (serviceClass.name == service.service.className) {
//                return true
//            }
//        }
//        return false
//    }

    override fun onBackPressed() {
        if (drawerLayout?.isDrawerOpen(GravityCompat.START) == true) {
            drawerLayout?.closeDrawer(GravityCompat.START)
        } else {
            if (doubleBackToExitPressedOnce) {
                val a = Intent(Intent.ACTION_MAIN)
                a.addCategory(Intent.CATEGORY_HOME)
                a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(a)
            }
            doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        }
    }

    override fun onRefresh() {
        loadCategoryFromLocal()
        updateViewPager()
    }

    private fun updateViewPager() {
        // Update the ViewPager and Tabs after fetching data
        mSectionsPagerAdapter = SectionsPagerAdapter(
            supportFragmentManager, parseTabArray(rankingString)
        )
        mViewPager?.adapter = mSectionsPagerAdapter
        tabLayout?.setupWithViewPager(mViewPager)

        // Hide the swipe refresh progress
        swipeRefreshLayout?.isRefreshing = false
    }

    private suspend fun fetchTabDataFromNetwork() {
        try {
            val documentSnapshot = withContext(Dispatchers.IO) {
                db.collection(NEWS_CATEGORY).document(CONFIG).get().await()
            }
            if (documentSnapshot.exists()) {
                val editor = sharedPrefs.edit()
                MediaType.getAllMedia().forEach { media ->
                    val newCategory = documentSnapshot[media] as List<String>?
                    newCategory?.let {
                        MediaType.updateCategoryMapBy(media, it)
                        editor.putString(media + CATEGORY_POST_FIX, newCategory.joinToString(","))
                    }
                }
                editor.putLong(LAST_UPDATE_TIME, System.currentTimeMillis())
                editor.apply()
                updateViewPager()
            }
        } catch (e: Exception) {
            Timber.d("Fail to get the data.$e")
        }
    }

    // Function to load and display tabs based on stored data
    private fun loadCategoryFromLocal() {
        // Check if tabData is not null and not outdated
        val lastUpdateTime = sharedPrefs.getLong(LAST_UPDATE_TIME, 0)
        val currentTime = System.currentTimeMillis()
        val updateIntervalMillis = 24 * 60 * 60 * 1000 // 24 hours

        if ((currentTime - lastUpdateTime) < updateIntervalMillis) {
            MediaType.getAllMedia().forEach { media ->
                val cur = sharedPrefs.getString(media + "_category", "") ?: ""
                MediaType.updateCategoryMapBy(media, cur.split(","))
            }
        } else {
            // Fetch and update tab data from the network
            showToast(this@NewsHybridActivity, "正在更新資料請稍候")
            swipeRefreshLayout?.isRefreshing = true
            lifecycleScope.launch { fetchTabDataFromNetwork() }
            showToast(this@NewsHybridActivity, "更新完成")
        }
    }
//    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
//        try {
//            val response = RetrofitInstance.api.postNotification(notification)
//            if(response.isSuccessful) {
//                Timber.d("Response: ${Gson().toJson(response)}")
//            } else {
//                Timber.e(response.message())
//            }
//        } catch(e: Exception) {
//            Timber.e(e.toString())
//        }
//    }
}
