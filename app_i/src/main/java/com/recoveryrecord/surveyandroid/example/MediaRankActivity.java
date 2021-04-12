package com.recoveryrecord.surveyandroid.example;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.recoveryrecord.surveyandroid.example.model.MediaModel;
import com.recoveryrecord.surveyandroid.example.model.Pagers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MediaRankActivity extends AppCompatActivity{

    private RecyclerView courseRV;
    private ArrayList<MediaModel> dataModalArrayList;
    private MediaRecycleViewAdapter dataRVAdapter;
    private FirebaseFirestore db;
//    private OnCustomerListChangedListener mListChangedListener;

    //temp for notification
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    boolean doubleBackToExitPressedOnce = false;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();

        setTitle("新聞APP偏好設定");
        setContentView(R.layout.activity_media_rank);

        // initializing our variables.
        courseRV = findViewById(R.id.mediaItems);

        // initializing our variable for firebase
        // firestore and getting its instance.
        db = FirebaseFirestore.getInstance();

        // creating our new array list
        dataModalArrayList = new ArrayList<>();
        courseRV.setHasFixedSize(true);

        // adding horizontal layout manager for our recycler view.
//        courseRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        courseRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // adding our array list to our recycler view adapter class.
//        dataRVAdapter = new MediaRecycleViewAdapter(dataModalArrayList, this, mListChangedListener);
        dataRVAdapter = new MediaRecycleViewAdapter(dataModalArrayList, this);
        // setting adapter to our recycler view.
        courseRV.setAdapter(dataRVAdapter);
        //drag and drop
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(dataRVAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(courseRV);

        Toast.makeText(this, "長按並拖移物件重新排序", Toast.LENGTH_SHORT).show();
        loadrecyclerViewData();


    }

    private void loadrecyclerViewData() {
        Set<String> ranking = mSharedPreferences.getStringSet("media_rank", null);
        for (int i = 1; i<=8; i++){
            for (String r : ranking){
                List<String> out= new ArrayList<String>(Arrays.asList(r.split(" ")));
                if(Integer.parseInt(out.get(1))==i){
                    dataModalArrayList.add(new MediaModel(out.get(0)));
                    continue;
                }
            }
        }
//        for(int i = 0; i<8)
//        dataModalArrayList.add(new MediaModel("中時"));
//        dataModalArrayList.add(new MediaModel("中央社"));
//        dataModalArrayList.add(new MediaModel("華視"));
//        dataModalArrayList.add(new MediaModel("東森"));
//        dataModalArrayList.add(new MediaModel("自由時報"));
//        dataModalArrayList.add(new MediaModel("風傳媒"));
//        dataModalArrayList.add(new MediaModel("聯合(udn)"));
//        dataModalArrayList.add(new MediaModel("ETTODAY"));
        dataRVAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }




}
