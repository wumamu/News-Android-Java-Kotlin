package com.recoveryrecord.surveyandroid.example.storm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.recoveryrecord.surveyandroid.example.TestNest2Fragment;
import com.recoveryrecord.surveyandroid.example.R;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class StormMainFragment extends Fragment {

//    private Toolbar toolbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab2_media, container, false);
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.container_main);
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
            switch (position) {
                case 0:
                    return Storm1Fragment.newInstance(1);
                case 1:
                    return Storm2Fragment.newInstance(2);
                case 2:
                    return Storm3Fragment.newInstance(3);
                case 3:
                    return Storm4Fragment.newInstance(4);
                case 4:
                    return Storm5Fragment.newInstance(5);
                case 5:
                    return Storm6Fragment.newInstance(6);
                case 6:
                    return Storm7Fragment.newInstance(7);
                case 7:
                    return Storm8Fragment.newInstance(8);
                case 8:
                    return Storm9Fragment.newInstance(9);
                case 9:
                    return Storm10Fragment.newInstance(10);
                default:
                    return TestNest2Fragment.newInstance(2);
            }

        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 10;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "國際";
                case 1:
                    return "歷史";
                case 2:
                    return "政治";
                case 3:
                    return "評論";
                case 4:
                    return "國內";
                case 5:
                    return "財經";
                case 6:
                    return "下班經濟學";
                case 7:
                    return "專欄";
                case 8:
                    return "中港澳";
                case 9:
                    return "台中";
                default:
                    return "Nested 2";
            }


        }
    }


}