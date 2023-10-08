package com.recoveryrecord.surveyandroid.example

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
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
import com.recoveryrecord.surveyandroid.example.receiever.LightSensorReceiver
import com.recoveryrecord.surveyandroid.example.receiever.NetworkChangeReceiver
import com.recoveryrecord.surveyandroid.example.receiever.RingModeReceiver
import com.recoveryrecord.surveyandroid.example.receiever.ScreenStateReceiver
import com.recoveryrecord.surveyandroid.example.ui.MainFragment.Companion.newInstance
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Date

class NewsHybridActivity
    : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OnRefreshListener {
    private var signature: String? = null
    private var drawerLayout: DrawerLayout? = null

    private var swipeRefreshLayout: MySwipeRefreshLayout? = null
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("HardwareIds", "LongLogTag", "ApplySharedPref", "RestrictedApi", "BatteryLife")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = applicationContext
        setContentView(R.layout.activity_news_hybrid)
        FirebaseCrashlytics.getInstance().setUserId(deviceId)

        //initial value for user (first time)
        val db = FirebaseFirestore.getInstance()
        deviceId = Settings.Secure.getString(
            applicationContext.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        //first in app
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(
            applicationContext
        )
        val clear = sharedPrefs.getBoolean(Constants.SHARE_PREFERENCE_CLEAR_CACHE, true)
        val first: MutableMap<String, Any?> = HashMap()
        val extras = intent.extras
        signature = if (extras != null) {
            extras.getString(Constants.USER_NUM)
            //The key argument here must match that used in the other activity
        } else {
            sharedPrefs.getString(Constants.SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號")
        }
        if (clear) {
            val editor = sharedPrefs.edit()
            editor.putString(Constants.SHARE_PREFERENCE_DEVICE_ID, deviceId)
            editor.putBoolean(Constants.SHARE_PREFERENCE_CLEAR_CACHE, false)
            editor.apply()
            //initial media list
            val ranking = sharedPrefs.getStringSet(
                Constants.SHARE_PREFERENCE_MAIN_PAGE_MEDIA_ORDER,
                emptySet()
            )
            if (ranking!!.isEmpty()) {
                val set: MutableSet<String> = HashSet()
                set.add("中時 1")
                set.add("中央社 2")
                set.add("華視 3")
                set.add("東森 4")
                set.add("自由時報 5")
                set.add("風傳媒 6")
                set.add("聯合 7")
                set.add("ettoday 8")
                set.add("三立 9")
                val edit = sharedPrefs.edit()
                //                edit.clear();
                edit.putStringSet(Constants.SHARE_PREFERENCE_MAIN_PAGE_MEDIA_ORDER, set)
                edit.apply()
            }
            val media_bar_result: MutableList<String> = ArrayList()
            val media_push_result: MutableList<String> = ArrayList()
            media_bar_result.add(
                sharedPrefs.getStringSet(
                    Constants.SHARE_PREFERENCE_MAIN_PAGE_MEDIA_ORDER,
                    emptySet()
                ).toString()
            )
            media_push_result.add(
                java.lang.String.join(
                    ",",
                    sharedPrefs.getStringSet(
                        Constants.SHARE_PREFERENCE_PUSH_NEWS_MEDIA_LIST_SELECTION,
                        emptySet()
                    ).toString()
                )
            )
            first[Constants.MEDIA_BAR_ORDER] = media_bar_result
            first[Constants.USER_DEVICE_ID] = deviceId
            val mAuth = FirebaseAuth.getInstance()
            val user = mAuth.currentUser!!
            first[Constants.USER_FIRESTORE_ID] = user.uid
            first[Constants.USER_SURVEY_NUMBER] =
                sharedPrefs.getString(Constants.SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號")
            first[Constants.USER_PHONE_ID] = Build.MODEL
            first[Constants.USER_ANDROID_SDK] = Build.VERSION.SDK_INT
            first[Constants.USER_ANDROID_RELEASE] = Build.VERSION.RELEASE
            first[Constants.UPDATE_TIME] = Timestamp.now()
            first[Constants.APP_VERSION_KEY] = Constants.APP_VERSION_VALUE
            first[Constants.PUSH_MEDIA_SELECTION] = media_push_result
            first["check_last_service"] = Timestamp(0, 0)
            first["check_last_schedule"] = Timestamp(0, 0)
            first["check_last_news"] = Timestamp(0, 0)
            first["check_last_diary"] = Timestamp(0, 0)
            first["check_last_esm"] = Timestamp(0, 0)
            db.collection(Constants.USER_COLLECTION)
                .document(deviceId)
                .set(first)
            db.collection(Constants.TEST_USER_COLLECTION)
                .document(deviceId)
                .set(first)
        } else {
            val rbRef = db.collection(Constants.USER_COLLECTION).document(deviceId)
            rbRef.get().addOnCompleteListener { task: Task<DocumentSnapshot?> ->
                if (task.isSuccessful) {
                    val document = task.result!!
                    if (document.exists()) {
                        val media_push_result =
                            (document[Constants.PUSH_MEDIA_SELECTION] as MutableList<String>?)!!
                        val tmp = java.lang.String.join(
                            ",",
                            sharedPrefs.getStringSet(
                                Constants.SHARE_PREFERENCE_PUSH_NEWS_MEDIA_LIST_SELECTION,
                                emptySet()
                            ).toString()
                        )
                        if (media_push_result[media_push_result.size - 1] != tmp) {
                            media_push_result.add(tmp)
                        }
                        rbRef.update(
                            Constants.PUSH_MEDIA_SELECTION,
                            media_push_result,
                            Constants.USER_SURVEY_NUMBER,
                            sharedPrefs.getString(
                                Constants.SHARE_PREFERENCE_USER_ID,
                                "尚未設定實驗編號"
                            ),
                            Constants.UPDATE_TIME,
                            Timestamp.now(),
                            Constants.APP_VERSION_KEY,
                            Constants.APP_VERSION_VALUE,
                            "check_last_service",
                            Timestamp.now(),
                            Constants.ESM_PUSH_TOTAL,
                            sharedPrefs.getInt(Constants.ESM_PUSH_TOTAL, 0),
                            Constants.ESM_DONE_TOTAL,
                            sharedPrefs.getInt(Constants.ESM_DONE_TOTAL, 0),
                            Constants.DIARY_PUSH_TOTAL,
                            sharedPrefs.getInt(Constants.DIARY_PUSH_TOTAL, 0),
                            Constants.DIARY_DONE_TOTAL,
                            sharedPrefs.getInt(Constants.DIARY_DONE_TOTAL, 0),
                            Constants.USER_DEVICE_ID,
                            deviceId,
                            Constants.USER_ANDROID_SDK,
                            Build.VERSION.SDK_INT,
                            Constants.USER_ANDROID_RELEASE,
                            Build.VERSION.RELEASE
                        )
                            .addOnSuccessListener {
                                Log.d(
                                    "log: firebase share",
                                    "DocumentSnapshot successfully updated!"
                                )
                            }
                            .addOnFailureListener { e: Exception? ->
                                Log.w(
                                    "log: firebase share",
                                    "Error updating document",
                                    e
                                )
                            }
                    } else {
                        Log.d("log: firebase share", "No such document")
                    }
                } else {
                    Log.d("log: firebase share", "get failed with ", task.exception)
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
        val user_phone = header.findViewById<TextView>(R.id.textView_user_phone)
        user_phone.text = Build.MODEL
        val user_id = header.findViewById<TextView>(R.id.textView_user_id)
        user_id.text = deviceId
        val user_name = header.findViewById<TextView>(R.id.textView_user_name)
        user_name.text = signature
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

        //check notification service running
        val mYourService = NewsNotificationService()
        mServiceIntent = Intent(this, NewsNotificationService::class.java)
        val log_service: MutableMap<String, Any> = HashMap()
        if (!isMyServiceRunning(mYourService.javaClass)) {
            log_service[Constants.NEWS_SERVICE_STATUS_KEY] =
                Constants.NEWS_SERVICE_STATUS_VALUE_RESTART
            log_service[Constants.NEWS_SERVICE_CYCLE_KEY] =
                Constants.NEWS_SERVICE_CYCLE_VALUE_FAILED_RESTART
            startService(mServiceIntent)
        } else {
            log_service[Constants.NEWS_SERVICE_STATUS_KEY] =
                Constants.NEWS_SERVICE_STATUS_VALUE_RUNNING
            log_service[Constants.NEWS_SERVICE_CYCLE_KEY] =
                Constants.NEWS_SERVICE_CYCLE_VALUE_MAIN_PAGE
        }
        log_service[Constants.NEWS_SERVICE_TIME] = Timestamp.now()
        log_service[Constants.NEWS_SERVICE_DEVICE_ID] = deviceId
        val date = Date(System.currentTimeMillis())
        @SuppressLint("SimpleDateFormat") val formatter =
            SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z")
        db.collection(Constants.NEWS_SERVICE_COLLECTION)
            .document(deviceId + " " + formatter.format(date))
            .set(log_service)
        mSectionsPagerAdapter = SectionsPagerAdapter(
            supportFragmentManager, this
        )
        mViewPager = findViewById(R.id.container_hy)
        tabLayout = findViewById(R.id.tabs_hy)
        mViewPager?.adapter = mSectionsPagerAdapter
        tabLayout?.setupWithViewPager(mViewPager)

        //Network
        _NetworkChangeReceiver = NetworkChangeReceiver()
        _NetworkChangeReceiver!!.registerNetworkReceiver(this)

        //Screen
        _ScreenStateReceiver = ScreenStateReceiver()
        _ScreenStateReceiver!!.registerScreenStateReceiver(this)

        //RingMode
        _RingModeReceiver = RingModeReceiver()
        _RingModeReceiver!!.registerRingModeReceiver(this)

        //LightSensor
        _LightSensorReceiver = LightSensorReceiver()
        _LightSensorReceiver!!.registerLightSensorReceiver(this)
    }

    @SuppressLint("HardwareIds")
    override fun onResume() {
        Log.e("onResume", "onResume")
        super.onResume()
    }

    @SuppressLint("LongLogTag")
    override fun onStart() {
        super.onStart()
    }

    @SuppressLint("HardwareIds")
    override fun onPause() {
        Log.d("log: activity cycle", "NewsHybridActivity On Pause")
        super.onPause()
    }

    @SuppressLint("LongLogTag")
    override fun onRestart() {
        super.onRestart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        _NetworkChangeReceiver!!.unregisterNetworkReceiver(this)
        _ScreenStateReceiver!!.unregisterScreenStateReceiver(this)
        _RingModeReceiver!!.unregisterBluetoothReceiver(this)
        _LightSensorReceiver!!.unregisterLightSensorReceiver(this)
        super.onDestroy()
    }

    @SuppressLint("NonConstantResourceId", "QueryPermissionsNeeded")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val intent: Intent? = when (item.itemId) {
            R.id.nav_setting -> Intent(this@NewsHybridActivity, SettingsActivity::class.java)
            R.id.nav_history -> Intent(this@NewsHybridActivity, ReadHistoryActivity::class.java)
            R.id.nav_reschedule -> Intent(this@NewsHybridActivity, PushHistoryActivity::class.java)
            R.id.nav_contact -> {
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

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    override fun onBackPressed() {
        if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout!!.closeDrawer(GravityCompat.START)
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
        mSectionsPagerAdapter = SectionsPagerAdapter(
            supportFragmentManager, context
        )
        mViewPager!!.adapter = mSectionsPagerAdapter
        tabLayout!!.setupWithViewPager(mViewPager)
        swipeRefreshLayout!!.isRefreshing = false
    }

    class SectionsPagerAdapter(fm: FragmentManager?, private val context: Context?) :
        FragmentPagerAdapter(
            fm!!
        ) {
        override fun getItem(position: Int): Fragment {
            val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(
                context
            )
            val ranking = sharedPrefs.getStringSet(
                Constants.SHARE_PREFERENCE_MAIN_PAGE_MEDIA_ORDER,
                emptySet()
            )
            var media_name = ""
            return if (ranking!!.isEmpty()) {
                when (position) {
                    0 -> for (r in ranking) {
                        val out: List<String> = ArrayList(
                            Arrays.asList(
                                *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()))
                        if (out[1].toInt() == 1) {
                            media_name = out[0]
                            break
                        }
                    }

                    1 -> for (r in ranking) {
                        val out: List<String> = ArrayList(
                            Arrays.asList(
                                *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()))
                        if (out[1].toInt() == 2) {
                            media_name = out[0]
                            break
                        }
                    }

                    2 -> for (r in ranking) {
                        val out: List<String> = ArrayList(
                            Arrays.asList(
                                *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()))
                        if (out[1].toInt() == 3) {
                            media_name = out[0]
                            break
                        }
                    }

                    3 -> for (r in ranking) {
                        val out: List<String> = ArrayList(
                            Arrays.asList(
                                *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()))
                        if (out[1].toInt() == 4) {
                            media_name = out[0]
                            break
                        }
                    }

                    4 -> for (r in ranking) {
                        val out: List<String> = ArrayList(
                            Arrays.asList(
                                *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()))
                        if (out[1].toInt() == 5) {
                            media_name = out[0]
                            break
                        }
                    }

                    5 -> for (r in ranking) {
                        val out: List<String> = ArrayList(
                            Arrays.asList(
                                *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()))
                        if (out[1].toInt() == 6) {
                            media_name = out[0]
                            break
                        }
                    }

                    6 -> for (r in ranking) {
                        val out: List<String> = ArrayList(
                            Arrays.asList(
                                *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()))
                        if (out[1].toInt() == 7) {
                            media_name = out[0]
                            break
                        }
                    }

                    7 -> for (r in ranking) {
                        val out: List<String> = ArrayList(
                            Arrays.asList(
                                *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()))
                        if (out[1].toInt() == 8) {
                            media_name = out[0]
                            break
                        }
                    }

                    8 -> for (r in ranking) {
                        val out: List<String> = ArrayList(
                            Arrays.asList(
                                *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()))
                        if (out[1].toInt() == 9) {
                            media_name = out[0]
                            break
                        }
                    }
                }
                Log.d("mediaselect", "$media_name   565")
                when (media_name) {
                    "中時" -> newInstance("chinatimes")
                    "中央社" -> newInstance("cna")
                    "華視" -> newInstance("cts")
                    "東森" -> newInstance("ebc")
                    "ettoday" -> newInstance("ettoday")
                    "聯合" -> newInstance("udn")
                    "自由時報" -> newInstance("ltn")
                    "風傳媒" -> newInstance("storm")
                    "三立" -> newInstance("setn")
                    else -> TestTab3Fragment.newInstance()
                }
            } else {
                when (position) {
                    0 -> newInstance("chinatimes")
                    1 -> newInstance("cna")
                    2 -> newInstance("cts")
                    3 -> newInstance("ebc")
                    4 -> newInstance("ettoday")
                    5 -> newInstance("udn")
                    6 -> newInstance("ltn")
                    7 -> newInstance("storm")
                    8 -> newInstance("setn")
                    else -> TestTab3Fragment.newInstance()
                }
            }
        }

        override fun getCount(): Int {
            return 9
        }

        override fun getPageTitle(position: Int): CharSequence? {
            val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(
                context
            )
            val ranking = sharedPrefs.getStringSet(
                Constants.SHARE_PREFERENCE_MAIN_PAGE_MEDIA_ORDER,
                emptySet()
            )
            return if (!ranking!!.isEmpty()) {
                when (position) {
                    0 -> {
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 1) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 2) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 3) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 4) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 5) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 6) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 7) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 8) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 9) {
                                return out[0]
                            }
                        }
                        null
                    }

                    1 -> {
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 2) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 3) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 4) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 5) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 6) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 7) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 8) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 9) {
                                return out[0]
                            }
                        }
                        null
                    }

                    2 -> {
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 3) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 4) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 5) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 6) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 7) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 8) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 9) {
                                return out[0]
                            }
                        }
                        null
                    }

                    3 -> {
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 4) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 5) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 6) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 7) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 8) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 9) {
                                return out[0]
                            }
                        }
                        null
                    }

                    4 -> {
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 5) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 6) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 7) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                listOf(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 8) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                listOf(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 9) {
                                return out[0]
                            }
                        }
                        null
                    }

                    5 -> {
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                listOf(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 6) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                listOf(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 7) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 8) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 9) {
                                return out[0]
                            }
                        }
                        null
                    }

                    6 -> {
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 7) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 8) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 9) {
                                return out[0]
                            }
                        }
                        null
                    }

                    7 -> {
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 8) {
                                return out[0]
                            }
                        }
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 9) {
                                return out[0]
                            }
                        }
                        null
                    }

                    8 -> {
                        for (r in ranking) {
                            val out: List<String> = ArrayList(
                                Arrays.asList(
                                    *r.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()))
                            if (out[1].toInt() == 9) {
                                return out[0]
                            }
                        }
                        null
                    }

                    else -> null
                }
            } else {
                when (position) {
                    0 -> return "中時"
                    1 -> return "中央社"
                    2 -> return "華視"
                    3 -> return "東森"
                    4 -> return "ettoday"
                    5 -> return "聯合"
                    6 -> return "自由"
                    7 -> return "風傳媒"
                    8 -> return "三立"
                }
                null
            }
        }
    }
}