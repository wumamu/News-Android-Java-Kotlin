package com.recoveryrecord.surveyandroid.example.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.recoveryrecord.surveyandroid.example.ui.MainFragment
import com.recoveryrecord.surveyandroid.example.ui.MediaType

class SectionsPagerAdapter(
    fm: FragmentManager?,
    private val tabs: Array<String>
) : FragmentPagerAdapter(fm!!) {
    override fun getItem(position: Int): Fragment {
        return MainFragment.newInstance(MediaType.getEnglish(tabs[position]))
    }

    override fun getCount(): Int {
        return tabs.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabs[position]
    }
}

