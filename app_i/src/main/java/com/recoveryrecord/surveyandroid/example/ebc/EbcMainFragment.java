package com.recoveryrecord.surveyandroid.example.ebc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.recoveryrecord.surveyandroid.example.Nest2Fragment;
import com.recoveryrecord.surveyandroid.example.R;
import com.recoveryrecord.surveyandroid.example.ebc.Ebc10Fragment;
import com.recoveryrecord.surveyandroid.example.ebc.Ebc1Fragment;
import com.recoveryrecord.surveyandroid.example.ebc.Ebc2Fragment;
import com.recoveryrecord.surveyandroid.example.ebc.Ebc3Fragment;
import com.recoveryrecord.surveyandroid.example.ebc.Ebc4Fragment;
import com.recoveryrecord.surveyandroid.example.ebc.Ebc5Fragment;
import com.recoveryrecord.surveyandroid.example.ebc.Ebc6Fragment;
import com.recoveryrecord.surveyandroid.example.ebc.Ebc7Fragment;
import com.recoveryrecord.surveyandroid.example.ebc.Ebc8Fragment;
import com.recoveryrecord.surveyandroid.example.ebc.Ebc9Fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class EbcMainFragment extends Fragment {

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
                    return Ebc1Fragment.newInstance(1);
                case 1:
                    return Ebc2Fragment.newInstance(2);
                case 2:
                    return Ebc3Fragment.newInstance(3);
                case 3:
                    return Ebc4Fragment.newInstance(4);
                case 4:
                    return Ebc5Fragment.newInstance(5);
                case 5:
                    return Ebc6Fragment.newInstance(6);
                case 6:
                    return Ebc7Fragment.newInstance(7);
                case 7:
                    return Ebc8Fragment.newInstance(8);
                case 8:
                    return Ebc9Fragment.newInstance(9);
                case 9:
                    return Ebc10Fragment.newInstance(10);
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
                    return "生活";
                case 1:
                    return "新奇";
                case 2:
                    return "社會";
                case 3:
                    return "娛樂";
                case 4:
                    return "國際";
                case 5:
                    return "政治";
                case 6:
                    return "E星聞";
                case 7:
                    return "星座";
                case 8:
                    return "財經";
                case 9:
                    return "體育";
                default:
                    return "Nested 2";
            }


        }
    }


}