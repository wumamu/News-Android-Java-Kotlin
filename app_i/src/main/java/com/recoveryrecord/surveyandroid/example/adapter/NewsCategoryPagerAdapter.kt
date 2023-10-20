package com.recoveryrecord.surveyandroid.example.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.recoveryrecord.surveyandroid.example.ui.NewsSubFragment

class NewsCategoryPagerAdapter(
    fm: FragmentManager?,
    private val source: String,
    private val mediaMap: Map<Int, String>,
) : FragmentPagerAdapter(fm!!) {
    override fun getItem(position: Int): Fragment {
        return NewsSubFragment.newInstance(source, mediaMap.getOrDefault(position, ""))
    }

    override fun getCount(): Int {
        return mediaMap.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mediaMap.getOrDefault(position, "")
    }
}
