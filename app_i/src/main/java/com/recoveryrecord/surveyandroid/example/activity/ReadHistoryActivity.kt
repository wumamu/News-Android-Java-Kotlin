package com.recoveryrecord.surveyandroid.example.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.recoveryrecord.surveyandroid.example.R
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_ALL
import com.recoveryrecord.surveyandroid.example.ui.EmptyFragment.Companion.newInstance
import com.recoveryrecord.surveyandroid.example.ui.HistoryFragment
import com.recoveryrecord.surveyandroid.example.ui.ReadHistorySummaryFragment
import com.recoveryrecord.surveyandroid.example.ui.ReadHistoryTimeFragment
import com.recoveryrecord.surveyandroid.example.ui.ReadHistoryWeeklyFragment
import com.recoveryrecord.surveyandroid.example.ui.ReadingHistoryDailyMainFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReadHistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reading_history)
        title = "歷史閱讀紀錄"
        val mSectionsPagerAdapter =
            SectionsPagerAdapter(
                supportFragmentManager,
            )
        val mViewPager = findViewById<ViewPager>(R.id.container)
        mViewPager.adapter = mSectionsPagerAdapter
        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(mViewPager)
    }

    class SectionsPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(
        fm!!,
    ) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> HistoryFragment.newInstance(READING_ALL)
                1 -> ReadingHistoryDailyMainFragment()
                2 -> ReadHistoryWeeklyFragment.newInstance()
                3 -> ReadHistorySummaryFragment.newInstance()
                4 -> ReadHistoryTimeFragment.newInstance()
                else -> newInstance()
            }
        }

        override fun getCount(): Int {
            return 5
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return "最近50則"
                1 -> return "每日"
                2 -> return "近一週"
                3 -> return "總統計"
                4 -> return "花費時間"
            }
            return null
        }
    }
}
