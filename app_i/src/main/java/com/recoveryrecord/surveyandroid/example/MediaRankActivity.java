package com.recoveryrecord.surveyandroid.example;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.recoveryrecord.surveyandroid.example.model.MediaModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.recoveryrecord.surveyandroid.example.Constants.MEDIA_BAR_ORDER;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.SHARE_PREFERENCE_MAIN_PAGE_MEDIA_ORDER;
import static com.recoveryrecord.surveyandroid.example.Constants.USER_COLLECTION;

public class MediaRankActivity extends AppCompatActivity{

    private RecyclerView courseRV;
    private ArrayList<MediaModel> dataModalArrayList;
    private MediaRankRecycleViewAdapter dataRVAdapter;
    private FirebaseFirestore db;

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
//        dataRVAdapter = new MediaRankRecycleViewAdapter(dataModalArrayList, this, mListChangedListener);
        dataRVAdapter = new MediaRankRecycleViewAdapter(dataModalArrayList, this);
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
        Set<String> ranking = mSharedPreferences.getStringSet(SHARE_PREFERENCE_MAIN_PAGE_MEDIA_ORDER, null);
        for (int i = 1; i<=9; i++){
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
//        for(int i=0; i < dataModalArrayList.size(); i++){
//            System.out.println( dataModalArrayList.get(i).getMedia() );
//        }
        super.onStart();
    }

    @Override
    protected void onStop(){
        final Set<String> new_set = new HashSet<String>();
        for(int i=0; i < dataModalArrayList.size(); i++){
//            System.out.println( dataModalArrayList.get(i).getMedia());
            String tmp = dataModalArrayList.get(i).getMedia() + " " + (i+1);
            System.out.println(tmp);
            new_set.add(tmp);
        }

        //save to SharedPreference
        mEditor.putStringSet(SHARE_PREFERENCE_MAIN_PAGE_MEDIA_ORDER, new_set).commit();
        mEditor.commit();
        @SuppressLint("HardwareIds")
        String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        final DocumentReference rbRef = db.collection(USER_COLLECTION).document(device_id);
        final Set<String> tmpset = new_set;
        rbRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("log: firebase", "Success");
                        List<String> media_bar_result = (List<String>) document.get(MEDIA_BAR_ORDER);
                        media_bar_result.add(String.valueOf(tmpset));
//                                share_result.set(0,"none");
                        rbRef.update(MEDIA_BAR_ORDER, media_bar_result)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("log: firebase share", "DocumentSnapshot successfully updated!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("log: firebase share", "Error updating document", e);
                                    }
                                });
                    } else {
                        Log.d("log: firebase share", "No such document");
                    }
                } else {
                    Log.d("log: firebase share", "get failed with ", task.getException());
                }
            }
        });
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "可能要重啟app設定才會生效", Toast.LENGTH_SHORT).show();
        super.onDestroy();

    }




}
