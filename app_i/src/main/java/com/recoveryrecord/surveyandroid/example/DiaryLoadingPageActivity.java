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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_TARGET_RANGE;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_TIME_ON_PAGE_THRESHOLD;
import static com.recoveryrecord.surveyandroid.example.Constants.NA_STRING;
import static com.recoveryrecord.surveyandroid.example.Constants.NONE_STRING;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_TARGET_RANGE;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_TYPE_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_TYPE_VALUE_ESM;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_UNCLICKED_CANDIDATE;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_NOTI_ARRAY;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_READ_ARRAY;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_SAMPLE;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_TARGET_IN_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_TARGET_PLACE;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_TARGET_SITUATION;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_TARGET_TITLE;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_CLICK;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_NOTI_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_TITLE;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_CATEGORY;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_IN_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_OUT_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_SAMPLE_CHECK;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_SHARE;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_TIME_ON_PAGE;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_TITLE;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_TRIGGER_BY;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_TRIGGER_BY_NOTIFICATION;
import static com.recoveryrecord.surveyandroid.example.Constants.READ_HISTORY_CANDIDATE;
import static com.recoveryrecord.surveyandroid.example.Constants.TARGET_NEWS_TITLE;
import static com.recoveryrecord.surveyandroid.example.Constants.TEST_USER_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.ZERO_RESULT_STRING;



public class DiaryLoadingPageActivity extends AppCompatActivity {
    private Button button;
//    String output_file_name = "2.json";
    Task task, task2, task3;
//    Boolean finish = false;
    String diary_id = "";
    List<String> diary_option_array = new ArrayList<>();
//    List<String> notification_unclick_array = new ArrayList<>();
//    String result_json = "";
//    String sample_title = "";
//    String target_read_title = "NA";
//    List<String> tmp_share_Array = new ArrayList<>();
//    List<String> tmp_category_Array = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_page);
        if (getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();
            diary_id = Objects.requireNonNull(b.getString(DIARY_ID));
        }
        select_news_title_candidate();
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openESM();
            }
        });
    }


    public void openESM() {

        Intent intent_esm = new Intent();
        intent_esm.setClass(this, SurveyActivity.class);
        intent_esm.putExtra(ESM_ID, diary_id);//******************
        intent_esm.putExtra(NOTIFICATION_TYPE_KEY, NOTIFICATION_TYPE_VALUE_ESM);
//        Log.d("lognewsselect", "openESM" + esm_id);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> esm = new HashMap<>();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String ReadNewsTitle = sharedPrefs.getString(READ_HISTORY_CANDIDATE, ZERO_RESULT_STRING);
        String NotiNewTitle = sharedPrefs.getString(NOTIFICATION_UNCLICKED_CANDIDATE, ZERO_RESULT_STRING);
        final List<String> noti_news_title_array_json = new ArrayList<String>(Arrays.asList(NotiNewTitle.split("#")));
        final List<String> read_news_title_array_json = new ArrayList<String>(Arrays.asList(ReadNewsTitle.split("#")));

        @SuppressLint("HardwareIds")
        String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        if (!diary_id.equals("")){
            final DocumentReference rbRef = db.collection(TEST_USER_COLLECTION).document(device_id).collection(PUSH_ESM_COLLECTION).document(diary_id);
            rbRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        assert document != null;
                        if (document.exists()) {
                            rbRef.update(PUSH_ESM_READ_ARRAY, read_news_title_array_json,
                                    PUSH_ESM_NOTI_ARRAY, noti_news_title_array_json)//another field
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
        String TMP = sharedPrefs.getString(TARGET_NEWS_TITLE, "");
        Log.d("lognewsselect", "#################current sample history" + TMP);
        startActivity(intent_esm);
    }

    private void select_news_title_candidate() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
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
                        if (d.get(PUSH_ESM_TARGET_TITLE)!=null && !d.get(PUSH_ESM_TARGET_TITLE).equals("NA")){
//                            diary_option_array.add(d.get(PUSH_ESM_TARGET_TITLE) + "\n" + d.get(PUSH_ESM_TARGET_IN_TIME) + "\n" + d.get(PUSH_ESM_TARGET_SITUATION) + "\n" + d.get(PUSH_ESM_TARGET_PLACE));
                        }
//                        //mark as check
//                        db.collection(TEST_USER_COLLECTION)
//                                .document(device_id)
//                                .collection(PUSH_ESM_COLLECTION)
//                                .document(d.getId())
//                                .update(PUSH_ESM_SAMPLE, 3)
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        Log.d("lognewsselect", "DocumentSnapshot successfully updated!");
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Log.w("lognewsselect", "Error updating document", e);
//                                    }
//                                });

                    }
                }
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