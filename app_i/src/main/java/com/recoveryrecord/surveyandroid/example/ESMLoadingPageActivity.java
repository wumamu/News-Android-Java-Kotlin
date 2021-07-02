package com.recoveryrecord.surveyandroid.example;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.recoveryrecord.surveyandroid.example.DbHelper.ESMDbHelper;
import com.recoveryrecord.surveyandroid.example.DbHelper.PushNewsDbHelper;
import com.recoveryrecord.surveyandroid.example.DbHelper.ReadingBehaviorDbHelper;
import com.recoveryrecord.surveyandroid.example.sqlite.ESM;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_READ_EXIST;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_DEVICE_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_SAMPLE_CHECK_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_DEVICE_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_SAMPLE_CHECK_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.SURVEY_PAGE_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_TARGET_RANGE;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_TIME_ON_PAGE_THRESHOLD;
import static com.recoveryrecord.surveyandroid.example.Constants.LOADING_PAGE_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.LOADING_PAGE_TYPE_KEY;
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
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_IN_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_OUT_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_SAMPLE_CHECK;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_TIME_ON_PAGE;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_TITLE;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_READ_HISTORY_CANDIDATE;
import static com.recoveryrecord.surveyandroid.example.Constants.ZERO_RESULT_STRING;

public class ESMLoadingPageActivity extends AppCompatActivity {
    private Button button;
    Task task, task2, task3;
    Boolean exist_read = false, exist_notification = false;
    String esm_id = "", type ="";
    List<String> rb_query = new ArrayList<>();
    String rb_sample = "NA";
    String noti_sample = "NA";
    List<String> noti_query = new ArrayList<>();
    List<String> not_sample_short = new ArrayList<>();
    List<String> not_sample_far = new ArrayList<>();
    List<String> not_sample_far_noti = new ArrayList<>();

    String device_id = "NA";
    List<String> tmp_category_Array = new ArrayList<>();
    Timestamp my_time;

    private ProgressBar pgsBar;
    private int i = 0;
    private TextView txtView;
    private Handler hdlr = new Handler();
    @SuppressLint("HardwareIds")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_page_esm);
        device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
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
        pgsBar = (ProgressBar) findViewById(R.id.pBar);
        //initial
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(ESM_READ_HISTORY_CANDIDATE, ZERO_RESULT_STRING);
        editor.putString(ESM_NOTIFICATION_UNCLICKED_CANDIDATE, ZERO_RESULT_STRING);
        my_time = Timestamp.now();
        editor.apply();
        //find
        firestore_query(esm_id);
        sql_query_noti();
        sql_query_rb();
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openESM();
            }
        });

        button.setEnabled(false);
        txtView = (TextView) findViewById(R.id.textView);
        i = pgsBar.getProgress();
        new Thread(new Runnable() {
            public void run() {
                while (i < 100) {
                    i += 1;
                    // Update the progress bar and display the current value in text view
                    hdlr.post(new Runnable() {
                        public void run() {
                            pgsBar.setProgress(i);
//                            txtView.setText(i+"/"+pgsBar.getMax());
                            if(i==100){
                                txtView.setText("資料匯入完成");
                                button.setEnabled(true);
                            }

                        }
                    });
                    try {
                        // Sleep for 100 milliseconds to show the progress slowly.
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
//                button.setEnabled(true);

            }
        }).start();
    }

    private void sql_query_rb() {
        rb_query.clear();
        ReadingBehaviorDbHelper dbHandler = new ReadingBehaviorDbHelper(getApplicationContext());
        Cursor cursor = dbHandler.getReadingDataForESM(my_time.getSeconds());

        if (cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String news_id = cursor.getString(cursor.getColumnIndex("news_id"));
                String media = cursor.getString(cursor.getColumnIndex("media"));
                long in_time = cursor.getLong(cursor.getColumnIndex("in_timestamp"));
                Date date = new Date();
                date.setTime(in_time*1000);
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
                if(!news_id.equals("NA") && !title.equals("NA") && !media.equals("NA")){
                    rb_query.add(news_id + "¢" + title+ "¢" + media + "¢" + formatter.format(date) + "#");
                    rb_query.add(news_id);
                    rb_query.add(title);
                    rb_query.add(media);
                    rb_query.add(formatter.format(date));
                    break;
                }
            }
//            rb_sample = news_id + "¢" + title+ "¢" + media + "¢" + formatter.format(date) + "#";
//            while(!cursor.isAfterLast()) {
//                // If you use c.moveToNext() here, you will bypass the first row, which is WRONG
//                String title = cursor.getString(cursor.getColumnIndex("title"));
//                String news_id = cursor.getString(cursor.getColumnIndex("news_id"));
//                String media = cursor.getString(cursor.getColumnIndex("media"));
//                long in_time = cursor.getLong(cursor.getColumnIndex("in_timestamp"));
//                Log.d("lognewsselect", title);
//                if(!title.equals("NA")){
//                    exist_read = true;
//                    int share_var = 0, trigger_var = 0;
//                    String cat_var = "種類";
//                    Date date = new Date();
//                    date.setTime(in_time*1000);
//                    @SuppressLint("SimpleDateFormat")
//                    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//                    rb_query.add(title + "(" + media + ")" + "¢" + share_var + "¢" + trigger_var + "¢" + cat_var + "¢" + formatter.format(date) + "¢" + news_id);
//                }
//                cursor.moveToNext();
//            }
        }
        if (!cursor.isClosed())  {
            cursor.close();
        }

        if(rb_query.size()!=0){
//            String title_array = "";
//            for(int i = 0; i< rb_query.size(); i++){
//                title_array+= rb_query.get(i) + "#";
//            }
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString(ESM_READ_HISTORY_CANDIDATE, rb_query.get(0));
            editor.apply();
        }
    }

    private void sql_query_noti() {
        PushNewsDbHelper dbHandler = new PushNewsDbHelper(getApplicationContext());
        noti_query.clear();
        Cursor cursor = dbHandler.getNotiDataForESM(my_time.getSeconds());
        if (cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String news_id = cursor.getString(cursor.getColumnIndex("news_id"));
                if(!title.equals("NA")){
    //                my_noti_list.add(title + "¢" + news_id + "#");
                    noti_query.add(title);
                    exist_notification = true;
                }
                cursor.moveToNext();
            }
        }
        if (!cursor.isClosed())  {
            cursor.close();
        }

        if(noti_query.size()!=0){
            String notification_unclick_string = "";
            for(int i = 0; i< noti_query.size(); i++){
                notification_unclick_string+= noti_query.get(i) + "#";
            }
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString(ESM_NOTIFICATION_UNCLICKED_CANDIDATE, notification_unclick_string);
            editor.apply();
        }
//        Log.d("lognewsselect", my_noti_list.toString());
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void openESM() {

        Intent intent_esm = new Intent();
        intent_esm.setClass(this, SurveyActivity.class);
        intent_esm.putExtra(SURVEY_PAGE_ID, esm_id);//******************
        intent_esm.putExtra(NOTIFICATION_TYPE_KEY, NOTIFICATION_TYPE_VALUE_ESM);
        intent_esm.putExtra(ESM_EXIST_READ_SAMPLE, exist_read);
        intent_esm.putExtra(ESM_EXIST_NOTIFICATION_SAMPLE, exist_notification);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        ESM myesm = new ESM();
        myesm.setKEY_DOC_ID(device_id + " " + esm_id);
        myesm.setKEY_ESM_TYPE(0);
//        myesm.setKEY_NOTI_SAMPLE(String.valueOf(noti_query));
        myesm.setKEY_NOTI_SAMPLE(String.join("#", noti_query));
//        myesm.setKEY_SELF_READ_SAMPLE(String.valueOf(rb_query));
        myesm.setKEY_SELF_READ_SAMPLE(String.join("#", noti_query));
        myesm.setKEY_ESM_SAMPLE_TIME(my_time.getSeconds());
        ESMDbHelper dbHandler_esm = new ESMDbHelper(getApplicationContext());
        dbHandler_esm.UpdatePushESMDetailsClick(myesm);

        if (!esm_id.equals("")){
//            final DocumentReference rbRef = db.collection(TEST_USER_COLLECTION).document(device_id).collection(PUSH_ESM_COLLECTION).document(esm_id);
            final DocumentReference rbRef = db.collection(PUSH_ESM_COLLECTION).document(device_id + " " + esm_id);
            rbRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        assert document != null;
                        if (document.exists()) {
                            rbRef.update(PUSH_ESM_READ_ARRAY, rb_query,
                                    PUSH_ESM_NOTI_ARRAY, noti_query,
//                                    PUSH_ESM_NOT_SAMPLE_READ_SHORT, not_sample_short,
//                                    PUSH_ESM_NOT_SAMPLE_READ_FAR, not_sample_far,
//                                    PUSH_ESM_NOT_SAMPLE_NOTIFICATION_FAR, not_sample_far_noti,
                                    PUSH_ESM_READ_EXIST, exist_read,
                                    PUSH_ESM_NOTIFICATION_EXIST, exist_notification,
                                    "sample_time", my_time.getSeconds()
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
                            Log.d("lognewsselect", "ESMLoadingPageActivity No such document");
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

    private void firestore_query(final String esm_id_in) {
        final String tmp_esm_id = esm_id_in;
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        @SuppressLint("HardwareIds")
        final String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//        final Long now = System.currentTimeMillis();
        final Timestamp now_time_stamp = Timestamp.now();

        task =   db.collection(READING_BEHAVIOR_COLLECTION)
                .whereEqualTo(READING_BEHAVIOR_DEVICE_ID, device_id)
                .whereEqualTo(READING_BEHAVIOR_SAMPLE_CHECK, false)
                .orderBy(READING_BEHAVIOR_OUT_TIME, Query.Direction.DESCENDING)
                .get();
        task2 =  db.collection(PUSH_NEWS_COLLECTION)
                .whereEqualTo(PUSH_NEWS_DEVICE_ID, device_id)
                .whereEqualTo(PUSH_NEWS_CLICK, 0)
                .orderBy(PUSH_NEWS_NOTI_TIME, Query.Direction.DESCENDING)
                .get();

        task.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        List<String> news_title_array_add = new ArrayList<>();
                if (!queryDocumentSnapshots.isEmpty()) {
//                    news_title_target_array.clear();
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        if(!(d.getString(READING_BEHAVIOR_TITLE) ==null) && !d.getString(READING_BEHAVIOR_TITLE).equals("NA")){
                            if (d.getDouble(READING_BEHAVIOR_TIME_ON_PAGE)>= ESM_TIME_ON_PAGE_THRESHOLD) {
                                if (Math.abs(now_time_stamp.getSeconds() - d.getTimestamp(READING_BEHAVIOR_IN_TIME).getSeconds()) <= ESM_TARGET_RANGE) {
//                                    news_title_target_array.add(d.getString(READING_BEHAVIOR_TITLE));
                                    //define share arrary trigger by category
//                                    exist_read = true;
//                                    int share_var = 0, trigger_var = 0;
//                                    String cat_var = "種類";
//                                    tmp_share_Array = (List<String>) d.get(READING_BEHAVIOR_SHARE);
//                                    for (int i=0; i< tmp_share_Array.size(); i++){
//                                        if(!tmp_share_Array.get(i).equals(NA_STRING) && !tmp_share_Array.get(i).equals(NONE_STRING)){
//                                            share_var = 1;
//                                            break;
//                                        }
//                                    }
//                                    if(d.getString(READING_BEHAVIOR_TRIGGER_BY).equals(READING_BEHAVIOR_TRIGGER_BY_NOTIFICATION)){
//                                        trigger_var = 1;
//                                    }
////                                    tmp_category_Array = (List<String>) d.get(READING_BEHAVIOR_CATEGORY);
//                                    tmp_category_Array.add("暫無");
//                                    cat_var = tmp_category_Array.get(0);
//                                    Timestamp timestamp = d.getTimestamp(READING_BEHAVIOR_IN_TIME);
//                                    Date date = timestamp.toDate();
//                                    @SuppressLint("SimpleDateFormat")
//                                    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//                                    news_title_target_array.add(d.getString(READING_BEHAVIOR_TITLE) + "¢" + share_var + "¢" + trigger_var + "¢" + cat_var + "¢" + formatter.format(date) + "¢" + d.getString(READING_BEHAVIOR_NEWS_ID));
                                } else {
                                    not_sample_far.add(d.getString(READING_BEHAVIOR_TITLE));
                                }
                            } else {
                                not_sample_short.add(d.getString(READING_BEHAVIOR_TITLE));
                            }
                        }
//                        if(tmp_esm_id.equals("")){
//                            tmp_esm_id = "NA";
//                        }
                        //mark as check
                        db.collection(READING_BEHAVIOR_COLLECTION)
                                .document(d.getId())
                                .update(READING_BEHAVIOR_SAMPLE_CHECK, true,
                                        READING_BEHAVIOR_SAMPLE_CHECK_ID, tmp_esm_id)
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
//                if(news_title_target_array.size()!=0){
//                    String title_array = "";
//                    for(int i = 0; i< news_title_target_array.size(); i++){
//                        title_array+= news_title_target_array.get(i) + "#";
//                    }
//                    SharedPreferences.Editor editor = sharedPrefs.edit();
//                    editor.putString(ESM_READ_HISTORY_CANDIDATE, title_array);
//                    editor.apply();
////                    Log.d("lognewsselect", "@@@@@@@@@@@@@");
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
//                notification_unclick_array.clear();
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        if (Math.abs(now_time_stamp.getSeconds() - d.getTimestamp(PUSH_NEWS_NOTI_TIME).getSeconds()) <= NOTIFICATION_TARGET_RANGE) {
//                            exist_notification = true;
//                            notification_unclick_array.add(d.getString(PUSH_NEWS_TITLE));
//                            Log.d("lognewsselect", "task2 " + d.getString(PUSH_NEWS_TITLE));
                        } else {
                            not_sample_far_noti.add(d.getString(PUSH_NEWS_TITLE));
                        }
                        //mark as check
//                        db.collection(TEST_USER_COLLECTION)
                        db.collection(PUSH_NEWS_COLLECTION)
                                .document(d.getId())
                                .update(PUSH_NEWS_CLICK, 4, PUSH_NEWS_SAMPLE_CHECK_ID, tmp_esm_id)
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
//                if(notification_unclick_array.size()!=0){
//                    String notification_unclick_string = "";
//                    for(int i = 0; i< notification_unclick_array.size(); i++){
//                        notification_unclick_string+= notification_unclick_array.get(i) + "#";
//                    }
//                    SharedPreferences.Editor editor = sharedPrefs.edit();
//                    editor.putString(ESM_NOTIFICATION_UNCLICKED_CANDIDATE, notification_unclick_string);
//                    editor.apply();
//                }
//                Log.d("lognewsselect", "**********COMPLETE TASK2");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("lognewsselect", String.valueOf(e));
            }
        });
        Log.d("lognewsselect", "select news finish################################# ");
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