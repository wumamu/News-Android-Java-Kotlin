package com.recoveryrecord.surveyandroid.example;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class MyPagerAdapter extends PagerAdapter {
    private List<View> mPager;
    private int childCount = 0;

    public MyPagerAdapter(List<View> mPager) {
        this.mPager = mPager;

    }
    @Override
    public int getItemPosition(@NonNull Object object) {
        if (childCount>0){
            childCount --;
            return POSITION_NONE;
        }
        return  super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return mPager.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        mPager.get(position).setTag(position);
        ((ViewPager) container).addView(mPager.get(position));
        return mPager.get(position);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object == view;
    }



    @Override
    public void notifyDataSetChanged() {
        childCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String media_name = "";
        switch (position) {
            case 0:
                media_name = "中時";
//                media_name = "中央社";
                break;
            case 1:
                media_name = "中央社";
                break;
            case 2:
                media_name = "華視";
                break;
            case 3:
                media_name = "東森";
                break;
            case 4:
                media_name = "自由時報";
                break;
            case 5:
                media_name = "風傳媒";
                break;
            case 6:
                media_name = "聯合";
                break;
            case 7:
                media_name = "ettoday";
                break;
            default:
                media_name = String.valueOf(position);
                break;

        }
        return media_name;
//        return "第"+(position+1)+"頁";
    }

}
