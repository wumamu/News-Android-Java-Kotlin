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
//                case 6:
//                    return Storm7Fragment.newInstance(7);
//                case 7:
//                    return Storm8Fragment.newInstance(8);
//                case 8:
//                    return Storm9Fragment.newInstance(9);
                case 6:
                    return Storm10Fragment.newInstance(10);
                case 7:
                    return Storm11Fragment.newInstance(11);
                case 8:
                    return Storm12Fragment.newInstance(12);
                case 9:
                    return Storm13Fragment.newInstance(13);
                case 10:
                    return Storm14Fragment.newInstance(14);
                case 11:
                    return Storm15Fragment.newInstance(15);
                case 12:
                    return Storm16Fragment.newInstance(16);
                case 13:
                    return Storm17Fragment.newInstance(17);
                case 14:
                    return Storm18Fragment.newInstance(18);
                case 15:
                    return Storm19Fragment.newInstance(19);
                case 16:
                    return Storm20Fragment.newInstance(20);
                case 17:
                    return Storm21Fragment.newInstance(21);
                case 18:
                    return Storm22Fragment.newInstance(22);
                case 19:
                    return Storm23Fragment.newInstance(23);
                case 20:
                    return Storm24Fragment.newInstance(24);
                case 21:
                    return Storm25Fragment.newInstance(25);
                case 22:
                    return Storm26Fragment.newInstance(26);
                case 23:
                    return Storm27Fragment.newInstance(27);
                default:
                    return TestNest2Fragment.newInstance(2);
            }

        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 24;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "新新聞";
                case 1:
                    return "華爾街";
                case 2:
                    return "國內";
                case 3:
                    return "政治";
                case 4:
                    return "國際";
                case 5:
                    return "財經";
//                case 6:
//                    return "下班經濟學";
//                case 7:
//                    return "中港澳";
//                case 8:
//                    return "台中";
                case 6:
                    return "專欄";
                case 7:
                    return "評論";
                case 8:
                    return "調查";
                case 9:
                    return "娛樂";
                case 10:
                    return "運動";
                case 11:
                    return "軍事";
                case 12:
                    return "科技";
                case 13:
                    return "職場";
                case 14:
                    return "健康";
                case 15:
                    return "兩性";
                case 16:
                    return "旅遊";
                case 17:
                    return "美食";
                case 18:
                    return "影音";
                case 19:
                    return "汽車";
                case 20:
                    return "房地產";
                case 21:
                    return "歷史";
                case 22:
                    return "藝文";
                case 23:
                    return "人物";
                default:
                    return "Nested 2";
            }


        }
    }


}