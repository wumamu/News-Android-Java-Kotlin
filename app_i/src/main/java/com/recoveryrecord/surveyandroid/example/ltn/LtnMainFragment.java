package com.recoveryrecord.surveyandroid.example.ltn;

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

public class LtnMainFragment extends Fragment {

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
                    return Ltn1Fragment.newInstance(1);
                case 1:
                    return Ltn2Fragment.newInstance(2);
                case 2:
                    return Ltn3Fragment.newInstance(3);
                case 3:
                    return Ltn4Fragment.newInstance(4);
                case 4:
                    return Ltn5Fragment.newInstance(5);
                case 5:
                    return Ltn6Fragment.newInstance(6);
                case 6:
                    return Ltn7Fragment.newInstance(7);
                case 7:
                    return Ltn8Fragment.newInstance(8);
                case 8:
                    return Ltn9Fragment.newInstance(9);
                case 9:
                    return Ltn10Fragment.newInstance(10);
                case 10:
                    return Ltn11Fragment.newInstance(11);
                case 11:
                    return Ltn12Fragment.newInstance(12);
                case 12:
                    return Ltn13Fragment.newInstance(13);

                default:
                    return TestNest2Fragment.newInstance(2);
            }

        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 13;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
//                case 0:
//                    return "焦點";
                case 0:
                    return "政治";
                case 1:
                    return "社會";
                case 2:
                    return "生活";
                case 3:
                    return "國際";
                case 4:
                    return "財經";
//                case 6:
//                    return "言論";
                case 5:
                    return "體育";
                case 6:
                    return "娛樂";
                case 7:
                    return "3C";
                case 8:
                    return "汽車";
                case 9:
                    return "時尚";
                case 10:
                    return "蒐奇";
                case 11:
                    return "健康";
                case 12:
                    return "玩咖";
                default:
                    return "Nested 2";
            }


        }
    }


}