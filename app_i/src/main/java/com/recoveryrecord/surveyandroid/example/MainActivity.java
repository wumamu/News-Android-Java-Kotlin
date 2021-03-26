package com.recoveryrecord.surveyandroid.example;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.recoveryrecord.surveyandroid.example.model.NewsModel;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

//import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {
//    private RecyclerView courseRV;
//    private ArrayList<NewsModel> dataModalArrayList;
//    private NewsRecycleViewAdapter dataRVAdapter;
//    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FirebaseApp.initializeApp();
        setContentView(R.layout.activity_main);
        setTitle("即時新聞");

        ArrayList<View> mPages = new ArrayList<>();
//        for (int i=0;i<5;i++) {
//            mPages.add(new Pagers(this, (i + 1)));
//        }
        int i = 0;
        mPages.add(new Pagers(this, (i + 1), "中時", "chinatimes"));
        mPages.add(new Pagers(this, (i + 1), "中央社", "cna"));
        mPages.add(new Pagers(this, (i + 1), "華視", "cts"));//cts
        mPages.add(new Pagers(this, (i + 1), "東森", "ebc"));
        mPages.add(new Pagers(this, (i + 1), "自由時報", "ltn"));
        mPages.add(new Pagers(this, (i + 1), "風傳媒", "storm"));
        mPages.add(new Pagers(this, (i + 1), "聯合", "udn"));
        mPages.add(new Pagers(this, (i + 1), "ettoday", "ettoday"));

        ViewPager viewPager = findViewById(R.id.mViewPager);
        TabLayout tab = findViewById(R.id.tab);
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(mPages);

        tab.setupWithViewPager(viewPager);
        viewPager.setAdapter(myPagerAdapter);
        viewPager.setCurrentItem(0);//指定跳到某頁，一定得設置在setAdapter後面
    }
}
