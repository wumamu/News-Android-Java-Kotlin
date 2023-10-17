package com.recoveryrecord.surveyandroid.example

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
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
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import androidx.viewpager.widget.ViewPager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.recoveryrecord.surveyandroid.example.activity.PushHistoryActivity
import com.recoveryrecord.surveyandroid.example.activity.ReadHistoryActivity
import com.recoveryrecord.surveyandroid.example.activity.SettingsActivity
import com.recoveryrecord.surveyandroid.example.adapter.SectionsPagerAdapter
import com.recoveryrecord.surveyandroid.example.config.Constants
import com.recoveryrecord.surveyandroid.example.config.Constants.ACTIVITY_COLLECTION
import com.recoveryrecord.surveyandroid.example.config.Constants.APP_VERSION_KEY
import com.recoveryrecord.surveyandroid.example.config.Constants.APP_VERSION_VALUE
import com.recoveryrecord.surveyandroid.example.config.Constants.CATEGORY_POST_FIX
import com.recoveryrecord.surveyandroid.example.config.Constants.CONFIG
import com.recoveryrecord.surveyandroid.example.config.Constants.CURRENT_TIME
import com.recoveryrecord.surveyandroid.example.config.Constants.DETECT_ACTIVITY
import com.recoveryrecord.surveyandroid.example.config.Constants.FCM_COLLECTION
import com.recoveryrecord.surveyandroid.example.config.Constants.FCM_TOKEN
import com.recoveryrecord.surveyandroid.example.config.Constants.LAST_UPDATE_TIME
import com.recoveryrecord.surveyandroid.example.config.Constants.MEDIA_BAR_ORDER
import com.recoveryrecord.surveyandroid.example.config.Constants.MEDIA_ORDER
import com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_CATEGORY
import com.recoveryrecord.surveyandroid.example.config.Constants.OUR_EMAIL
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_MEDIA_SELECTION
import com.recoveryrecord.surveyandroid.example.config.Constants.SHARE_PREFERENCE_CLEAR_CACHE
import com.recoveryrecord.surveyandroid.example.config.Constants.SHARE_PREFERENCE_PUSH_NEWS_MEDIA_LIST_SELECTION
import com.recoveryrecord.surveyandroid.example.config.Constants.SHARE_PREFERENCE_USER_ID
import com.recoveryrecord.surveyandroid.example.config.Constants.UNKNOWN_USER_ID
import com.recoveryrecord.surveyandroid.example.config.Constants.UPDATE_TIME
import com.recoveryrecord.surveyandroid.example.config.Constants.USER_ANDROID_RELEASE
import com.recoveryrecord.surveyandroid.example.config.Constants.USER_ANDROID_SDK
import com.recoveryrecord.surveyandroid.example.config.Constants.USER_DEVICE_ID
import com.recoveryrecord.surveyandroid.example.config.Constants.USER_FIRESTORE_ID
import com.recoveryrecord.surveyandroid.example.config.Constants.USER_ID
import com.recoveryrecord.surveyandroid.example.config.Constants.USER_PHONE_ID
import com.recoveryrecord.surveyandroid.example.detectedactivity.DetectedActivityService
import com.recoveryrecord.surveyandroid.example.detectedactivity.SUPPORTED_ACTIVITY_KEY
import com.recoveryrecord.surveyandroid.example.detectedactivity.SupportedActivity
import com.recoveryrecord.surveyandroid.example.model.MediaType
import com.recoveryrecord.surveyandroid.example.model.PermissionType
import com.recoveryrecord.surveyandroid.example.receiver.LightSensorReceiver
import com.recoveryrecord.surveyandroid.example.receiver.NetworkChangeReceiver
import com.recoveryrecord.surveyandroid.example.receiver.RingModeReceiver
import com.recoveryrecord.surveyandroid.example.receiver.ScreenStateReceiver
import com.recoveryrecord.surveyandroid.example.service.FirebaseService
import com.recoveryrecord.surveyandroid.example.transitions.TransitionsReceiver
import com.recoveryrecord.surveyandroid.example.transitions.TransitionsReceiver.Companion.TRANSITIONS_RECEIVER_ACTION
import com.recoveryrecord.surveyandroid.example.transitions.removeActivityTransitionUpdates
import com.recoveryrecord.surveyandroid.example.transitions.requestActivityTransitionUpdates
import com.recoveryrecord.surveyandroid.example.util.addRemote
import com.recoveryrecord.surveyandroid.example.util.convertToIdArray
import com.recoveryrecord.surveyandroid.example.util.insertRemote
import com.recoveryrecord.surveyandroid.example.util.isPermissionGranted
import com.recoveryrecord.surveyandroid.example.util.parseTabArray
import com.recoveryrecord.surveyandroid.example.util.requestPermission
import com.recoveryrecord.surveyandroid.example.util.showSettingsDialog
import com.recoveryrecord.surveyandroid.example.util.showToast
import com.recoveryrecord.surveyandroid.example.util.updateRemote
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

@AndroidEntryPoint
class NewsHybridActivity
    : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OnRefreshListener {

    lateinit var context: Context
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var swipeRefreshLayout: CustomSwipeRefreshLayout
    private lateinit var mSectionsPagerAdapter: SectionsPagerAdapter
    private lateinit var mViewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    lateinit var sharedPrefs: SharedPreferences

    private var _NetworkChangeReceiver: NetworkChangeReceiver? = null
    private var _ScreenStateReceiver: ScreenStateReceiver? = null
    private var _RingModeReceiver: RingModeReceiver? = null
    private var _LightSensorReceiver: LightSensorReceiver? = null
    private var isTrackingStarted = false

    private val transitionBroadcastReceiver: TransitionsReceiver = TransitionsReceiver().apply {
        action = { setDetectedActivity(it) }
    }

    private var doubleBackToExitPressedOnce = false
    private var firstInit = false
    private var deviceId = ""
    private var userName = ""
    private var rankingString: String = ""
//    private var mediaPushString: String = ""

    private var currentToken: String = ""

    @Inject
    lateinit var db: FirebaseFirestore

    @Inject
    lateinit var auth: FirebaseAuth

    private var remotePushNews: MutableList<String> = mutableListOf()
    private val localMediaPushNews: MutableList<String> = ArrayList()


    private lateinit var userDocRef: DocumentReference


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.hasExtra(SUPPORTED_ACTIVITY_KEY)) {
            val supportedActivity = intent.getSerializableExtra(
                SUPPORTED_ACTIVITY_KEY
            ) as SupportedActivity
            setDetectedActivity(supportedActivity)
        }
    }

    private fun setDetectedActivity(supportedActivity: SupportedActivity) {
        val detectedActivity = getString(supportedActivity.activityText)
        lifecycleScope.launch {
            val updateData = hashMapOf<String, Any>(
                USER_DEVICE_ID to deviceId,
                USER_ID to userName,
                CURRENT_TIME to Timestamp.now(),
                DETECT_ACTIVITY to detectedActivity
            )
            addRemote(db.collection(ACTIVITY_COLLECTION), updateData) {
                Timber.d("Activity Recognition Update Success")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("HardwareIds", "LongLogTag", "ApplySharedPref", "RestrictedApi", "BatteryLife")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = applicationContext
        setContentView(R.layout.activity_news_hybrid).apply { findLayout() }
        startActivityRecognition()
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        rankingString =
            sharedPrefs.getString(MEDIA_ORDER, MediaType.DEFAULT_MEDIA_ORDER)
                ?: MediaType.DEFAULT_MEDIA_ORDER
        deviceId = Settings.Secure.getString(
            applicationContext.contentResolver,
            Settings.Secure.ANDROID_ID
        ).also {
            FirebaseService.deviceId = it
            FirebaseCrashlytics.getInstance().setUserId(it)
            userDocRef = db.collection(Constants.USER_COLLECTION).document(it)
        }
        fetchDateFromLocal()
        initLayout()
        lifecycleScope.launch { getRemotePushNewsSelection() }

        if (firstInit) {
            val editor = sharedPrefs.edit()
            editor.putBoolean(SHARE_PREFERENCE_CLEAR_CACHE, false)
            editor.putString(MEDIA_ORDER, rankingString)
            editor.apply()
            val mediaPushString = localMediaPushNews.joinToString(",")

            val userInitData = hashMapOf(
                USER_DEVICE_ID to deviceId,
                USER_FIRESTORE_ID to (auth.currentUser?.uid ?: ""),
                // TODO rename
                USER_ID to userName,
                USER_PHONE_ID to Build.MODEL,
                USER_ANDROID_SDK to Build.VERSION.SDK_INT,
                USER_ANDROID_RELEASE to Build.VERSION.RELEASE,
                MEDIA_BAR_ORDER to ArrayList<String>().apply { add(rankingString) },
                PUSH_MEDIA_SELECTION to ArrayList<String>().apply { add(mediaPushString) },//localMediaPushNews, mediaPushString
                UPDATE_TIME to Timestamp.now(),
                APP_VERSION_KEY to APP_VERSION_VALUE,
                FCM_TOKEN to currentToken
                // TODO FCM token?, check_last_news
            )

            lifecycleScope.launch {
                insertRemote(userDocRef, userInitData) {
                    Timber.d("Init user data success $deviceId")
                    showToast(this@NewsHybridActivity, "User 資料庫建立成功")
                }
                updateRemoteFcm()
            }
        } else {
            val prevPushData = localMediaPushNews.joinToString(",")
            if (remotePushNews.isEmpty() || remotePushNews[remotePushNews.size - 1] != prevPushData) {
                remotePushNews.add(prevPushData)
            }

            val dataMap = hashMapOf(
                USER_DEVICE_ID to deviceId,
                USER_FIRESTORE_ID to (auth.currentUser?.uid ?: ""),
                USER_ID to userName,
                PUSH_MEDIA_SELECTION to remotePushNews,
                UPDATE_TIME to Timestamp.now(),
                USER_PHONE_ID to Build.MODEL,
                APP_VERSION_KEY to APP_VERSION_VALUE,
                USER_ANDROID_SDK to Build.VERSION.SDK_INT,
                USER_ANDROID_RELEASE to Build.VERSION.RELEASE,
                FCM_TOKEN to currentToken
            )
            lifecycleScope.launch {
                updateRemote(
                    userDocRef, dataMap,
                    onSuccess = { Timber.d("Update user data success $deviceId") }
                ) {
                    if ((it is FirebaseFirestoreException) && (it.code == FirebaseFirestoreException.Code.NOT_FOUND)) {
                        CoroutineScope(Dispatchers.IO).launch {
                            insertRemote(userDocRef, dataMap)
                        }
                    }
                }
                updateRemoteFcm()
            }
        }


        //navi
        val toolbar = findViewById<Toolbar>(R.id.main_toolbar_hy)
        setSupportActionBar(toolbar)
        val navigationView = findViewById<NavigationView>(R.id.nav_view_hy)
        navigationView.setNavigationItemSelectedListener(this)
        val header = navigationView.getHeaderView(0)
        val userPhone = header.findViewById<TextView>(R.id.textView_user_phone)
        userPhone.text = Build.MODEL
        val userId = header.findViewById<TextView>(R.id.textView_user_id)
        userId.text = deviceId
        val userName = header.findViewById<TextView>(R.id.textView_user_name)
        userName.text = this.userName
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.openNavDrawer,
            R.string.closeNavDrawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        title = "新聞列表"

//        if (listeners != null) Timber.d("Listeners are : $listeners")
    }

    private fun fetchDateFromLocal() {
        currentToken = sharedPrefs.getString(FCM_TOKEN, "") ?: ""
        userName = intent.extras?.getString(SHARE_PREFERENCE_USER_ID) ?: run {
            sharedPrefs.getString(SHARE_PREFERENCE_USER_ID, UNKNOWN_USER_ID)
        } ?: UNKNOWN_USER_ID
        firstInit = sharedPrefs.getBoolean(SHARE_PREFERENCE_CLEAR_CACHE, true)
        val defaultMediaPush = MediaType.getAllMedia().toMutableSet()
        localMediaPushNews.add(
            (sharedPrefs.getStringSet(
                SHARE_PREFERENCE_PUSH_NEWS_MEDIA_LIST_SELECTION,
                defaultMediaPush
            ) ?: defaultMediaPush).joinToString(",")
        )
        loadCategoryFromLocal()
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onResume() {
        super.onResume()
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

        registerReceiver(transitionBroadcastReceiver, IntentFilter(TRANSITIONS_RECEIVER_ACTION))
    }

    override fun onPause() {
        super.onPause()
        _NetworkChangeReceiver?.unregisterNetworkReceiver(this)
        _ScreenStateReceiver?.unregisterScreenStateReceiver(this)
        _RingModeReceiver?.unregisterRingModeReceiver(this)
        _LightSensorReceiver?.unregisterLightSensorReceiver()
        unregisterReceiver(transitionBroadcastReceiver)
    }

    override fun onDestroy() {
        removeActivityTransitionUpdates()
        stopService(Intent(this, DetectedActivityService::class.java))
        super.onDestroy()
    }

    private fun findLayout() {
        swipeRefreshLayout = findViewById(R.id.mainSwipeContainer)
        drawerLayout = findViewById(R.id.drawer_layout_hy)
        mViewPager = findViewById(R.id.container_hy)
        tabLayout = findViewById(R.id.tabs_hy)
    }


    private fun initLayout() {
        swipeRefreshLayout.apply {
            setOnRefreshListener(this@NewsHybridActivity)
            setDistanceToTriggerSync(200)
            setColorSchemeResources(R.color.blue, R.color.red, R.color.black)
        }
        mSectionsPagerAdapter = SectionsPagerAdapter(
            supportFragmentManager, parseTabArray(rankingString)
        )
        mViewPager.adapter = mSectionsPagerAdapter
        tabLayout.setupWithViewPager(mViewPager)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_setting -> Intent(
                this@NewsHybridActivity,
                SettingsActivity::class.java
            ).apply { startActivity(this) }

            R.id.nav_history -> Intent(
                this@NewsHybridActivity,
                ReadHistoryActivity::class.java
            ).apply { startActivity(this) }

            R.id.nav_reschedule -> Intent(
                this@NewsHybridActivity,
                PushHistoryActivity::class.java
            ).apply { startActivity(this) }

            R.id.nav_contact -> {
                Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    // why array :)
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(OUR_EMAIL))
                    putExtra(Intent.EXTRA_SUBJECT, "NewsMoment App 問題回報")
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "Hi, 我的 user id 是$userName，\ndevice id 是$deviceId，\n我有問題要回報(以文字描述發生的問題)：\n以下是相關問題截圖(如有截圖或是錄影，可以幫助我們更快了解問題)："
                    )
                }.let {
                    try {
                        startActivity(Intent.createChooser(it, "Send mail..."))
                    } catch (e: ActivityNotFoundException) {
                        showToast(
                            this@NewsHybridActivity,
                            "There are no email clients installed.",
                        )
                    }
                }
            }
            else -> null
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return intent != null
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
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
        mViewPager.adapter = mSectionsPagerAdapter
        tabLayout.setupWithViewPager(mViewPager)

        // Hide the swipe refresh progress
        swipeRefreshLayout.isRefreshing = false
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
            swipeRefreshLayout.isRefreshing = true
            lifecycleScope.launch {
                fetchTabDataFromNetwork()
            }
            showToast(this@NewsHybridActivity, "更新完成")
        }
    }

    private suspend fun getRemotePushNewsSelection() {
        try {
            val documentSnapshot = withContext(Dispatchers.IO) { userDocRef.get().await() }
            if (documentSnapshot.exists()) {
                remotePushNews =
                    documentSnapshot[PUSH_MEDIA_SELECTION] as MutableList<String>
            }
        } catch (e: Exception) {
            Timber.w("getRemotePushNewsSelection failed cause $e")
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun startActivityRecognition() {
        if (isPermissionGranted(PermissionType.ACTIVITY_RECOGNITION)) {
            startService(Intent(this, DetectedActivityService::class.java))
            requestActivityTransitionUpdates()
            isTrackingStarted = true
//            showToast(this@NewsHybridActivity, "You've started activity tracking")
        } else {
            requestPermission(PermissionType.ACTIVITY_RECOGNITION)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                PermissionType.ACTIVITY_RECOGNITION.string
            ).not() &&
            grantResults.size == 1 &&
            grantResults[0] == PackageManager.PERMISSION_DENIED
        ) {
            showSettingsDialog(this@NewsHybridActivity, PermissionType.ACTIVITY_RECOGNITION)
        } else if (requestCode == PermissionType.ACTIVITY_RECOGNITION.code &&
            permissions.contains(PermissionType.ACTIVITY_RECOGNITION.string) &&
            grantResults.size == 1 &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            Timber.d("permission granted")
            startService(Intent(this, DetectedActivityService::class.java))
            requestActivityTransitionUpdates()
            isTrackingStarted = true
        }
    }

    private suspend fun updateRemoteFcm() {
        if (currentToken.isBlank() || localMediaPushNews.isEmpty()) return
        val updateMediaList = convertToIdArray(localMediaPushNews.last())
        Timber.d("updateMediaList $updateMediaList")
        val newData = hashMapOf(
            USER_ID to (auth.currentUser?.uid ?: ""),
            USER_DEVICE_ID to deviceId,
            PUSH_MEDIA_SELECTION to updateMediaList,
            UPDATE_TIME to Timestamp.now(),
        )
        updateRemote(db.collection(FCM_COLLECTION).document(currentToken), newData) {
            Timber.d("FCM token update success")
        }
    }

}
