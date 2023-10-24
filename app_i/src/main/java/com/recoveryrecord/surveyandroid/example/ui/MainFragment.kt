package com.recoveryrecord.surveyandroid.example.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.recoveryrecord.surveyandroid.example.R
import com.recoveryrecord.surveyandroid.example.model.MediaType
import com.recoveryrecord.surveyandroid.example.ui.adapter.CategoryTabAdapter

class MainFragment : Fragment() {
    private var source: String = ""

    companion object {
        const val MEDIA_SOURCE = "media_source"

        fun newInstance(source: String) =
            MainFragment().apply {
                arguments =
                    Bundle().apply {
                        putString(MEDIA_SOURCE, source)
                    }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            source = it.getString(MEDIA_SOURCE, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.nested_layer1_media, container, false)
        val mViewPager = view.findViewById<View>(R.id.container_main) as ViewPager2
        val tabLayout = view.findViewById<TabLayout>(R.id.tabs2)
        val mediaMap = MediaType.getCategoryMapByEnglishId(source)
        val mCategoryTabAdapter =
            CategoryTabAdapter(
                requireActivity(),
                source,
                mediaMap,
            )
        mViewPager.adapter = mCategoryTabAdapter
        TabLayoutMediator(tabLayout, mViewPager) { tab, position ->
            tab.text = mediaMap.getOrDefault(position, "")
        }.attach()
        return view
    }
}
