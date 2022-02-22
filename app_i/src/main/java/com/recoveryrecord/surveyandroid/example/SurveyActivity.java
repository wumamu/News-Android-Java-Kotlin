package com.recoveryrecord.surveyandroid.example;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.recoveryrecord.surveyandroid.Answer;
import com.recoveryrecord.surveyandroid.R;
import com.recoveryrecord.surveyandroid.condition.CustomConditionHandler;
import com.recoveryrecord.surveyandroid.example.DbHelper.DiaryDbHelper;
import com.recoveryrecord.surveyandroid.example.DbHelper.ESMDbHelper;
import com.recoveryrecord.surveyandroid.example.sqlite.Diary;
import com.recoveryrecord.surveyandroid.example.sqlite.ESM;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceManager;

import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_EXIST_ESM_SAMPLE;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_NOTI_HISTORY_CANDIDATE;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_READ_HISTORY_CANDIDATE;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_EXIST_NOTIFICATION_SAMPLE;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_EXIST_READ_SAMPLE;
import static com.recoveryrecord.surveyandroid.example.Constants.LOADING_PAGE_TYPE_DIARY;
import static com.recoveryrecord.surveyandroid.example.Constants.LOADING_PAGE_TYPE_ESM;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_ESM_TYPE_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_TYPE_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_TYPE_VALUE_DIARY;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_TYPE_VALUE_ESM;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_DIARY_CLOSE_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_DIARY_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_DIARY_OPEN_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_CLOSE_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_OPEN_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.SAMPLE_IN;
import static com.recoveryrecord.surveyandroid.example.Constants.SAMPLE_MEDIA;
import static com.recoveryrecord.surveyandroid.example.Constants.SAMPLE_RECEIEVE;
import static com.recoveryrecord.surveyandroid.example.Constants.SAMPLE_TITLE;
import static com.recoveryrecord.surveyandroid.example.Constants.SHARE_PREFERENCE_SILENT_ESM;
import static com.recoveryrecord.surveyandroid.example.Constants.SURVEY_PAGE_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.ZERO_RESULT_STRING;

//import static com.recoveryrecord.surveyandroid.example.Constants.ESM_ID;
//import static com.recoveryrecord.surveyandroid.example.Constants.JSON_TEMPLATE;

public class SurveyActivity extends com.recoveryrecord.surveyandroid.SurveyActivity implements CustomConditionHandler, UserListCallback {
    String esm_id = "", diary_id = "", type = "";
    boolean silentEsm = false;
    Boolean is_esm = false, is_diary = false;
    Boolean exist_read = false, exist_notification = false;
    Boolean exist_esm_sample = false;
//    List<String> news_title_target_array = new ArrayList<>();
//    List<String> notification_unclick_array = new ArrayList<>();
//    List<String> final_news_title_array = new ArrayList<>();
//    List<NewsCompareObj> HistoryNewsTitleObjListArray = new ArrayList<>();
//    List<NewsCompareObj> MyNewsTitleObjListArray = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Timestamp current_now = Timestamp.now();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        @SuppressLint("HardwareIds")
        String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);



        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        silentEsm = sharedPrefs.getBoolean(SHARE_PREFERENCE_SILENT_ESM, false);
        if(silentEsm){
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);
//            Toast.makeText(getApplicationContext(),"silentEsm",Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(getApplicationContext(),"no silentEsm",Toast.LENGTH_SHORT).show();
        }


        if (getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();
            type = Objects.requireNonNull(b.getString(NOTIFICATION_TYPE_KEY));
            if (type.equals(LOADING_PAGE_TYPE_ESM)){
                esm_id = Objects.requireNonNull(b.getString(SURVEY_PAGE_ID));
                if(!esm_id.equals("")){
                    is_esm = true;
                    exist_read = Objects.requireNonNull(b.getBoolean(ESM_EXIST_READ_SAMPLE));
                    exist_notification = Objects.requireNonNull(b.getBoolean(ESM_EXIST_NOTIFICATION_SAMPLE));
                }
            } else if (type.equals(LOADING_PAGE_TYPE_DIARY)){
                diary_id = Objects.requireNonNull(b.getString(SURVEY_PAGE_ID));
                if(!diary_id.equals("")){
                    is_diary = true;
                    exist_esm_sample = Objects.requireNonNull(b.getBoolean(DIARY_EXIST_ESM_SAMPLE));
                }
            }
        }
        if (is_esm){
            ESM myesm = new ESM();
            myesm.setKEY_DOC_ID(device_id + " " + esm_id);
            myesm.setKEY_OPEN_TIMESTAMP(current_now.getSeconds());
            ESMDbHelper dbHandler = new ESMDbHelper(getApplicationContext());
            dbHandler.UpdatePushESMDetailsOpen(myesm);

            final DocumentReference rbRef = db.collection(PUSH_ESM_COLLECTION).document(device_id + " " + esm_id);
            rbRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        rbRef.update(PUSH_ESM_OPEN_TIME, current_now)//another field
                                .addOnSuccessListener(aVoid -> Log.d("lognewsselect", "DocumentSnapshot successfully updated!"))
                                .addOnFailureListener(e -> Log.w("lognewsselect", "Error updating document", e));
                    } else {
                        Log.d("lognewsselect", "No such document");
                    }
                } else {
                    Log.d("lognewsselect", "get failed with ", task.getException());
                }
            });
        } else if(is_diary){
            Diary mydiary = new Diary();
            Log.d("lognewsselect", "open diary" + current_now.toString());
            mydiary.setKEY_DOC_ID(device_id + " " + diary_id);
            mydiary.setKEY_OPEN_TIMESTAMP(current_now.getSeconds());
            DiaryDbHelper dbHandler = new DiaryDbHelper(getApplicationContext());
            dbHandler.UpdatePushDiaryDetailsOpen(mydiary);

            final DocumentReference rbRef = db.collection(PUSH_DIARY_COLLECTION).document(device_id + " " + diary_id);
            rbRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        rbRef.update(PUSH_DIARY_OPEN_TIME, current_now)//another field
                                .addOnSuccessListener(aVoid -> Log.d("lognewsselect", "DocumentSnapshot successfully updated!"))
                                .addOnFailureListener(e -> Log.w("lognewsselect", "Error updating document", e));
                    } else {
                        Log.d("lognewsselect", "No such document");
                    }
                } else {
                    Log.d("lognewsselect", "get failed with ", task.getException());
                }
            });
        }

    }
    @Override
    protected String getSurveyId() {
        return "123123";
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String getSurveyTitle() {
        if (getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();
            type = Objects.requireNonNull(b.getString(NOTIFICATION_TYPE_KEY));
            if (type.equals(LOADING_PAGE_TYPE_ESM)){
                esm_id = Objects.requireNonNull(b.getString(SURVEY_PAGE_ID));
                if(!esm_id.equals("")){
                    is_esm = true;
                    return "ESM";
                }
            } else if (type.equals(LOADING_PAGE_TYPE_DIARY)){
                diary_id = Objects.requireNonNull(b.getString(SURVEY_PAGE_ID));
                if(!diary_id.equals("")){
                    is_diary = true;
                    return "DIARY";
                }
            }
        }
        return "ERROR";
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String getJsonFilename() {
        String file_name = "test.json";
        if (getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();
            if (Objects.requireNonNull(b.getString(NOTIFICATION_TYPE_KEY)).equals(NOTIFICATION_TYPE_VALUE_ESM)){//
                if(Objects.requireNonNull(b.getInt(NOTIFICATION_ESM_TYPE_KEY))==1){
                    //read esm type 1
                    try {
                        file_name = generate_json_read();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    //noti esm type 0 2
                    try {
                        file_name = generate_json_noti();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return file_name;
            } else if (Objects.requireNonNull(b.getString(NOTIFICATION_TYPE_KEY)).equals(NOTIFICATION_TYPE_VALUE_DIARY)){
                final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
                String diary_read = sharedPrefs.getString(DIARY_READ_HISTORY_CANDIDATE, ZERO_RESULT_STRING);
                String diary_noti = sharedPrefs.getString(DIARY_NOTI_HISTORY_CANDIDATE, ZERO_RESULT_STRING);
                if(!diary_read.equals(ZERO_RESULT_STRING)){
                    Log.d("lognewsselect", "exist rb");
                    try {
                        file_name = generate_diary_json(diary_read, diary_noti, true);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("lognewsselect", "zero_result here");
                    try {
                        file_name = generate_diary_json(diary_read, diary_noti, false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return file_name;
            }
        }
        return "diary.json";
    }


    @Override
    protected CustomConditionHandler getCustomConditionHandler() {
        return this;
    }

    @Override
    public boolean isConditionMet(Map<String, Answer> answers, Map<String, String> extra) {
        String id = extra.get("id");
        if (id != null && id.equals("check_age")) {
            if (answers.get("birthyear") == null || answers.get("age") == null || extra.get("wiggle_room") == null) {
                return false;
            }
            String birthYearStr = answers.get("birthyear").getValue();
            Integer birthYear = Integer.valueOf(birthYearStr);
            String ageStr = answers.get("age").getValue();
            Integer age = Integer.valueOf(ageStr);
            int wiggleRoom = Integer.parseInt(extra.get("wiggle_room"));
            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            return Math.abs(birthYear + age - currentYear) > wiggleRoom;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (!getOnSurveyStateChangedListener().scrollBackOneQuestion()) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.close_survey)
                    .setMessage(R.string.are_you_sure_you_want_to_close)
                    .setNeutralButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss()).setPositiveButton(android.R.string.ok, (dialog, which) -> SurveyActivity.super.onBackPressed()).show();
        }
    }
//    @Override
//    protected void onResume() {
////        Log.d("survey", "onPause");
//        super.onResume();
//        new AlertDialog.Builder(this)
//                .setTitle(R.string.close_survey)
//                .setMessage(R.string.are_you_sure_you_want_to_close)
//                .setNeutralButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                }).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                SurveyActivity.super.finish();
//            }
//        }).show();
//    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final Timestamp current_end = Timestamp.now();
        @SuppressLint("HardwareIds")
        String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        if(silentEsm){
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert mNotificationManager != null;
            mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
//            Toast.makeText(getApplicationContext(),"silentEsm",Toast.LENGTH_SHORT).show();
        }  //            Toast.makeText(getApplicationContext(),"no silentEsm",Toast.LENGTH_SHORT).show();


        if (is_esm){
            ESM myesm = new ESM();
            myesm.setKEY_DOC_ID(device_id + " " + esm_id);
            myesm.setKEY_CLOSE_TIMESTAMP(current_end.getSeconds());
            ESMDbHelper dbHandler = new ESMDbHelper(getApplicationContext());
            dbHandler.UpdatePushESMDetailsClose(myesm);

            final DocumentReference rbRef = db.collection(PUSH_ESM_COLLECTION).document(device_id + " " + esm_id);
            rbRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        rbRef.update(PUSH_ESM_CLOSE_TIME, current_end)//another field
                                .addOnSuccessListener(aVoid -> Log.d("lognewsselect", "DocumentSnapshot successfully updated!"))
                                .addOnFailureListener(e -> Log.w("lognewsselect", "Error updating document", e));
                    } else {
                        Log.d("lognewsselect", "No such document");
                    }
                } else {
                    Log.d("lognewsselect", "get failed with ", task.getException());
                }
            });
        } else if(is_diary){
            Diary mydiary = new Diary();
            Log.d("lognewsselect", "close diary" + current_end.toString());
            mydiary.setKEY_DOC_ID(device_id + " " + diary_id);
            mydiary.setKEY_CLOSE_TIMESTAMP(current_end.getSeconds());
            DiaryDbHelper dbHandler = new DiaryDbHelper(getApplicationContext());
            dbHandler.UpdatePushDiaryDetailsClose(mydiary);
            final DocumentReference rbRef = db.collection(PUSH_DIARY_COLLECTION).document(device_id + " " + diary_id);
            rbRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        rbRef.update(PUSH_DIARY_CLOSE_TIME, current_end)//another field
                                .addOnSuccessListener(aVoid -> Log.d("lognewsselect", "DocumentSnapshot successfully updated!"))
                                .addOnFailureListener(e -> Log.w("lognewsselect", "Error updating document", e));
                    } else {
                        Log.d("lognewsselect", "No such document");
                    }
                } else {
                    Log.d("lognewsselect", "get failed with ", task.getException());
                }
            });
        }

    }

    private String generate_json_read() throws JSONException {
        JSONObject jsonRootObject = new JSONObject(loadJSONFromAsset("read.json"));
        JSONArray jsonQuestionObject = jsonRootObject.optJSONArray("questions");
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//        String my_news_id = sharedPrefs.getString(SAMPLE_ID, "NA");
        String my_title = sharedPrefs.getString(SAMPLE_TITLE, "NA");
        String my_media = sharedPrefs.getString(SAMPLE_MEDIA, "NA");
//        String my_re = sharedPrefs.getString(SAMPLE_RECEIEVE, "NA");
        String my_in = sharedPrefs.getString(SAMPLE_IN, "NA");
        for (int i = 0; i < Objects.requireNonNull(jsonQuestionObject).length(); i++){
            JSONObject one = jsonQuestionObject.getJSONObject(i);
            if(one.optString("id").equals("active_base_1")){
                one.putOpt("question", "請問您有印象在NewsMoment上閱讀「" + my_title + "」嗎？" + my_in);
                continue;
            }
            if(one.optString("id").equals("active_read_moment_1")){
                one.putOpt("question", "根據資料顯示，您在" + my_in + "閱讀該篇新聞");
                continue;
            }
            if(one.optString("id").equals("active_read_moment_2")){
                one.putOpt("question", "請問您「閱讀該篇新聞當下」" + my_in + "約有幾分鐘可以使用手機閱讀新聞？(覺得沒空請填0，沒有印象請填-1)");
                continue;
            }
            if(one.optString("id").equals("active_read_moment_3")){
                one.putOpt("question", "請問您「閱讀該篇新聞當下」" + my_in + "，您正在從事以下哪個活動?");
                continue;
            }
            if(one.optString("id").equals("active_read_moment_4")){
                one.putOpt("question", "承上題，該項您「閱讀該篇新聞當下」" + my_in + "從事的活動，對您來說，該活動的複雜程度為何？(沒有印象請選4)(每個活動所需執行的動作不同，因此複雜程度不同。如吃蘋果只需要執行吃的動作，但如上課；則需要聆聽、作筆記、思考，是個相對複雜的活動。請依您主觀感受回答。)");
                continue;
            }
            if(one.optString("id").equals("active_read_moment_5")){
                one.putOpt("question", "對我來說，「閱讀該篇新聞當下」" + my_in + "是個適合收到該新聞通知的時機。請選擇您對此句描述的感受為何。(沒有印象請選4)");
                continue;
            }
            if(one.optString("id").equals("active_read_moment_6")){
                one.putOpt("question", "對我來說，「閱讀該篇新聞當下」" + my_in + "是個適合我瀏覽該篇新聞標題的時機。請選擇您對此句描述的感受為何。(沒有印象請選4)");
                continue;
            }
            if(one.optString("id").equals("active_read_moment_7")){
                one.putOpt("question", "對我來說，「閱讀該篇新聞當下」" + my_in + "是個適合我閱讀該篇新聞完整內容的時機。請選擇您對此句描述的感受為何。(沒有印象請選4)");
                continue;
            }
            if(one.optString("id").equals("active_read_behaf_8")){
                one.putOpt("question", "請問您點入「" + my_media + "」的新聞進行閱讀之動機為何？");
            }
        }
        File file;
        file = new File(getFilesDir(), "read.json");
        try{
            FileOutputStream fos = new FileOutputStream(file);//创建一个文件输出流
            fos.write(jsonRootObject.toString().getBytes());//将生成的JSON数据写出
            fos.close();//关闭输出流
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    private String generate_json_noti() throws JSONException {
        JSONObject jsonRootObject = new JSONObject(loadJSONFromAsset("noti.json"));
        JSONArray jsonQuestionObject = jsonRootObject.optJSONArray("questions");
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//        String my_news_id = sharedPrefs.getString(SAMPLE_ID, "NA");
        String my_title = sharedPrefs.getString(SAMPLE_TITLE, "NA");
        String my_media = sharedPrefs.getString(SAMPLE_MEDIA, "NA");
        String my_re = sharedPrefs.getString(SAMPLE_RECEIEVE, "NA");
        String my_in = sharedPrefs.getString(SAMPLE_IN, "NA");
        for (int i = 0; i < Objects.requireNonNull(jsonQuestionObject).length(); i++){
            JSONObject one = jsonQuestionObject.getJSONObject(i);
            if(one.optString("id").equals("base_1")){
                one.putOpt("question", "你是否有印象注意到在" + my_re + "「" + my_title + "」這則新聞通知的出現？");
                continue;
            }
            if(one.optString("id").equals("base_2")){
                if(!my_in.equals("NA")){
                    one.putOpt("question", "你是否有印象注意到在" + my_re + "「" + my_title + "」這則新聞通知的出現？");
                } else {
                    one.putOpt("question", "資料顯示，您並沒有在NewsMoment閱讀此篇新聞，請選擇 我沒有點入閱讀 繼續作答");
                    JSONArray op_arr = new JSONArray();
                    op_arr.put("我沒有點入閱讀");
                    one.putOpt("options", op_arr);
                }
                continue;
            }
            if(one.optString("id").equals("recieve_moment_1")){
                one.putOpt("question", "請問您在「該則通知出現當下」" + my_re + "，約有幾分鐘可以使用手機閱讀新聞？(覺得沒空請填0，沒有印象請填-1)");
                continue;
            }
            if(one.optString("id").equals("recieve_moment_2")){
                one.putOpt("question", "在「該則通知出現當下」" + my_re + "，您正在從事以下哪個活動?");
                continue;
            }
            if(one.optString("id").equals("recieve_moment_3")){
                one.putOpt("question", "承上題，該項您「該則通知出現當下」" + my_re + "從事的活動，對您來說，該活動的複雜程度為何？(沒有印象請選4)\n(每個活動所需執行的動作不同，因此複雜程度不同。如吃蘋果只需要執行吃的動作，但如上課；則需要聆聽、作筆記、思考，是個相對複雜的活動。請依您主觀感受回答。)");
                continue;
            }
            if(one.optString("id").equals("recieve_moment_4")){
                one.putOpt("question", "對我來說，「該則通知出現的當下」" + my_re + "是個適合收到該新聞通知的時機。請選擇您對此句描述的感受為何。(沒有印象請選4)");
                continue;
            }
            if(one.optString("id").equals("recieve_moment_5")){
                one.putOpt("question", "對我來說，「該則通知出現的當下」" + my_re + "，是個適合我瀏覽該篇新聞標題的時機。請選擇您對此句描述的感受為何。(沒有印象請選4)");
                continue;
            }
            if(one.optString("id").equals("recieve_moment_6")){
                one.putOpt("question", "對我來說，「該則通知出現的當下」" + my_re + "，是個適合我閱讀該篇新聞完整內容的時機。請選擇您對此句描述的感受為何。(沒有印象請選4)");
                continue;
            }
            if(one.optString("id").equals("recieve_moment_7")){
                one.putOpt("question", "請選擇最符合您「該則通知出現當下」" + my_re + "所處地點");
                continue;
            }
            if(one.optString("id").equals("read_moment_1")){
                if(!my_in.equals("NA")){
                    one.putOpt("question", "根據資料顯示，您在" + my_in + "閱讀該篇新聞");
                } else {
                    one.putOpt("question", "根據資料顯示，您未閱讀該篇新聞，請重填問題2");
                }
                continue;
            }
            if(one.optString("id").equals("read_moment_3")){
                one.putOpt("question", "請問您「閱讀該篇新聞當下」" + my_in + "約有幾分鐘可以使用手機閱讀新聞？(覺得沒空請填0，沒有印象請填-1)");
                continue;
            }
            if(one.optString("id").equals("read_moment_4")){
                one.putOpt("question", "在「閱讀該篇新聞當下」" + my_in + "，您正在從事以下哪個活動?");
                continue;
            }
            if(one.optString("id").equals("read_moment_5")){
                one.putOpt("question", "承上題，該項您「閱讀該篇新聞當下」" + my_in + "從事的活動，對您來說，該活動的複雜程度為何？(沒有印象請選4)\n(每個活動所需執行的動作不同，因此複雜程度不同。如吃蘋果只需要執行吃的動作，但如上課；則需要聆聽、作筆記、思考，是個相對複雜的活動。請依您主觀感受回答。)");
                continue;
            }
            if(one.optString("id").equals("read_moment_6")){
                one.putOpt("question", "對我來說，「閱讀該篇新聞當下」」" + my_in + "是個適合收到該新聞通知的時機。請選擇您對此句描述的感受為何。(沒有印象請選4)");
                continue;
            }
            if(one.optString("id").equals("read_moment_7")){
                one.putOpt("question", "對我來說，「閱讀該篇新聞當下」」" + my_in + "是個適合我瀏覽該篇新聞標題的時機。請選擇您對此句描述的感受為何。(沒有印象請選4)");
                continue;
            }
            if(one.optString("id").equals("read_moment_8")){
                one.putOpt("question", "對我來說，「閱讀該篇新聞當下」」" + my_in + "是個適合我閱讀該篇新聞完整內容的時機。請選擇您對此句描述的感受為何。(沒有印象請選4)");
                continue;
            }
            if(one.optString("id").equals("read_behaf_9")){
                one.putOpt("question", "請問您點入「" + my_media + "」的新聞進行閱讀之原因為何？");
                continue;
            }
            if(one.optString("id").equals("not_read_recieve_moment_1")){
                one.putOpt("question", "請問您在「該則通知出現當下」" + my_re + "，約有幾分鐘可以使用手機閱讀新聞？(覺得沒空請填0，沒有印象請填-1)");
                continue;
            }
            if(one.optString("id").equals("not_read_recieve_moment_2")){
                one.putOpt("question", "在「該則通知出現當下」" + my_re + "，您正在從事以下哪個活動?");
                continue;
            }
            if(one.optString("id").equals("not_read_recieve_moment_3")){
                one.putOpt("question", "承上題，該項您「該則通知出現當下」" + my_re + "從事的活動，對您來說，該活動的複雜程度為何？(沒有印象請選4)\n(每個活動所需執行的動作不同，因此複雜程度不同。如吃蘋果只需要執行吃的動作，但如上課；則需要聆聽、作筆記、思考，是個相對複雜的活動。請依您主觀感受回答。)");
                continue;
            }
            if(one.optString("id").equals("not_read_recieve_moment_4")){
                one.putOpt("question", "對我來說，「該則通知出現的當下」" + my_re + "是個適合收到該新聞通知的時機。請選擇您對此句描述的感受為何。(沒有印象請選4)");
                continue;
            }
            if(one.optString("id").equals("not_read_recieve_moment_5")){
                one.putOpt("question", "對我來說，「該則通知出現的當下」" + my_re + "是個適合我瀏覽該篇新聞標題的時機。請選擇您對此句描述的感受為何。(沒有印象請選4)");
                continue;
            }
            if(one.optString("id").equals("not_read_recieve_moment_6")){
                one.putOpt("question", "對我來說，「該則通知出現的當下」" + my_re + "是個適合我閱讀該篇新聞完整內容的時機。請選擇您對此句描述的感受為何。(沒有印象請選4)");
                continue;
            }
            if(one.optString("id").equals("not_read_recieve_moment_7")){
                one.putOpt("question", "請選擇最符合您「該則通知出現當下」" + my_re + "所處地點");
                continue;
            }
            if(one.optString("id").equals("miss_recieve_moment_1")){
                one.putOpt("question", "請問您在「該則通知出現當下」" + my_re + "，約有幾分鐘可以使用手機閱讀新聞？覺得沒空請填0，沒有印象請填-1)\n(接下來，請您回想該則新聞通知出現當下狀態(抵達時間)進行回答。)");
                continue;
            }
            if(one.optString("id").equals("miss_recieve_moment_2")){
                one.putOpt("question", "在「該則通知出現當下」" + my_re + "，您正在從事以下哪個活動?");
                continue;
            }
            if(one.optString("id").equals("miss_recieve_moment_3")){
                one.putOpt("question", "承上題，該項您「該則通知出現當下」" + my_re + "從事的活動，對您來說，該活動的複雜程度為何？(沒有印象請選4)\n(每個活動所需執行的動作不同，因此複雜程度不同。如吃蘋果只需要執行吃的動作，但如上課；則需要聆聽、作筆記、思考，是個相對複雜的活動。請依您主觀感受回答。)");
                continue;
            }
            if(one.optString("id").equals("miss_recieve_moment_4")){
                one.putOpt("question", "對我來說，「該則通知出現的當下」" + my_re + "是個適合收到該新聞通知的時機。請選擇您對此句描述的感受為何。(沒有印象請選4)");
                continue;
            }
            if(one.optString("id").equals("miss_recieve_moment_5")){
                one.putOpt("question", "對我來說，「該則通知出現的當下」" + my_re + " 是個適合我瀏覽該篇新聞標題的時機。請選擇您對此句描述的感受為何。(沒有印象請選4)");
                continue;
            }
            if(one.optString("id").equals("miss_recieve_moment_6")){
                one.putOpt("question", "對我來說，「該則通知出現的當下」" + my_re + "是個適合我閱讀該篇新聞完整內容的時機。請選擇您對此句描述的感受為何。(沒有印象請選4)");
                continue;
            }
            if(one.optString("id").equals("miss_recieve_moment_7")){
                one.putOpt("question", "請選擇最符合您「該則通知出現當下」" + my_re + "所處地點");
            }
        }
        File file;
        file = new File(getFilesDir(), "noti.json");
        try{
            FileOutputStream fos = new FileOutputStream(file);//创建一个文件输出流
            fos.write(jsonRootObject.toString().getBytes());//将生成的JSON数据写出
            fos.close();//关闭输出流
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    private String generate_diary_json(String diary_read, String diary_noti, Boolean exist) throws JSONException {
        if(exist){
//            Log.d("AlarmReceiver", diary_read);
//            Log.d("AlarmReceiver", diary_noti);
            List<String> diary_option_read = new ArrayList<>(Arrays.asList(diary_read.split("#")));
            List<String> diary_option_noti = new ArrayList<>(Arrays.asList(diary_noti.split("#")));
            JSONArray option_arr_read = new JSONArray();
            if(!diary_read.equals(ZERO_RESULT_STRING) && !diary_read.equals("")){
                for (int x = 0; x< diary_option_read.size(); x++){
                    if(diary_option_read.get(0).equals(ZERO_RESULT_STRING)){
                        break;
                    }
                    String origin_tmp = diary_option_read.get(x);
                    List<String> tmp_array = new ArrayList<>(Arrays.asList(origin_tmp.split("\n")));
                    origin_tmp = tmp_array.get(0) + "\n" + tmp_array.get(1)  + "\n" + tmp_array.get(2)  + "\n" + tmp_array.get(3);
                    option_arr_read.put(origin_tmp);
                }
            }
            option_arr_read.put("無");
            JSONArray option_arr_noti = new JSONArray();
            if(!diary_noti.equals(ZERO_RESULT_STRING) && !diary_noti.equals("")){
                for (int x = 0; x< diary_option_noti.size(); x++){
                    if(diary_option_noti.get(0).equals(ZERO_RESULT_STRING)){
                        break;
                    }
                    String origin_tmp = diary_option_noti.get(x);
                    List<String> tmp_array = new ArrayList<>(Arrays.asList(origin_tmp.split("\n")));
                    origin_tmp = tmp_array.get(0) + "\n" + tmp_array.get(1)  + "\n" + tmp_array.get(2)  + "\n" + tmp_array.get(3);
                    option_arr_noti.put(origin_tmp);
                }
            }
            option_arr_noti.put("無");
            JSONObject jsonRootObject = new JSONObject(loadJSONFromAsset("diary.json"));
            JSONArray jsonQuestionObject = jsonRootObject.optJSONArray("questions");
            for (int i = 0; i < Objects.requireNonNull(jsonQuestionObject).length(); i++){
                JSONObject one = jsonQuestionObject.getJSONObject(i);
                if(one.optString("id").equals("opportune_read_1")){
                    one.putOpt("options", option_arr_read);
                    continue;
                }
                if(one.optString("id").equals("inopportune_read_1")){
                    one.putOpt("options", option_arr_read);
                    continue;
                }
                if(one.optString("id").equals("opportune_noti_1")){
                    one.putOpt("options", option_arr_noti);
                    continue;
                }
                if(one.optString("id").equals("inopportune_noti_1")){
                    one.putOpt("options", option_arr_noti);
                }
            }
            File file;
            file = new File(getFilesDir(), "diary.json");
            try{
                FileOutputStream fos = new FileOutputStream(file);//创建一个文件输出流
                fos.write(jsonRootObject.toString().getBytes());//将生成的JSON数据写出
                fos.close();//关闭输出流
//                Toast.makeText(getApplicationContext(),"创建成功紀錄！",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file.getAbsolutePath();
        } else {
            JSONArray option_arr = new JSONArray();
            option_arr.put("無");
            JSONObject jsonRootObject = new JSONObject(loadJSONFromAsset("diary.json"));
            File file;
            file = new File(getFilesDir(), "diary.json");
            try{
                FileOutputStream fos = new FileOutputStream(file);//创建一个文件输出流
                fos.write(jsonRootObject.toString().getBytes());//将生成的JSON数据写出
                fos.close();//关闭输出流
//                Toast.makeText(getApplicationContext(),"创建成功無閱讀紀錄！",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file.getAbsolutePath();
        }

    }
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private String generate_esm_json(String read_news_title_array_string, String noti_news_title_array_string, String file_name) throws JSONException {
//        if(file_name.equals("0.json")){
//            List<String> noti_news_title_array_json = new ArrayList<>(Arrays.asList(noti_news_title_array_string.split("#")));
//            JSONArray notifications_arr = new JSONArray();
//            for (int x = 0; x< noti_news_title_array_json.size(); x++){
//                if(noti_news_title_array_json.get(0).equals(ZERO_RESULT_STRING)){
//                    break;
//                }
//                notifications_arr.put(noti_news_title_array_json.get(x));
//            }
//            notifications_arr.put(NOTI_UNCLICK_LAST_ONE);
//            JSONObject jsonRootObject = new JSONObject(loadJSONFromAsset(file_name));
//            JSONArray jsonQuestionObject = jsonRootObject.optJSONArray("questions");
//            for (int i = 0; i < Objects.requireNonNull(jsonQuestionObject).length(); i++){
//                JSONObject one = jsonQuestionObject.getJSONObject(i);
//                //for notification unclick 24
//                if(one.optString("id").equals("read_title_1")){
//                    one.putOpt("options", notifications_arr);
//                    if(notifications_arr.length()==1){
//                        one.putOpt("header", one.optString("header")+"(無推播紀錄)");
//                        one.putOpt("question", "資料庫無推播紀錄，麻煩直接填選-沒有印象");
//                    } else {
//                        one.putOpt("header", one.optString("header")+"(>1推播紀錄)");
//                    }
//                    continue;
//                }
//                //for notification unclick 46
//                if(one.optString("id").equals("46")){
//                    one.putOpt("options", notifications_arr);
//                    if(notifications_arr.length()==1){
//                        one.putOpt("header", one.optString("header")+"(無推播紀錄)");
//                        one.putOpt("question", "資料庫無推播紀錄，麻煩直接填選-沒有印象");
//                    } else {
//                        one.putOpt("header", one.optString("header")+"(>1推播紀錄)");
//                    }
//                }
//            }
//
//            File file;
//            file = new File(getFilesDir(), "0.json");
//            try{
//                FileOutputStream fos = new FileOutputStream(file);//创建一个文件输出流
//                fos.write(jsonRootObject.toString().getBytes());//将生成的JSON数据写出
//                fos.close();//关闭输出流
////                Toast.makeText(getApplicationContext(),"创建成功無閱讀紀錄！",Toast.LENGTH_SHORT).show();
////                Log.d("lognewsselect", "!!!!!!!!!!!!!!!!!!!()" + file.getAbsolutePath());
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return file.getAbsolutePath();
//        } else {
//            //reorder_news_title
//            final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//            String target_history = sharedPrefs.getString(ESM_TARGET_NEWS_TITLE, "");
//            if(target_history.equals("")){
//                //no sample preference
//                final_news_title_array = new ArrayList<String>(Arrays.asList(read_news_title_array_string.split("#")));
//            } else {
////                final_news_title_array = new ArrayList<String>(Arrays.asList(read_news_title_array_string.split("#")));
//                //old
//                List<String> history_list_title = new ArrayList<String>(Arrays.asList(target_history.split("#")));
//                for(int i = 0; i<history_list_title.size();i++){
//                    List<String> very_tmp = new ArrayList<String>(Arrays.asList(history_list_title.get(i).split("¢")));
//                    if(very_tmp.size()==6){
//                        HistoryNewsTitleObjListArray.add(new NewsCompareObj(parseInt(very_tmp.get(1)), parseInt(very_tmp.get(2)), very_tmp.get(3), i, history_list_title.get(i),0));
//                    } else {
//                        Log.d("lognewsselect", "======================OLD==================================very_tmp" + very_tmp.size());
//                    }
//                }
//                //new
//                List<String> read_news_title_array_json = new ArrayList<String>(Arrays.asList(read_news_title_array_string.split("#")));
//                if(read_news_title_array_json.size()>0 && !read_news_title_array_json.get(0).equals(ZERO_RESULT_STRING)){
//                    for(int i = 0; i<read_news_title_array_json.size();i++){
//                        List<String> very_tmp = new ArrayList<String>(Arrays.asList(read_news_title_array_json.get(i).split("¢")));
//                        if(very_tmp.size()==6){
//                            MyNewsTitleObjListArray.add(new NewsCompareObj(parseInt(very_tmp.get(1)), parseInt(very_tmp.get(2)), very_tmp.get(3), i, read_news_title_array_json.get(i),0));
//                        } else {
//                            Log.d("lognewsselect", "======================NEW=================================very_tmp" + very_tmp.size());
//                        }
//                    }
//                }
//                compare_and_reorder();
//            }
//
//            List<String> noti_news_title_array_json = new ArrayList<String>(Arrays.asList(noti_news_title_array_string.split("#")));
//            JSONArray notifications_arr = new JSONArray();
//            for (int x = 0; x< noti_news_title_array_json.size(); x++){
//                if(noti_news_title_array_json.get(0).equals("zero_result")){
//                    break;
//                }
//                notifications_arr.put(noti_news_title_array_json.get(x));
//            }
//            notifications_arr.put(NOTI_UNCLICK_LAST_ONE);
//            String template_file_name = "1.json";
//            int title_count = final_news_title_array.size();
//            if (final_news_title_array.size()==1){
//                template_file_name = "1.json";
//            } else {
//                template_file_name = "2.json";
//            }
//            //then use two template
//            JSONObject jsonRootObject = new JSONObject(loadJSONFromAsset(template_file_name));
//            JSONArray jsonQuestionObject = jsonRootObject.optJSONArray("questions");
//            JSONArray addedArray = new JSONArray();
//            if(title_count>2){
//                //we have to add more
//                String strJsons="    {\n" +
//                        "      \"id\": \"add_2\",\n" +
//                        "      \"header\": \"Question add_2(2_2)\",\n" +
//                        "      \"question\": \"您對於「某某news title」這篇新聞，是否有點進來閱讀新聞內文，且未看過其它媒體或平台「引用相同新聞稿，闡述字句幾乎相同」的新聞？\",\n" +
//                        "      \"question_type\": \"single_select\",\n" +
//                        "      \"options\": [\n" +
//                        "        \"有點入閱讀，且沒看過相同的新聞\",\n" +
//                        "        \"沒有點入閱讀過或有看過相同的新聞\"\n" +
//                        "      ],\n" +
//                        "      \"show_if\": {\n" +
//                        "        \"operation\": \"and\",\n" +
//                        "        \"subconditions\": [\n" +
//                        "          {\n" +
//                        "            \"id\": \"add_0\",\n" +
//                        "            \"operation\": \"equals\",\n" +
//                        "            \"value\": \"沒有點入閱讀過或有看過相同的新聞\"\n" +
//                        "          },\n" +
//                        "          {\n" +
//                        "            \"id\": \"base_1\",\n" +
//                        "            \"operation\": \"not equals\",\n" +
//                        "            \"value\": \"沒有\"\n" +
//                        "          }\n" +
//                        "        ]\n" +
//                        "      }\n" +
//                        "    }";
//                for(int i = 2; i<title_count ; i++){
//                    JSONObject tmp_jsonObject = new JSONObject(strJsons);
//                    tmp_jsonObject.putOpt("id", "add_" + i);
//                    List<String> tmp_array = new ArrayList<String>(Arrays.asList(final_news_title_array.get(i).split("¢")));
//                    tmp_jsonObject.putOpt("question", "您對於「" + tmp_array.get(0) + "」這篇新聞，是否有點進來閱讀新聞內文，且未看過其它媒體或平台「引用相同新聞稿，闡述字句幾乎相同」的新聞？");
//                    tmp_jsonObject.putOpt("header", "add_" + i);
//                    JSONArray n_condi_for_ques = new JSONArray();
//                    JSONObject n_condi_single = new JSONObject();
//                    for(int j=0; j<i;j++){
//                        n_condi_single.putOpt("id", "add_" + j);
//                        n_condi_single.putOpt("operation", "equals");
//                        n_condi_single.putOpt("value", NOT_TARGET_READ_NEWS_TITLE_ANSWER);
//                        n_condi_for_ques.put(n_condi_single);
//                        n_condi_single = new JSONObject();
//                    }
//                    n_condi_single.putOpt("id", "base_1");
//                    n_condi_single.putOpt("operation", "not equals");
//                    n_condi_single.putOpt("value", "沒有");
//                    n_condi_for_ques.put(n_condi_single);
//                    tmp_jsonObject.optJSONObject("show_if").putOpt("subconditions", n_condi_for_ques);
////                    Log.d("lognewsselect", "new object " + i + " " + tmp_jsonObject);
//
//                    jsonQuestionObject.put(tmp_jsonObject);//origin
//                    addedArray.put(tmp_jsonObject);//append
//                }
//
//            }
//
//
//
//
//            for (int i =0 ; i < jsonQuestionObject.length();i++){
//                JSONObject one = jsonQuestionObject.getJSONObject(i);
//                //add0
//                if(title_count>=1 && one.optString("id").equals("add_0")){
//                    one.putOpt("id", "add_0");
//                    List<String> tmp_array = new ArrayList<String>(Arrays.asList(final_news_title_array.get(0).split("¢")));
//                    one.putOpt("question", "您對於「"+ tmp_array.get(0) +"」這篇新聞，是否有點進來閱讀新聞內文，且未看過其它媒體或平台「引用相同新聞稿，闡述字句幾乎相同」的新聞？");
//                    one.putOpt("header", "add_0");
//                    continue;
//                }
//                //add1
//                if(title_count>=2 && one.optString("id").equals("add_1")){
//                    one.putOpt("id", "add_1");
//                    List<String> tmp_array = new ArrayList<String>(Arrays.asList(final_news_title_array.get(1).split("¢")));
//                    one.putOpt("question", "您對於「"+ tmp_array.get(0) +"」這篇新聞，是否有點進來閱讀新聞內文，且未看過其它媒體或平台「引用相同新聞稿，闡述字句幾乎相同」的新聞？");
//                    one.putOpt("header", "add_1");
////                    Log.d("lognewsselect", "old for title add1" + one);
//                    JSONArray condi_for_ques = new JSONArray();
//                    JSONObject condi_single = new JSONObject();
//                    for(int j=0; j<1;j++){
//                        condi_single.putOpt("id", "add_" + j);
//                        condi_single.putOpt("operation", "equals");
//                        condi_single.putOpt("value", NOT_TARGET_READ_NEWS_TITLE_ANSWER);
//                        condi_for_ques.put(condi_single);
//                        condi_single = new JSONObject();
//                    }
//                    condi_single.putOpt("id", "base_1");
//                    condi_single.putOpt("operation", "not equals");
//                    condi_single.putOpt("value", "沒有");
//                    condi_for_ques.put(condi_single);
//                    one.optJSONObject("show_if").putOpt("subconditions", condi_for_ques);
////                    Log.d("lognewsselect", "new for title add1" + one);
//
//                    if (title_count>2){
//                        //move back one or more position
//                        int difference = title_count-2;
//                        for (int k = jsonQuestionObject.length()-1; k-difference>i; k--) {
//                            jsonQuestionObject.put(k, jsonQuestionObject.get(k-difference));
//                        }
//                        int insert_pos = i+1;
//                        //insert in the middle
//                        for(int p = 0; p<addedArray.length();p++){
//                            jsonQuestionObject.put(insert_pos, addedArray.get(p));
//                            insert_pos++;
//                        }
//                    }
//                    continue;
//                }
//                //for notification unclick 24
//                if(one.optString("id").equals("read_title_1")){
//                    one.putOpt("options", notifications_arr);
//                    if(notifications_arr.length()==1){
//                        one.putOpt("header", one.optString("header")+"(無推播紀錄)");
//                        one.putOpt("question", "資料庫無推播紀錄，麻煩直接填選-沒有印象");
//                    } else {
//                        one.putOpt("header", one.optString("header")+"(>1推播紀錄)");
//                    }
//                    continue;
//                }
//                //for notification unclick 46
//                if(one.optString("id").equals("46")){
//                    one.putOpt("options", notifications_arr);
//                    if(notifications_arr.length()==1){
//                        one.putOpt("header", one.optString("header")+"(無推播紀錄)");
//                        one.putOpt("question", "資料庫無推播紀錄，麻煩直接填選-沒有印象");
//                    } else {
//                        one.putOpt("header", one.optString("header")+"(>1推播紀錄)");
//                    }
//                    continue;
//                }
//                //for read
//                if(one.optString("id").equals("base_3_2")){
////                    Log.d("lognewsselect", "old for title 0" + one);
//                    JSONArray my = new JSONArray();
//                    JSONObject tt = new JSONObject();
//                    for(int j=0; j<title_count;j++){
//                        tt.putOpt("id", "add_" + j);
//                        tt.putOpt("operation", "equals");
//                        tt.putOpt("value", TARGET_READ_NEWS_TITLE_ANSWER);
//                        my.put(tt);
//                        tt = new JSONObject();
//                    }
//                    one.optJSONObject("show_if").putOpt("subconditions", my);
////                    Log.d("lognewsselect", "new for title 0" + one);
////                    one.optJSONObject("show_if").optJSONObject("subconditions");
//                    continue;
//                }
//                //for not read
//                if(one.optString("id").equals("base_3_1")){
////                    JSONArray tmp = one.optJSONObject("show_if").getJSONArray("subconditions");
////                    Log.d("lognewsselect", "old for title 45" + one);
//                    JSONArray my = new JSONArray();
//                    JSONObject tt = new JSONObject();
//                    for(int j=0; j<title_count;j++){
//                        tt.putOpt("id", "add_" + j);
//                        tt.putOpt("operation", "equals");
//                        tt.putOpt("value", NOT_TARGET_READ_NEWS_TITLE_ANSWER);
//                        my.put(tt);
//                        tt = new JSONObject();
//                    }
//                    tt.putOpt("id", "base_1");
//                    tt.putOpt("operation", "not equals");
//                    tt.putOpt("value", "沒有");
//                    my.put(tt);
//                    one.optJSONObject("show_if").putOpt("subconditions", my);
////                    Log.d("lognewsselect", "new for title 45" + one);
////                    one.optJSONObject("show_if").optJSONObject("subconditions");
//                    continue;
//                }
//            }
//
//            File file;
//            file = new File(getFilesDir(), "diary.json");
//            try{
//                FileOutputStream fos = new FileOutputStream(file);//创建一个文件输出流
//                fos.write(jsonRootObject.toString().getBytes());//将生成的JSON数据写出
//                fos.close();//关闭输出流
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return file.getAbsolutePath();
//
//        }
//    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private void compare_and_reorder() {
//        for(int i=0; i<MyNewsTitleObjListArray.size();i++){
//            int sum = 0;
//            int share = MyNewsTitleObjListArray.get(i).getShare_or_not();
//            int trigger = MyNewsTitleObjListArray.get(i).getTrigger_by();
//            String cat = MyNewsTitleObjListArray.get(i).getCategory();
//            for(int x = 0; x<HistoryNewsTitleObjListArray.size();x++){
//                if(HistoryNewsTitleObjListArray.get(x).getShare_or_not()!=share){
//                    sum++;
////                    Log.d("lognewsselect", "-----share");
//                }
//                if(HistoryNewsTitleObjListArray.get(x).getTrigger_by()!=trigger){
//                    sum++;
////                    Log.d("lognewsselect", "-----trigger");
//                }
//                if(!HistoryNewsTitleObjListArray.get(x).getCategory().equals(cat)){
//                    sum++;
////                    Log.d("lognewsselect", "-----cat");
//                }
//            }
//            MyNewsTitleObjListArray.get(i).setSum(sum);
//        }
//        Collections.sort(MyNewsTitleObjListArray);
//        final_news_title_array.clear();
//        for(int i=MyNewsTitleObjListArray.size()-1; i>=0;i--){
//            Log.d("lognewsselect", "------------------------------------" + MyNewsTitleObjListArray.get(i).getSum());
//            final_news_title_array.add(MyNewsTitleObjListArray.get(i).getNews_title());
//        }
//    }

    public String loadJSONFromAsset(String file_name) {
        String json;
        try {
            InputStream is = getApplication().getAssets().open(file_name);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    @Override
    public void onCallback(String value) {

    }
}
