package com.recoveryrecord.surveyandroid.example;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.recoveryrecord.surveyandroid.example.chinatimes.ChinatimesMainFragment;
import com.recoveryrecord.surveyandroid.example.cna.CnaMainFragment;
import com.recoveryrecord.surveyandroid.example.cts.CtsMainFragment;
import com.recoveryrecord.surveyandroid.example.ebc.EbcMainFragment;
import com.recoveryrecord.surveyandroid.example.ettoday.EttodayMainFragment;
import com.recoveryrecord.surveyandroid.example.ltn.LtnMainFragment;
import com.recoveryrecord.surveyandroid.example.storm.StormMainFragment;
import com.recoveryrecord.surveyandroid.example.udn.UdnMainFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class NewHybridActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_hybrid);

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
                    return new ChinatimesMainFragment();
                case 1:
                    return new CnaMainFragment();
                case 2:
                    return new CtsMainFragment();
                case 3:
                    return new EbcMainFragment();
                case 4:
                    return new EttodayMainFragment();
                case 5:
                    return new UdnMainFragment();
                case 6:
                    return new LtnMainFragment();
                case 7:
                    return new StormMainFragment();
                default:
                    return Tab3Fragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 8;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "中時";
                case 1:
                    return "中央社";
                case 2:
                    return "華視";
                case 3:
                    return "東森";
                case 4:
                    return "ettoday";
                case 5:
                    return "聯合";
                case 6:
                    return "自由";
                case 7:
                    return "風傳媒";
            }
            return null;
        }
    }
}