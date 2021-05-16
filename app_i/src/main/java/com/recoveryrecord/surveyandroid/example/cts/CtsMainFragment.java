package com.recoveryrecord.surveyandroid.example.cts;

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

public class CtsMainFragment extends Fragment {

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
                    return Cts1Fragment.newInstance(1);
                case 1:
                    return Cts2Fragment.newInstance(2);
                case 2:
                    return Cts3Fragment.newInstance(3);
                case 3:
                    return Cts4Fragment.newInstance(4);
                case 4:
                    return Cts5Fragment.newInstance(5);
                case 5:
                    return Cts6Fragment.newInstance(6);
                case 6:
                    return Cts7Fragment.newInstance(7);
                case 7:
                    return Cts8Fragment.newInstance(8);
                case 8:
                    return Cts9Fragment.newInstance(9);
                case 9:
                    return Cts10Fragment.newInstance(10);
                case 10:
                    return Cts11Fragment.newInstance(11);
                case 11:
                    return Cts12Fragment.newInstance(12);
                default:
                    return TestNest2Fragment.newInstance(2);
            }

        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 12;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "氣象";
                case 1:
                    return "社會";
                case 2:
                    return "政治";
                case 3:
                    return "娛樂";
                case 4:
                    return "國際";
                case 5:
                    return "生活";
                case 6:
                    return "運動";
                case 7:
                    return "財經";
                case 8:
                    return "地方";
                case 9:
                    return "藝文";
                case 10:
                    return "綜合";
                case 11:
                    return "校園";
                default:
                    return "Nested 2";
            }


        }
    }


}