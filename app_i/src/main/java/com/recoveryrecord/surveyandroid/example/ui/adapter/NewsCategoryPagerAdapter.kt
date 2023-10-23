package com.recoveryrecord.surveyandroid.example.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.recoveryrecord.surveyandroid.example.ui.NewsSubFragment

class NewsCategoryPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val source: String,
    private val mediaMap: Map<Int, String>,
) : FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return NewsSubFragment.newInstance(source, mediaMap.getOrDefault(position, ""))
    }

    override fun getItemCount(): Int {
        return mediaMap.size
    }
}
