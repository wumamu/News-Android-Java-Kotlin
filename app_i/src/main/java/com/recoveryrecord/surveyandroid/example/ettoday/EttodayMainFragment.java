package com.recoveryrecord.surveyandroid.example.ettoday;

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

public class EttodayMainFragment extends Fragment {

//    private Toolbar toolbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nested_layer1_media, container, false);
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
                    return Ettoday1Fragment.newInstance(1);
                case 1:
                    return Ettoday2Fragment.newInstance(2);
                case 2:
                    return Ettoday3Fragment.newInstance(3);
                case 3:
                    return Ettoday4Fragment.newInstance(4);
                case 4:
                    return Ettoday5Fragment.newInstance(5);
                case 5:
                    return Ettoday6Fragment.newInstance(6);
                case 6:
                    return Ettoday7Fragment.newInstance(7);
                case 7:
                    return Ettoday8Fragment.newInstance(8);
                case 8:
                    return Ettoday9Fragment.newInstance(9);
                case 9:
                    return Ettoday10Fragment.newInstance(10);
                case 10:
                    return Ettoday11Fragment.newInstance(11);
                case 11:
                    return Ettoday12Fragment.newInstance(12);
                case 12:
                    return Ettoday13Fragment.newInstance(13);
                case 13:
                    return Ettoday14Fragment.newInstance(14);
                case 14:
                    return Ettoday15Fragment.newInstance(15);
                case 15:
                    return Ettoday16Fragment.newInstance(16);
                case 16:
                    return Ettoday17Fragment.newInstance(17);
                case 17:
                    return Ettoday18Fragment.newInstance(18);
                case 18:
                    return Ettoday19Fragment.newInstance(19);
                default:
                    return TestNest2Fragment.newInstance(2);
            }

        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 19;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "社會";
                case 1:
                    return "影劇";
                case 2:
                    return "生活";
                case 3:
                    return "國際";
                case 4:
                    return "地方";
                case 5:
                    return "大陸";
                case 6:
                    return "軍武";
                case 7:
                    return "政治";
                case 8:
                    return "寵物動物";
                case 9:
                    return "體育";
                case 10:
                    return "健康";
                case 11:
                    return "旅遊";
                case 12:
                    return "新奇";
                case 13:
                    return "財經";
                case 14:
                    return "房產雲";
                case 15:
                    return "ET車雲";
                case 16:
                    return "遊戲";
                case 17:
                    return "3C家電";
                case 18:
                    return "法律";
                default:
                    return "Nested 2";
            }


        }
    }


}