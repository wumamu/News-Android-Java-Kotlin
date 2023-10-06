package com.recoveryrecord.surveyandroid.example.dailyreadinghistory

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.firebase.Timestamp
import com.recoveryrecord.surveyandroid.example.R
import com.recoveryrecord.surveyandroid.example.ReadingHistoryDailyFragment
import java.util.Calendar

class ReadingHistoryDailyMainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.nested_layer1_readinghistory, container, false)
        val mViewPager = view.findViewById<View>(R.id.container_main) as ViewPager
        val mSectionsPagerAdapter = SectionsPagerAdapter(
            childFragmentManager
        )
        mViewPager.adapter = mSectionsPagerAdapter
        return view
    }

    private inner class SectionsPagerAdapter(
        fm: FragmentManager?
    ) : FragmentPagerAdapter(
        fm!!
    ) {

        override fun getItem(position: Int): Fragment {
            Log.d("lognewsselect1231", "getItem$position")
            return when (position) {
                else -> ReadingHistoryDailyFragment.newInstance(position + 1)
            }
        }

        override fun getCount(): Int {
            // Show 4 total pages.
            return 30
        }

        override fun getPageTitle(position: Int): CharSequence? {
            val calendar = Calendar.getInstance()
            calendar.time = Timestamp.now().toDate()
            return when (position) {
                else -> {
                    calendar.add(Calendar.DAY_OF_YEAR, -position)
                    (calendar[Calendar.MONTH] + 1).toString() + "/" + calendar[Calendar.DAY_OF_MONTH]
                }
            }
        }
    }
}