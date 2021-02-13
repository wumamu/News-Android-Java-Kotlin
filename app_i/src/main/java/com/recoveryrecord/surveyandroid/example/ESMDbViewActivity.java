package com.recoveryrecord.surveyandroid.example;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class ESMDbViewActivity extends AppCompatActivity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esm_db_view);
        ESMDbHelper db = new ESMDbHelper(this);
        ArrayList<HashMap<String, String>> esmList = db.GetESMs();
        ListView lv = (ListView) findViewById(R.id.esm_list);
        ListAdapter adapter = new SimpleAdapter(
                ESMDbViewActivity.this,
                esmList,
                R.layout.list_row_esm,
                new String[]{"id","time","base_1", "base_2", "not_read_1", "not_read_2", "not_read_3", "not_read_4", "not_read_5", "read_1", "read_2", "read_3", "read_4", "read_5", "read_6", "read_7", "read_8", "read_9", "read_10", "read_11", "read_12", "read_13", "read_14", "read_15", "read_16", "read_17", "not_share_1", "share_1", "share_2"},
                new int[]{R.id.esm_id, R.id.time, R.id.base_1, R.id.base_2, R.id.not_read_1, R.id.not_read_2, R.id.not_read_3, R.id.not_read_4, R.id.not_read_5, R.id.read_1, R.id.read_2, R.id.read_3, R.id.read_4, R.id.read_5, R.id.read_6, R.id.read_7, R.id.read_8, R.id.read_9, R.id.read_10, R.id.read_11, R.id.read_12, R.id.read_13, R.id.read_14, R.id.read_15, R.id.read_16, R.id.read_17, R.id.not_share_1, R.id.share_1, R.id.share_2});
        lv.setAdapter(adapter);
    }
    @Override
    public void onBackPressed() {
        intent = new Intent(ESMDbViewActivity.this, ESMJsonViewActivity.class);
        startActivity(intent);
    }

}