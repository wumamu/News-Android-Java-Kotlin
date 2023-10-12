package com.recoveryrecord.surveyandroid.example

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
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
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
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
import com.recoveryrecord.surveyandroid.example.Constants.APP_VERSION_KEY
import com.recoveryrecord.surveyandroid.example.Constants.APP_VERSION_VALUE
import com.recoveryrecord.surveyandroid.example.Constants.CATEGORY_POST_FIX
import com.recoveryrecord.surveyandroid.example.Constants.CONFIG
import com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_TEST_CHANNEL_ID
import com.recoveryrecord.surveyandroid.example.Constants.LAST_UPDATE_TIME
import com.recoveryrecord.surveyandroid.example.Constants.MEDIA_BAR_ORDER
import com.recoveryrecord.surveyandroid.example.Constants.MEDIA_ORDER
import com.recoveryrecord.surveyandroid.example.Constants.NEWS_CATEGORY
import com.recoveryrecord.surveyandroid.example.Constants.PUSH_MEDIA_SELECTION
import com.recoveryrecord.surveyandroid.example.Constants.SHARE_PREFERENCE_USER_NAME
import com.recoveryrecord.surveyandroid.example.Constants.TEST_CHANNEL_ID
import com.recoveryrecord.surveyandroid.example.Constants.UNKNOWN_USER_NAME
import com.recoveryrecord.surveyandroid.example.Constants.UPDATE_TIME
import com.recoveryrecord.surveyandroid.example.Constants.USER_ANDROID_RELEASE
import com.recoveryrecord.surveyandroid.example.Constants.USER_ANDROID_SDK
import com.recoveryrecord.surveyandroid.example.Constants.USER_COLLECTION
import com.recoveryrecord.surveyandroid.example.Constants.USER_DEVICE_ID
import com.recoveryrecord.surveyandroid.example.Constants.USER_FIRESTORE_ID
import com.recoveryrecord.surveyandroid.example.Constants.USER_PHONE_ID
import com.recoveryrecord.surveyandroid.example.Constants.USER_SURVEY_NAME
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
import com.recoveryrecord.surveyandroid.util.insertRemote
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

    private val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    lateinit var context: Context
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var swipeRefreshLayout: CustomSwipeRefreshLayout
    private lateinit var mSectionsPagerAdapter: SectionsPagerAdapter
    private lateinit var mViewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private var doubleBackToExitPressedOnce = false
    private var mServiceIntent: Intent? = null
    private var deviceId = ""
    private var userName = ""

    private var _NetworkChangeReceiver: NetworkChangeReceiver? = null
    private var _ScreenStateReceiver: ScreenStateReceiver? = null
    private var _RingModeReceiver: RingModeReceiver? = null
    private var _LightSensorReceiver: LightSensorReceiver? = null
    lateinit var sharedPrefs: SharedPreferences
    lateinit var rankingString: String

    @Inject
    lateinit var db: FirebaseFirestore

    @Inject
    lateinit var auth: FirebaseAuth

    private var remotePushNews: MutableList<String> = mutableListOf()

    private lateinit var userDocRef: DocumentReference


    companion object {
        const val PERMISSION_REQUEST_CODE = 112
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("HardwareIds", "LongLogTag", "ApplySharedPref", "RestrictedApi", "BatteryLife")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = applicationContext
        setContentView(R.layout.activity_news_hybrid)
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        rankingString =
            sharedPrefs.getString(MEDIA_ORDER, MediaType.DEFAULT_MEDIA_ORDER)
                ?: MediaType.DEFAULT_MEDIA_ORDER
        deviceId = Settings.Secure.getString(
            applicationContext.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        userDocRef = db.collection(USER_COLLECTION).document(deviceId)

        loadCategoryFromLocal()
        initLayout()
        lifecycleScope.launch { getRemotePushNews() }

        FirebaseService.sharedPref = sharedPrefs
        FirebaseService.deviceId = deviceId
        FirebaseCrashlytics.getInstance().setUserId(deviceId)

        //first in app
        userName = intent.extras?.getString(SHARE_PREFERENCE_USER_NAME) ?: run {
            sharedPrefs.getString(SHARE_PREFERENCE_USER_NAME, UNKNOWN_USER_NAME)
        } ?: UNKNOWN_USER_NAME

        val clear = sharedPrefs.getBoolean(Constants.SHARE_PREFERENCE_CLEAR_CACHE, true)
        if (clear) {
            createNotificationChannel()
            checkNotificationPermission()
            val editor = sharedPrefs.edit()
            editor.putBoolean(Constants.SHARE_PREFERENCE_CLEAR_CACHE, false)
            editor.putString(MEDIA_ORDER, rankingString)
            editor.apply()

            val mediaPushResult: MutableList<String> = ArrayList()
            mediaPushResult.add(
                (sharedPrefs.getStringSet(
                    Constants.SHARE_PREFERENCE_PUSH_NEWS_MEDIA_LIST_SELECTION,
                    emptySet()
                ) ?: emptySet()).joinToString(",")
            )
            val userInitData = hashMapOf(
                USER_DEVICE_ID to deviceId,
                USER_FIRESTORE_ID to (auth.currentUser?.uid ?: ""),
                // TODO rename
                USER_SURVEY_NAME to (sharedPrefs.getString(
                    SHARE_PREFERENCE_USER_NAME,
                    UNKNOWN_USER_NAME
                ) ?: UNKNOWN_USER_NAME),
                USER_PHONE_ID to Build.MODEL,
                USER_ANDROID_SDK to Build.VERSION.SDK_INT,
                USER_ANDROID_RELEASE to Build.VERSION.RELEASE,
                MEDIA_BAR_ORDER to ArrayList<String>().apply { add(rankingString) },
                PUSH_MEDIA_SELECTION to mediaPushResult,
                UPDATE_TIME to Timestamp.now(),
                APP_VERSION_KEY to APP_VERSION_VALUE,
                // TODO FCM token?, check_last_news
            )

            lifecycleScope.launch {
                insertRemote(userDocRef, userInitData)
            }
        } else {
            val tmp = (sharedPrefs.getStringSet(
                Constants.SHARE_PREFERENCE_PUSH_NEWS_MEDIA_LIST_SELECTION,
                emptySet()
            ) ?: emptySet()).joinToString(",")

            if (remotePushNews.isEmpty() || remotePushNews[remotePushNews.size - 1] != tmp) {
                remotePushNews.add(tmp)
            }

            val dataMap = hashMapOf(
                PUSH_MEDIA_SELECTION to remotePushNews,
                USER_SURVEY_NAME to (sharedPrefs.getString(
                    SHARE_PREFERENCE_USER_NAME,
                    UNKNOWN_USER_NAME
                ) ?: UNKNOWN_USER_NAME),
                UPDATE_TIME to Timestamp.now(),
                APP_VERSION_KEY to APP_VERSION_VALUE,
                USER_DEVICE_ID to deviceId,
                USER_ANDROID_SDK to Build.VERSION.SDK_INT,
                USER_ANDROID_RELEASE to Build.VERSION.RELEASE
            )
            lifecycleScope.launch {
                updateRemote(userDocRef, dataMap)
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

    private fun initLayout() {
        swipeRefreshLayout = findViewById(R.id.mainSwipeContainer)
        swipeRefreshLayout.apply {
            setOnRefreshListener(this@NewsHybridActivity)
            setDistanceToTriggerSync(200)
            setColorSchemeResources(R.color.blue, R.color.red, R.color.black)
        }
        drawerLayout = findViewById(R.id.drawer_layout_hy)
        mSectionsPagerAdapter = SectionsPagerAdapter(
            supportFragmentManager, parseTabArray(rankingString)
        )
        mViewPager = findViewById(R.id.container_hy)
        tabLayout = findViewById(R.id.tabs_hy)
        mViewPager.adapter = mSectionsPagerAdapter
        tabLayout.setupWithViewPager(mViewPager)
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
                        "Hi, 我的 user id 是$userName，\ndevice id 是$deviceId，\n我有問題要回報(以文字描述發生的問題)：\n以下是相關問題截圖(如有截圖或是錄影，可以幫助我們更快了解問題)："
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            showDummyNotification()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    /**
     * Creates Notification Channel (required for API level >= 26) before sending any notification.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            TEST_CHANNEL_ID,
            DEFAULT_TEST_CHANNEL_ID,
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            description = "This notification contains important announcement, etc."
        }
        notificationManager.createNotificationChannel(channel)
    }

    private fun showDummyNotification() {
        val builder = NotificationCompat.Builder(this, TEST_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Congratulations! 🎉🎉🎉")
            .setContentText("You have post a notification to Android 13!!!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build())
        }
    }

    private suspend fun getRemotePushNews() {
        try {
            val documentSnapshot = withContext(Dispatchers.IO) { userDocRef.get().await() }
            if (documentSnapshot.exists()) {
                remotePushNews =
                    documentSnapshot[PUSH_MEDIA_SELECTION] as MutableList<String>
            }
        } catch (e: Exception) {
            Timber.d("get failed with " + e)
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
