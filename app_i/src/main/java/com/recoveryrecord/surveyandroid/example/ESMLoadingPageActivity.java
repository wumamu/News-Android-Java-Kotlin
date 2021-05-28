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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

//import static com.recoveryrecord.surveyandroid.example.Constants.CATEGORY_HASH_SET_PREFIX;
//import static com.recoveryrecord.surveyandroid.example.Constants.CATEGORY_HASH_SET_SIZE;
//import static com.recoveryrecord.surveyandroid.example.Constants.ESM_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_EXIST_NOTIFICATION_SAMPLE;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_EXIST_READ_SAMPLE;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_NOTIFICATION_EXIST;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_NOT_SAMPLE_NOTIFICATION_FAR;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_NOT_SAMPLE_READ_FAR;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_NOT_SAMPLE_READ_SHORT;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_READ_EXIST;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_SAMPLE_CHECK_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_SAMPLE_CHECK_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.SURVEY_PAGE_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_TARGET_RANGE;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_TIME_ON_PAGE_THRESHOLD;
import static com.recoveryrecord.surveyandroid.example.Constants.LOADING_PAGE_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.LOADING_PAGE_TYPE_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.NA_STRING;
import static com.recoveryrecord.surveyandroid.example.Constants.NONE_STRING;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_TARGET_RANGE;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_TYPE_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_TYPE_VALUE_ESM;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_NOTIFICATION_UNCLICKED_CANDIDATE;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_CLICK;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_NOTI_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_TITLE;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_NOTI_ARRAY;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_READ_ARRAY;
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
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_READ_HISTORY_CANDIDATE;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_TARGET_NEWS_TITLE;
import static com.recoveryrecord.surveyandroid.example.Constants.TEST_USER_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.ZERO_RESULT_STRING;

public class ESMLoadingPageActivity extends AppCompatActivity {
    private Button button;
    String output_file_name = "2.json";
    Task task, task2, task3;
    Boolean exist_read = false, exist_notification = false;
    String esm_id = "", type ="";
    List<String> news_title_target_array = new ArrayList<>();
    List<String> not_sample_short = new ArrayList<>();
    List<String> not_sample_far = new ArrayList<>();
    List<String> not_sample_far_noti = new ArrayList<>();
    List<String> notification_unclick_array = new ArrayList<>();
    String result_json = "";
    String sample_title = "";
    String target_read_title = "NA";
    List<String> tmp_share_Array = new ArrayList<>();
    List<String> tmp_category_Array = new ArrayList<>();
//    TextView who;
//    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_page_esm);
        if (getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();
            esm_id = Objects.requireNonNull(b.getString(LOADING_PAGE_ID));
            type = Objects.requireNonNull(b.getString(LOADING_PAGE_TYPE_KEY));
        }
        if(esm_id.equals("")){
            Toast.makeText(this, "系統出錯，幫您導回主頁面", Toast.LENGTH_SHORT).show();
            Intent back = new Intent();
            back.setClass(getApplicationContext(), NewsHybridActivity.class);
            startActivity(back);
        }

        //initial
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(ESM_READ_HISTORY_CANDIDATE, ZERO_RESULT_STRING);
        editor.putString(ESM_NOTIFICATION_UNCLICKED_CANDIDATE, ZERO_RESULT_STRING);
        editor.apply();
        //find
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
        intent_esm.putExtra(SURVEY_PAGE_ID, esm_id);//******************
        intent_esm.putExtra(NOTIFICATION_TYPE_KEY, NOTIFICATION_TYPE_VALUE_ESM);
        intent_esm.putExtra(ESM_EXIST_READ_SAMPLE, exist_read);
        intent_esm.putExtra(ESM_EXIST_NOTIFICATION_SAMPLE, exist_notification);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> esm = new HashMap<>();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String ReadNewsTitle = sharedPrefs.getString(ESM_READ_HISTORY_CANDIDATE, ZERO_RESULT_STRING);
        String NotiNewTitle = sharedPrefs.getString(ESM_NOTIFICATION_UNCLICKED_CANDIDATE, ZERO_RESULT_STRING);
        final List<String> noti_news_title_array_json = new ArrayList<String>(Arrays.asList(NotiNewTitle.split("#")));
        final List<String> read_news_title_array_json = new ArrayList<String>(Arrays.asList(ReadNewsTitle.split("#")));

        @SuppressLint("HardwareIds")
        String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        if (!esm_id.equals("")){
            final DocumentReference rbRef = db.collection(TEST_USER_COLLECTION).document(device_id).collection(PUSH_ESM_COLLECTION).document(esm_id);
            rbRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        assert document != null;
                        if (document.exists()) {
                            rbRef.update(PUSH_ESM_READ_ARRAY, read_news_title_array_json,
                                    PUSH_ESM_NOTI_ARRAY, noti_news_title_array_json,
                                    PUSH_ESM_NOT_SAMPLE_READ_SHORT, not_sample_short,
                                    PUSH_ESM_NOT_SAMPLE_READ_FAR, not_sample_far,
                                    PUSH_ESM_NOT_SAMPLE_NOTIFICATION_FAR, not_sample_far_noti,
                                    PUSH_ESM_READ_EXIST, exist_read,
                                    PUSH_ESM_NOTIFICATION_EXIST, exist_notification
                                    )//another field
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
                            Toast.makeText(getApplicationContext(), "系統出錯，幫您導回主頁面", Toast.LENGTH_SHORT).show();
                            Intent back = new Intent();
                            back.setClass(getApplicationContext(), NewsHybridActivity.class);
                            startActivity(back);
                        }
                    } else {
                        Log.d("lognewsselect", "get failed with ", task.getException());
                        Toast.makeText(getApplicationContext(), "系統出錯，幫您導回主頁面", Toast.LENGTH_SHORT).show();
                        Intent back = new Intent();
                        back.setClass(getApplicationContext(), NewsHybridActivity.class);
                        startActivity(back);
                    }
                }
            });
        }
        esm_id = "";
//        String TMP = sharedPrefs.getString(ESM_TARGET_NEWS_TITLE, "");
//        Log.d("lognewsselect", "#################current sample history" + TMP);
        startActivity(intent_esm);
    }

    private void select_news_title_candidate() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        @SuppressLint("HardwareIds")
        final String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//        final Long now = System.currentTimeMillis();
        final Timestamp now_time_stamp = Timestamp.now();
//        final Calendar c = Calendar.getInstance();
//        c.setTimeInMillis(now);
        task =   db.collection(TEST_USER_COLLECTION)
                .document(device_id)
                .collection(READING_BEHAVIOR_COLLECTION)
                .whereEqualTo(READING_BEHAVIOR_SAMPLE_CHECK, false)
                .orderBy(READING_BEHAVIOR_OUT_TIME, Query.Direction.DESCENDING)
                .get();
        task2 =  db.collection(TEST_USER_COLLECTION)
                .document(device_id)
                .collection(PUSH_NEWS_COLLECTION)
                .whereEqualTo(PUSH_NEWS_CLICK, 0)
                .orderBy(PUSH_NEWS_NOTI_TIME, Query.Direction.DESCENDING)
                .get();
//        db.collection("test_users")
//                .document(device_id)
//                .collection("reading_behaviors")
//                .whereEqualTo("select", false)
////                .whereGreaterThanOrEqualTo("time_on_page(s)", 5)
//                .orderBy("out_timestamp", Query.Direction.DESCENDING)
//                .get()
        task.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        List<String> news_title_array_add = new ArrayList<>();
                if (!queryDocumentSnapshots.isEmpty()) {
                    news_title_target_array.clear();
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        if(!(d.getString(READING_BEHAVIOR_TITLE) ==null) && !d.getString(READING_BEHAVIOR_TITLE).equals("NA")){
//                            if (d.getDouble(READING_BEHAVIOR_TIME_ON_PAGE)>= ESM_TIME_ON_PAGE_THRESHOLD) {
                                if (Math.abs(now_time_stamp.getSeconds() - d.getTimestamp(READING_BEHAVIOR_IN_TIME).getSeconds()) <= ESM_TARGET_RANGE) {
//                                    news_title_target_array.add(d.getString(READING_BEHAVIOR_TITLE));
                                    //define share arrary trigger by category
                                    exist_read = true;
                                    int share_var = 0, trigger_var = 0;
                                    String cat_var = "種類";
                                    tmp_share_Array = (List<String>) d.get(READING_BEHAVIOR_SHARE);
                                    for (int i=0; i< tmp_share_Array.size(); i++){
                                        if(!tmp_share_Array.get(i).equals(NA_STRING) && !tmp_share_Array.get(i).equals(NONE_STRING)){
                                            share_var = 1;
                                            break;
                                        }
                                    }
                                    if(d.getString(READING_BEHAVIOR_TRIGGER_BY).equals(READING_BEHAVIOR_TRIGGER_BY_NOTIFICATION)){
                                        trigger_var = 1;
                                    }
//                                    tmp_category_Array = (List<String>) d.get(READING_BEHAVIOR_CATEGORY);
                                    tmp_category_Array.add("暫無");
                                    cat_var = tmp_category_Array.get(0);
                                    Timestamp timestamp = d.getTimestamp(READING_BEHAVIOR_IN_TIME);
                                    Date date = timestamp.toDate();
                                    news_title_target_array.add(d.getString(READING_BEHAVIOR_TITLE) + "¢" + share_var + "¢" + trigger_var + "¢" + cat_var + "¢" + date);
                                } else {
                                    not_sample_far.add(d.getString(READING_BEHAVIOR_TITLE));
                                }
//                            } else {
//                                not_sample_short.add(d.getString(READING_BEHAVIOR_TITLE));
//                            }
                        }
                        //mark as check
                        db.collection(TEST_USER_COLLECTION)
                                .document(device_id)
                                .collection(READING_BEHAVIOR_COLLECTION)
                                .document(d.getId())
                                .update(READING_BEHAVIOR_SAMPLE_CHECK, true, READING_BEHAVIOR_SAMPLE_CHECK_ID, esm_id)
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
                if(news_title_target_array.size()!=0){
                    String title_array = "";
                    for(int i = 0; i< news_title_target_array.size(); i++){
                        title_array+= news_title_target_array.get(i) + "#";
                    }
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putString(ESM_READ_HISTORY_CANDIDATE, title_array);
                    editor.apply();
//                    Log.d("lognewsselect", "@@@@@@@@@@@@@");
                }
//                else {
//                    SharedPreferences.Editor editor = sharedPrefs.edit();
//                    editor.putString(ESM_READ_HISTORY_CANDIDATE, ZERO_RESULT_STRING);
//                    editor.apply();
//                }
                Log.d("lognewsselect", "**********COMPLETE TASK1");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("lognewsselect", String.valueOf(e));
            }
        });

        task2.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                notification_unclick_array.clear();
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        if (Math.abs(now_time_stamp.getSeconds() - d.getTimestamp(PUSH_NEWS_NOTI_TIME).getSeconds()) <= NOTIFICATION_TARGET_RANGE) {
                            exist_notification = true;
                            notification_unclick_array.add(d.getString(PUSH_NEWS_TITLE));
                            Log.d("lognewsselect", "task2 " + d.getString(PUSH_NEWS_TITLE));
                        } else {
                            not_sample_far_noti.add(d.getString(PUSH_NEWS_TITLE));
                        }
                        //mark as check
                        db.collection(TEST_USER_COLLECTION)
                                .document(device_id)
                                .collection(PUSH_NEWS_COLLECTION)
                                .document(d.getId())
                                .update(PUSH_NEWS_CLICK, 4, PUSH_NEWS_SAMPLE_CHECK_ID, esm_id)
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
                if(notification_unclick_array.size()!=0){
                    String notification_unclick_string = "";
                    for(int i = 0; i< notification_unclick_array.size(); i++){
                        notification_unclick_string+= notification_unclick_array.get(i) + "#";
                    }
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putString(ESM_NOTIFICATION_UNCLICKED_CANDIDATE, notification_unclick_string);
                    editor.apply();
                }
//                else {
//                    SharedPreferences.Editor editor = sharedPrefs.edit();
//                    editor.putString(ESM_NOTIFICATION_UNCLICKED_CANDIDATE, ZERO_RESULT_STRING);
//                    editor.apply();
//                }
                Log.d("lognewsselect", "**********COMPLETE TASK2");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("lognewsselect", String.valueOf(e));
            }
        });
//        Log.d("lognewsselect", "select news finish################################# ");
    }

    @Override
    protected void onResume() {
        if(esm_id.equals("")){
            Intent back = new Intent();
            back.setClass(this, NewsHybridActivity.class);
            startActivity(back);
        }
        super.onResume();
    }
}