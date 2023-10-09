package com.recoveryrecord.surveyandroid.example;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.recoveryrecord.surveyandroid.example.dailyreadinghistory.ReadingHistoryDailyMainFragment;
import com.recoveryrecord.surveyandroid.example.ui.EmptyFragment;
import com.recoveryrecord.surveyandroid.example.ui.ReadHistorySummaryFragment;
import com.recoveryrecord.surveyandroid.example.ui.ReadHistoryTimeFragment;
import com.recoveryrecord.surveyandroid.example.ui.ReadHistoryWeeklyFragment;
import com.recoveryrecord.surveyandroid.example.ui.ReadingHistoryFragment;

public class ReadHistoryActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_history);
        setTitle("歷史閱讀紀錄");
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    public static class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
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
                case 4:
                    return ReadHistoryTimeFragment.newInstance();
                default:
                    return EmptyFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "最近50則";
                case 1:
                    return "每日";
                case 2:
                    return "近一週";
                case 3:
                    return "總統計";
                case 4:
                    return "花費時間";
            }
            return null;
        }
    }
}