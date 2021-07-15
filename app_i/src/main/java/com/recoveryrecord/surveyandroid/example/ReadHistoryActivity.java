package com.recoveryrecord.surveyandroid.example;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.recoveryrecord.surveyandroid.example.chinatimes.ChinatimesMainFragment;
import com.recoveryrecord.surveyandroid.example.dailyreadinghistory.ReadingHistoryDailyMainFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ReadHistoryActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_history);
        setTitle("歷史閱讀紀錄");
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return ReadingHistoryFragment.newInstance();
                case 1:
//                    return ReadingHistoryDailyFragment.newInstance();
                    return new ReadingHistoryDailyMainFragment();
                case 2:
                    return ReadHistoryWeeklyFragment.newInstance();
                case 3:
                    return ReadHistorySummaryFragment.newInstance();
                default:
                    return TestTab3Fragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "近50則";
                case 1:
                    return "每日";
                case 2:
                    return "一週統計";
                case 3:
                    return "總統計";
            }
            return null;
        }
    }
}