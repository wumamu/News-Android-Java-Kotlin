package com.recoveryrecord.surveyandroid.example.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.recoveryrecord.surveyandroid.example.R
import com.recoveryrecord.surveyandroid.example.ui.EmptyFragment.Companion.newInstance
import com.recoveryrecord.surveyandroid.example.ui.PushHistoryFragment
import com.recoveryrecord.surveyandroid.example.ui.ReadHistorySummaryFragment

class PushHistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_push_history)
        title = "歷史推播紀錄"
        val mSectionsPagerAdapter = SectionsPagerAdapter(
            supportFragmentManager
        )
        val mViewPager = findViewById<ViewPager>(R.id.container)
        mViewPager.adapter = mSectionsPagerAdapter
        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(mViewPager)
    }

    class SectionsPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(
        fm!!
    ) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> PushHistoryFragment.newInstance()
                1 -> ReadHistorySummaryFragment.newInstance()
                else -> newInstance()
            }
        }

        override fun getCount(): Int {
            return 1
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return if (position == 0) {
                "近50則"
            } else null
        }
    }
}