//package com.recoveryrecord.surveyandroid.example;
//
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.database.Cursor;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.provider.Settings;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.firebase.Timestamp;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.recoveryrecord.surveyandroid.example.DbHelper.DiaryDbHelper;
//import com.recoveryrecord.surveyandroid.example.DbHelper.ESMDbHelper;
//import com.recoveryrecord.surveyandroid.example.sqlite.Diary;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.preference.PreferenceManager;
//
//import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_NOTI_HISTORY_CANDIDATE;
//import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_READ_HISTORY_CANDIDATE;
//import static com.recoveryrecord.surveyandroid.example.Constants.EXIST_ESM;
//import static com.recoveryrecord.surveyandroid.example.Constants.LOADING_PAGE_ID;
//import static com.recoveryrecord.surveyandroid.example.Constants.LOADING_PAGE_TYPE_KEY;
//import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_TYPE_KEY;
//import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_TYPE_VALUE_DIARY;
//import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_DIARY_COLLECTION;
//import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_DIARY_OPTION_NOTI;
//import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_DIARY_OPTION_READ;
//import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_DIARY_SAMPLE_TIME;
//import static com.recoveryrecord.surveyandroid.example.Constants.SURVEY_PAGE_ID;
//import static com.recoveryrecord.surveyandroid.example.Constants.ZERO_RESULT_STRING;
//
////import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_DIARY_OPTION;
////import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_TARGET_TITLE_DIARY;
//
//
//
//public class DiaryLoadingPageActivity extends AppCompatActivity {
//    private Button button;
////    Task task, task2, task3;
//    String diary_id = "", type ="";
//    Boolean exist_esm_sample = false;
//    List<String> esm_query_read = new ArrayList<>();
//    List<String> esm_query_noti = new ArrayList<>();
//    private ProgressBar pgsBar;
//    private int i = 0;
//    private TextView txtView;
//    private final Handler hdlr = new Handler();
//
//    Timestamp my_time;
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_loading_page_diary);
//        if (getIntent().getExtras() != null) {
//            Bundle b = getIntent().getExtras();
//            diary_id = Objects.requireNonNull(b.getString(LOADING_PAGE_ID));
//            type = Objects.requireNonNull(b.getString(LOADING_PAGE_TYPE_KEY));
//        }
//        if(diary_id.equals("")){
//            Toast.makeText(this, "系統出錯，幫您導回主頁面", Toast.LENGTH_SHORT).show();
//            Intent back = new Intent();
//            back.setClass(getApplicationContext(), NewsHybridActivity.class);
//            startActivity(back);
//        }
//        pgsBar = findViewById(R.id.pBar);
//        //initial
//        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor editor = sharedPrefs.edit();
//        editor.putString(DIARY_READ_HISTORY_CANDIDATE, ZERO_RESULT_STRING);
//        editor.putString(DIARY_NOTI_HISTORY_CANDIDATE, ZERO_RESULT_STRING);
//        editor.putBoolean(EXIST_ESM, false);
//        editor.apply();
//
//        my_time = Timestamp.now();
//        sql_query_esm();
//        button = findViewById(R.id.button);
//        button.setOnClickListener(v -> openDiary());
//
//        button.setEnabled(false);
//        txtView = findViewById(R.id.textView);
//        i = pgsBar.getProgress();
//        new Thread(() -> {
//            while (i < 100) {
//                i += 1;
//                // Update the progress bar and display the current value in text view
//                hdlr.post(() -> {
//                    pgsBar.setProgress(i);
////                            txtView.setText(i+"/"+pgsBar.getMax());
//                    if(i==100){
//                        txtView.setText("資料匯入完成");
//                        button.setEnabled(true);
//                    }
//
//                });
//                try {
//                    // Sleep for 100 milliseconds to show the progress slowly.
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
////                button.setEnabled(true);
//
//        }).start();
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void sql_query_esm() {
//        ESMDbHelper dbHandler = new ESMDbHelper(getApplicationContext());
//        esm_query_read.clear();
//        esm_query_noti.clear();
//        Cursor cursor_noti = dbHandler.getNotiDataForDiaryNoti(my_time.getSeconds());
//        if (cursor_noti.moveToFirst()) {
//            while(!cursor_noti.isAfterLast()) {
//                String noti_read_news_id = cursor_noti.getString(cursor_noti.getColumnIndex("noti_read_news_id"));
//                String noti_read_title = cursor_noti.getString(cursor_noti.getColumnIndex("noti_read_title"));
//                String noti_read_receieve_time = cursor_noti.getString(cursor_noti.getColumnIndex("noti_read_receieve_time"));
//                String noti_read_situation = cursor_noti.getString(cursor_noti.getColumnIndex("noti_read_situation"));
//                String noti_read_place = cursor_noti.getString(cursor_noti.getColumnIndex("noti_read_place"));
//                int esm_type = cursor_noti.getInt(cursor_noti.getColumnIndex("esm_type"));
//                noti_read_title = noti_read_title.replace("\n", "");
//                noti_read_situation = noti_read_situation.replace("\n", "");
//                noti_read_place = noti_read_place.replace("\n", "");
//                if(esm_type==0 || esm_type==2){
//                    if(!noti_read_situation.equals("NA") && !noti_read_place.equals("NA")){
//                        esm_query_noti.add(noti_read_title + "\n收到時間: " + noti_read_receieve_time + "\n當下活動: " + noti_read_situation + "\n地點: " + noti_read_place + "\n" + noti_read_news_id);
//                        exist_esm_sample = true;
//                        Log.d("AlarmReceiver", "noti" + noti_read_news_id);
//                    }
//                }
//                cursor_noti.moveToNext();
//            }
//        }
//        if (!cursor_noti.isClosed())  {
//            cursor_noti.close();
//        }
//        Cursor cursor_read = dbHandler.getNotiDataForDiaryRead(my_time.getSeconds());
//        if (cursor_read.moveToFirst()) {
//            while(!cursor_read.isAfterLast()) {
//                String noti_read_news_id = cursor_read.getString(cursor_read.getColumnIndex("noti_read_news_id"));
//                String noti_read_title = cursor_read.getString(cursor_read.getColumnIndex("noti_read_title"));
//                String noti_read_in_time = cursor_read.getString(cursor_read.getColumnIndex("noti_read_in_time"));
//                String noti_read_situation = cursor_read.getString(cursor_read.getColumnIndex("self_read_situation"));
//                String noti_read_place = cursor_read.getString(cursor_read.getColumnIndex("self_read_place"));
//                int esm_type = cursor_read.getInt(cursor_read.getColumnIndex("esm_type"));
//                noti_read_title = noti_read_title.replace("\n", "");
//                noti_read_situation = noti_read_situation.replace("\n", "");
//                noti_read_place = noti_read_place.replace("\n", "");
//                if(esm_type==0 || esm_type==1){
//                    if(!noti_read_situation.equals("NA") && !noti_read_place.equals("NA")){
//                        esm_query_read.add(noti_read_title + "\n進入時間: " + noti_read_in_time + "\n當下活動: " + noti_read_situation + "\n地點: " + noti_read_place + "\n" + noti_read_news_id);
//                        exist_esm_sample = true;
//                        Log.d("AlarmReceiver", "read" + noti_read_news_id);
//                    }
//                }
//                cursor_read.moveToNext();
//            }
//        }
//        if (!cursor_read.isClosed())  {
//            cursor_read.close();
//        }
//        if(exist_esm_sample){
//            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//            SharedPreferences.Editor editor = sharedPrefs.edit();
//
//            editor.putString(DIARY_READ_HISTORY_CANDIDATE, String.join("#", esm_query_read));
//            editor.putString(DIARY_NOTI_HISTORY_CANDIDATE, String.join("#", esm_query_noti));
//            editor.putBoolean(EXIST_ESM, true);
//            editor.apply();
//        }
//        Log.d("AlarmReceiver", String.join("#", esm_query_read));
//        Log.d("AlarmReceiver", String.join("#", esm_query_noti));
//    }
//
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public void openDiary() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        @SuppressLint("HardwareIds")
//        String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//
//        Intent intent_diary = new Intent();
//        intent_diary.setClass(this, SurveyActivity.class);
//        intent_diary.putExtra(SURVEY_PAGE_ID, diary_id);//******************
//        intent_diary.putExtra(NOTIFICATION_TYPE_KEY, NOTIFICATION_TYPE_VALUE_DIARY);
//
//        Diary mydiary = new Diary();
//        mydiary.setKEY_DOC_ID(device_id + " " + diary_id);
//        mydiary.setKEY_ESM_RESULT_SAMPLE_READ(String.join("#", esm_query_read));
//        mydiary.setKEY_ESM_RESULT_SAMPLE_NOTI(String.join("#", esm_query_noti));
//        mydiary.setKEY_DIARY_SAMPLE_TIME(my_time.getSeconds());
//        DiaryDbHelper dbHandler_d = new DiaryDbHelper(getApplicationContext());
//        dbHandler_d.UpdatePushDiaryDetailsClick(mydiary);
//
//
//        if (!diary_id.equals("")){
//            final DocumentReference rbRef = db.collection(PUSH_DIARY_COLLECTION).document(device_id + " " + diary_id);
//            rbRef.get().addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    assert document != null;
//                    if (document.exists()) {
//                        rbRef.update(
//                                PUSH_DIARY_OPTION_READ, esm_query_read,
//                                PUSH_DIARY_OPTION_NOTI, esm_query_noti,
//                                PUSH_DIARY_SAMPLE_TIME, my_time.getSeconds())//another field
//                                .addOnSuccessListener(aVoid -> Log.d("lognewsselect", "DocumentSnapshot successfully updated!"))
//                                .addOnFailureListener(e -> Log.w("lognewsselect", "Error updating document", e));
//                    } else {
//                        Log.d("lognewsselect", "DiaryLoadingPageActivity No such document");
//                    }
//                } else {
//                    Log.d("lognewsselect", "get failed with ", task.getException());
//                }
//            });
//        }
//        diary_id = "";
//        startActivity(intent_diary);
//    }
//
////    private void select_news_title_candidate() {
////        final FirebaseFirestore db = FirebaseFirestore.getInstance();
////        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
////        final Timestamp now_time_stamp = Timestamp.now();
////        @SuppressLint("HardwareIds") final String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
////        task3 =  db.collection(PUSH_ESM_COLLECTION)
////                .whereEqualTo(PUSH_ESM_DEVICE_ID, device_id)
////                .whereEqualTo(PUSH_ESM_SAMPLE, 2)
////                .get();
////        task3.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
////            @Override
////            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
////                if (!queryDocumentSnapshots.isEmpty()) {
////                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
////                    for (DocumentSnapshot d : list) {
////                        if(Math.abs(now_time_stamp.getSeconds() - d.getTimestamp(PUSH_ESM_NOTI_TIME).getSeconds()) <= DIARY_TARGET_RANGE){
////                            if (d.getBoolean(PUSH_ESM_READ_EXIST)!=null && d.getBoolean(PUSH_ESM_READ_EXIST)){
//////                            if (d.get(PUSH_ESM_TARGET_TITLE)!=null && !d.get(PUSH_ESM_TARGET_TITLE).equals("NA")){
//////                                String news_title = "NA";
//////                                String news_time = "NA";
//////                                String news_situation = "NA";
//////                                String news_place = "NA";
//////                                String news_id = "NA";
//////                                news_title = d.getString(PUSH_ESM_TARGET_TITLE);
//////                                news_time = d.getString(PUSH_ESM_TARGET_IN_TIME);
//////                                news_situation = d.getString(PUSH_ESM_TARGET_SITUATION);
//////                                news_place = d.getString(PUSH_ESM_TARGET_PLACE);
//////                                news_id = d.getString(PUSH_ESM_TARGET_NEWS_ID);
//////                                diary_option_array.add(news_title + "\n" + news_time + "\n" + news_situation + "\n" + news_place + "\n" + news_id);
//////                                exist_esm_sample = true;
////                            }
////                        }
////                        //mark as check
//////                        db.collection(TEST_USER_COLLECTION)
//////                                .document(device_id)
//////                                .collection(PUSH_ESM_COLLECTION)
//////                                .document(d.getId())
//////                                .update(PUSH_ESM_SAMPLE, 3, PUSH_ESM_SAMPLE_ID, diary_id)
//////                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//////                                    @Override
//////                                    public void onSuccess(Void aVoid) {
//////                                        Log.d("lognewsselect", "DocumentSnapshot successfully updated!");
//////                                    }
//////                                })
//////                                .addOnFailureListener(new OnFailureListener() {
//////                                    @Override
//////                                    public void onFailure(@NonNull Exception e) {
//////                                        Log.w("lognewsselect", "Error updating document", e);
//////                                    }
//////                                });
////                        db.collection(PUSH_ESM_COLLECTION)
////                                .document(d.getId())
////                                .update(PUSH_ESM_SAMPLE, 3, PUSH_ESM_SAMPLE_ID, diary_id)
////                                .addOnSuccessListener(new OnSuccessListener<Void>() {
////                                    @Override
////                                    public void onSuccess(Void aVoid) {
////                                        Log.d("lognewsselect", "DocumentSnapshot successfully updated!");
////                                    }
////                                })
////                                .addOnFailureListener(new OnFailureListener() {
////                                    @Override
////                                    public void onFailure(@NonNull Exception e) {
////                                        Log.w("lognewsselect", "Error updating document", e);
////                                    }
////                                });
////
////                    }
////
////                }
//////                if(esm_query.size()!=0){
//////                    String title_array = "";
//////                    for(int i = 0; i< esm_query.size(); i++){
//////                        title_array+= esm_query.get(i) + "#";
//////                    }
//////                    SharedPreferences.Editor editor = sharedPrefs.edit();
//////                    editor.putString(DIARY_READ_HISTORY_CANDIDATE, title_array);
//////                    editor.apply();
//////                    Log.d("lognewsselect", "@@@@@@@@@@@@@");
//////                }
//////                else {
//////                    SharedPreferences.Editor editor = sharedPrefs.edit();
//////                    editor.putString(DIARY_READ_HISTORY_CANDIDATE, ZERO_RESULT_STRING);
//////                    editor.apply();
//////                }
////                Log.d("lognewsselect", "**********COMPLETE TASK3");
////            }
////        }).addOnFailureListener(new OnFailureListener() {
////            @Override
////            public void onFailure(@NonNull Exception e) {
////                Log.d("lognewsselect", String.valueOf(e));
////                // if we do not get any data or any error we are displaying
////                // a toast message that we do not get any data
//////                Toast.makeText(TestNewsOneActivity.this, "Fail to get the data.", Toast.LENGTH_SHORT).show();
////            }
////        });
////
////    }
//
//
//    @Override
//    protected void onResume() {
//        if(diary_id.equals("")){
//            Intent back = new Intent();
//            back.setClass(this, NewsHybridActivity.class);
//            startActivity(back);
//        }
//        super.onResume();
//    }
//
//    @Override
//    public void onBackPressed() {
//        new AlertDialog.Builder(this)
//                .setTitle(com.recoveryrecord.surveyandroid.R.string.close_survey)
//                .setMessage(com.recoveryrecord.surveyandroid.R.string.are_you_sure_you_want_to_close)
//                .setNeutralButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss()).setPositiveButton(android.R.string.ok, (dialog, which) -> DiaryLoadingPageActivity.super.onBackPressed()).show();
//    }
//}