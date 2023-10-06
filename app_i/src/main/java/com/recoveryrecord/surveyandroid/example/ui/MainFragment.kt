package com.recoveryrecord.surveyandroid.example.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.recoveryrecord.surveyandroid.example.R
import com.recoveryrecord.surveyandroid.example.ui.Util.mediaMap

const val MEDIA_SOURCE = "media_source"

class MainFragment : Fragment() {
    private var source: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            source = it.getString(MEDIA_SOURCE, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.nested_layer1_media, container, false)
        val mViewPager = view.findViewById<View>(R.id.container_main) as ViewPager
        val mSectionsPagerAdapter =
            SectionsPagerAdapter(childFragmentManager, source, mediaMap[source] ?: emptyMap())
        mViewPager.adapter = mSectionsPagerAdapter
        return view
    }

    private inner class SectionsPagerAdapter(
        fm: FragmentManager?,
        private val source: String,
        private val mediaMap: Map<Int, String>
    ) : FragmentPagerAdapter(fm!!) {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun getItem(position: Int): Fragment {
            return NewsSubFragment.newInstance(source, mediaMap.getOrDefault(position, ""))
        }

        override fun getCount(): Int {
            return mediaMap.size
        }

        @RequiresApi(Build.VERSION_CODES.N)
        override fun getPageTitle(position: Int): CharSequence? {
            return mediaMap.getOrDefault(position, "")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(source: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(MEDIA_SOURCE, source)
                }
            }

    }
}