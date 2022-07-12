package com.recoveryrecord.surveyandroid.example.ui.news.chinatimes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.recoveryrecord.surveyandroid.example.R;

public class ChinatimesMainFragment extends Fragment {

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
//            Log.d("lognewsselect1231", "getItemchina" + position);
            switch (position) {
                case 0:
                    return Chinatimes1Fragment.newInstance(1);
                case 1:
                    return Chinatimes2Fragment.newInstance(2);
                case 2:
                    return Chinatimes3Fragment.newInstance(3);
                case 3:
                    return Chinatimes4Fragment.newInstance(4);
                case 4:
                    return Chinatimes5Fragment.newInstance(5);
                case 5:
                    return Chinatimes6Fragment.newInstance(6);
                case 6:
                    return Chinatimes7Fragment.newInstance(7);
                case 7:
                    return Chinatimes8Fragment.newInstance(8);
                case 8:
                    return Chinatimes9Fragment.newInstance(9);
                case 9:
                    return Chinatimes10Fragment.newInstance(10);
                case 10:
                    return Chinatimes11Fragment.newInstance(11);
                case 11:
                    return Chinatimes12Fragment.newInstance(12);
                case 12:
                    return Chinatimes13Fragment.newInstance(13);
                default:
                    return Chinatimes13Fragment.newInstance(1);
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
                case 0:
                    return "政治";
                case 1:
                    return "生活";
                case 2:
                    return "娛樂";
                case 3:
                    return "財經";
                case 4:
                    return "國際";
                case 5:
                    return "兩岸";
                case 6:
                    return "社會";
                case 7:
                    return "軍事";
                case 8:
                    return "科技";
                case 9:
                    return "體育";
                case 10:
                    return "健康";
                case 11:
                    return "運勢";
                case 12:
                    return "寶島";
                default:
                    return "Nested 2";
            }


        }
    }


}