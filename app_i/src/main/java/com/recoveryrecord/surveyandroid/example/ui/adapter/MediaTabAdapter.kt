package com.recoveryrecord.surveyandroid.example.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.recoveryrecord.surveyandroid.example.model.MediaType
import com.recoveryrecord.surveyandroid.example.ui.MainFragment

class MediaTabAdapter(
    fragmentActivity: FragmentActivity,
    private val tabs: Array<String>,
) : FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return MainFragment.newInstance(MediaType.getEnglish(tabs[position]))
    }

    override fun getItemCount(): Int {
        return tabs.size
    }
}
