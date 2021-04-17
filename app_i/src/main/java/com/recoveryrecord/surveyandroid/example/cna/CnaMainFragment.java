package com.recoveryrecord.surveyandroid.example.cna;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.recoveryrecord.surveyandroid.example.Nest2Fragment;
import com.recoveryrecord.surveyandroid.example.R;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class CnaMainFragment extends Fragment {

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

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return Cna1Fragment.newInstance(1);
                case 1:
                    return Cna2Fragment.newInstance(2);
                case 2:
                    return Cna3Fragment.newInstance(3);
                case 3:
                    return Cna4Fragment.newInstance(4);
                case 4:
                    return Cna5Fragment.newInstance(5);
                case 5:
                    return Cna6Fragment.newInstance(6);
                case 6:
                    return Cna7Fragment.newInstance(7);
                case 7:
                    return Cna8Fragment.newInstance(8);
                case 8:
                    return Cna9Fragment.newInstance(9);
                case 9:
                    return Cna10Fragment.newInstance(10);
                default:
                    return Nest2Fragment.newInstance(2);
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
                    return "證券";
                case 1:
                    return "國際";
                case 2:
                    return "新聞專題";
                case 3:
                    return "社會";
                case 4:
                    return "重點新聞";
                case 5:
                    return "地方";
                case 6:
                    return "政治";
                case 7:
                    return "兩岸";
                case 8:
                    return "生活";
                case 9:
                    return "產經";
                default:
                    return "Nested 2";
            }


        }
    }


}