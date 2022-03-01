package com.recoveryrecord.surveyandroid.example.dailyreadinghistory;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.Timestamp;
import com.recoveryrecord.surveyandroid.example.R;
import com.recoveryrecord.surveyandroid.example.ReadingHistoryDailyFragment;

import java.util.Calendar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ReadingHistoryDailyMainFragment extends Fragment {

//    private Toolbar toolbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nested_layer1_readinghistory, container, false);
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.container_main);
//        mViewPager.setRotationY(180);
//        mViewPager.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        return view;


    }


    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            //super.destroyItem(container, position, object);
//        }
        @Override
        public Fragment getItem(int position) {
            Log.d("lognewsselect1231", "getItem" + position);
            switch (position) {
                default:
                    return ReadingHistoryDailyFragment.newInstance(position+1);
            }

        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 30;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Timestamp.now().toDate());
            switch (position) {
                default: {
                    calendar.add(Calendar.DAY_OF_YEAR, -(position));
                    return (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.DAY_OF_MONTH);
                }
            }


        }
    }


}