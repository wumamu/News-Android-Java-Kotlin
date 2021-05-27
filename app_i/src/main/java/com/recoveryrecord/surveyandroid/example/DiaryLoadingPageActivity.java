package com.recoveryrecord.surveyandroid.example;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_EXIST_ESM_SAMPLE;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_READ_HISTORY_CANDIDATE;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_TARGET_RANGE;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_EXIST_READ_SAMPLE;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_TARGET_RANGE;
import static com.recoveryrecord.surveyandroid.example.Constants.LOADING_PAGE_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.LOADING_PAGE_TYPE_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_TYPE_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_TYPE_VALUE_DIARY;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_TYPE_VALUE_ESM;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_DIARY_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_DIARY_OPTION;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_NOTI_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_READ_EXIST;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_SAMPLE;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_SAMPLE_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_TARGET_IN_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_TARGET_PLACE;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_TARGET_SITUATION;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_TARGET_TITLE;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_TARGET_TITLE_DIARY;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_IN_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.SURVEY_PAGE_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.TEST_USER_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.ZERO_RESULT_STRING;



public class DiaryLoadingPageActivity extends AppCompatActivity {
    private Button button;
    Task task, task2, task3;
    String diary_id = "", type ="";
    Boolean exist_esm_sample = false;
//    TextView who;
    List<String> diary_option_array = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_page_diary);
        if (getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();
            diary_id = Objects.requireNonNull(b.getString(LOADING_PAGE_ID));
            type = Objects.requireNonNull(b.getString(LOADING_PAGE_TYPE_KEY));
        }
        if(diary_id.equals("")){
            Toast.makeText(this, "系統出錯，幫您導回主頁面", Toast.LENGTH_SHORT).show();
            Intent back = new Intent();
            back.setClass(getApplicationContext(), NewsHybridActivity.class);
            startActivity(back);
        }
        //initial
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(DIARY_READ_HISTORY_CANDIDATE, ZERO_RESULT_STRING);
        editor.apply();

        select_news_title_candidate();
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDiary();
            }
        });
    }


    public void openDiary() {
        Intent intent_diary = new Intent();
        intent_diary.setClass(this, SurveyActivity.class);
        intent_diary.putExtra(SURVEY_PAGE_ID, diary_id);//******************
        intent_diary.putExtra(NOTIFICATION_TYPE_KEY, NOTIFICATION_TYPE_VALUE_DIARY);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String diary_option = sharedPrefs.getString(DIARY_READ_HISTORY_CANDIDATE, ZERO_RESULT_STRING);
        final List<String> diary_option_array = new ArrayList<String>(Arrays.asList(diary_option.split("#")));
        @SuppressLint("HardwareIds")
        String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        if (!diary_id.equals("")){
            final DocumentReference rbRef = db.collection(TEST_USER_COLLECTION).document(device_id).collection(PUSH_DIARY_COLLECTION).document(diary_id);
            rbRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        assert document != null;
                        if (document.exists()) {
                            rbRef.update(PUSH_DIARY_OPTION, diary_option_array, DIARY_EXIST_ESM_SAMPLE, exist_esm_sample)//another field
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("lognewsselect", "DocumentSnapshot successfully updated!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("lognewsselect", "Error updating document", e);
                                        }
                                    });
                        } else {
                            Log.d("lognewsselect", "No such document");
                        }
                    } else {
                        Log.d("lognewsselect", "get failed with ", task.getException());
                    }
                }
            });
        }
        diary_id = "";
        startActivity(intent_diary);
    }

    private void select_news_title_candidate() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//        final Long now = System.currentTimeMillis();
//        final Calendar c = Calendar.getInstance();
//        c.setTimeInMillis(now);
        final Timestamp now_time_stamp = Timestamp.now();
        @SuppressLint("HardwareIds") final String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        task3 =  db.collection(TEST_USER_COLLECTION)
                .document(device_id)
                .collection(PUSH_ESM_COLLECTION)
                .whereEqualTo(PUSH_ESM_SAMPLE, 2)
                .get();
        task3.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        if(Math.abs(now_time_stamp.getSeconds() - d.getTimestamp(PUSH_ESM_NOTI_TIME).getSeconds()) <= DIARY_TARGET_RANGE){
                            if (d.getBoolean(PUSH_ESM_READ_EXIST)!=null && d.getBoolean(PUSH_ESM_READ_EXIST)){
//                            if (d.get(PUSH_ESM_TARGET_TITLE)!=null && !d.get(PUSH_ESM_TARGET_TITLE).equals("NA")){
                                String news_title = "NA";
                                String news_time = "NA";
                                String news_situation = "NA";
                                String news_place = "NA";
                                news_title = d.getString(PUSH_ESM_TARGET_TITLE_DIARY);
                                news_time = d.getString(PUSH_ESM_TARGET_IN_TIME);
                                news_situation = d.getString(PUSH_ESM_TARGET_SITUATION);
                                news_place = d.getString(PUSH_ESM_TARGET_PLACE);
                                diary_option_array.add(news_title + "\n" + news_time + "\n" + news_situation + "\n" + news_place);
                                exist_esm_sample = true;
                            }
                        }
                        //mark as check
                        db.collection(TEST_USER_COLLECTION)
                                .document(device_id)
                                .collection(PUSH_ESM_COLLECTION)
                                .document(d.getId())
                                .update(PUSH_ESM_SAMPLE, 3, PUSH_ESM_SAMPLE_ID, diary_id)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("lognewsselect", "DocumentSnapshot successfully updated!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("lognewsselect", "Error updating document", e);
                                    }
                                });

                    }

                }
                if(diary_option_array.size()!=0){
                    String title_array = "";
                    for(int i = 0; i< diary_option_array.size(); i++){
                        title_array+= diary_option_array.get(i) + "#";
                    }
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putString(DIARY_READ_HISTORY_CANDIDATE, title_array);
                    editor.apply();
                    Log.d("lognewsselect", "@@@@@@@@@@@@@");
                }
//                else {
//                    SharedPreferences.Editor editor = sharedPrefs.edit();
//                    editor.putString(DIARY_READ_HISTORY_CANDIDATE, ZERO_RESULT_STRING);
//                    editor.apply();
//                }
                Log.d("lognewsselect", "**********COMPLETE TASK3");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("lognewsselect", String.valueOf(e));
                // if we do not get any data or any error we are displaying
                // a toast message that we do not get any data
//                Toast.makeText(TestNewsOneActivity.this, "Fail to get the data.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onResume() {
        if(diary_id.equals("")){
            Intent back = new Intent();
            back.setClass(this, NewsHybridActivity.class);
            startActivity(back);
        }
        super.onResume();
    }
}