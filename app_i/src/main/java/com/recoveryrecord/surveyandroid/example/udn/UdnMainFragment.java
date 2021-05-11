package com.recoveryrecord.surveyandroid.example.udn;

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

public class UdnMainFragment extends Fragment {

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
                    return Udn1Fragment.newInstance(1);
                case 1:
                    return Udn2Fragment.newInstance(2);
                case 2:
                    return Udn3Fragment.newInstance(3);
                case 3:
                    return Udn4Fragment.newInstance(4);
                case 4:
                    return Udn5Fragment.newInstance(5);
                case 5:
                    return Udn6Fragment.newInstance(6);
                case 6:
                    return Udn7Fragment.newInstance(7);
                case 7:
                    return Udn8Fragment.newInstance(8);
                case 8:
                    return Udn9Fragment.newInstance(9);
                case 9:
                    return Udn10Fragment.newInstance(10);
                case 10:
                    return Udn11Fragment.newInstance(11);
                case 11:
                    return Udn12Fragment.newInstance(12);
                case 12:
                    return Udn13Fragment.newInstance(13);
                case 13:
                    return Udn14Fragment.newInstance(14);
                default:
                    return TestNest2Fragment.newInstance(2);
            }

        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 14;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "要聞";
                case 1:
                    return "運動";
                case 2:
                    return "全球";
                case 3:
                    return "社會";
                case 4:
                    return "產經";
                case 5:
                    return "股市";
                case 6:
                    return "生活";
                case 7:
                    return "文教";
                case 8:
                    return "評論";
                case 9:
                    return "地方";
                case 10:
                    return "兩岸";
                case 11:
                    return "數位";
                case 12:
                    return "Oops";
                case 13:
                    return "閱讀";
                default:
                    return "Nested 2";
            }


        }
    }


}