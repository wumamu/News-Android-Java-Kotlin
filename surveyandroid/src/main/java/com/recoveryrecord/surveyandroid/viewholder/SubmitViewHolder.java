package com.recoveryrecord.surveyandroid.viewholder;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.recoveryrecord.surveyandroid.AnswerProvider;
import com.recoveryrecord.surveyandroid.R;
import com.recoveryrecord.surveyandroid.SubmitSurveyHandler;
import com.recoveryrecord.surveyandroid.example.DbHelper.DiaryDbHelper;
import com.recoveryrecord.surveyandroid.example.DbHelper.ESMDbHelper;
import com.recoveryrecord.surveyandroid.example.sqlite.Diary;
import com.recoveryrecord.surveyandroid.example.sqlite.ESM;
import com.recoveryrecord.surveyandroid.question.QuestionsWrapper.SubmitData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class SubmitViewHolder extends RecyclerView.ViewHolder {

    private Button submitButton;
    Boolean is_esm = false, is_diary = false;
    String esm_id = "", diary_id = "", type = "";
    String result_json = "";
    String sample_title = "";

    String target_title = "NA";
    String target_receieve_time = "NA";
    String target_in_time = "NA";
    String target_id = "NA";
    String receieve_situation = "NA";
    String receieve_place = "NA";
    String read_situation = "NA";
    String read_place = "NA";

    List<String> sample_read_Array = new ArrayList<>();

    String target_opportune = "NA";
    String target_opportune_news_id = "NA";
    String target_opportune_title = "NA";
    String target_inopportune = "NA";
    String target_inopportune_news_id = "NA";
    String target_inopportune_title = "NA";

    String DOC_ID_KEY = "id";
    String SURVEY_PAGE_ID = "id";
    String NOTIFICATION_TYPE_KEY = "type";
    String NOTIFICATION_TYPE_VALUE_NEWS = "news";
    String NOTIFICATION_TYPE_VALUE_ESM = "esm";
    String NOTIFICATION_TYPE_VALUE_DIARY = "diary";
    String LOADING_PAGE_TYPE_KEY = "type";
    String LOADING_PAGE_TYPE_ESM = "esm";
    String LOADING_PAGE_TYPE_DIARY = "diary";


    String ESM_DONE_TOTAL = "ESMDoneTotal";
    String ESM_DAY_DONE_PREFIX = "ESMDayDone_";
    String DIARY_DONE_TOTAL = "DiaryDoneTotal";
    String DIARY_DAY_DONE_PREFIX = "DiaryDayDone_";

    String PUSH_ESM_SUBMIT_TIME = "submit_timestamp";
    String PUSH_ESM_SAMPLE = "sample";
    String PUSH_ESM_RESULT = "result";
    String PUSH_ESM_TARGET_NEWS_ID = "target_news_id";
    String PUSH_ESM_TARGET_TITLE = "target_read_title";

    String PUSH_ESM_TARGET_RECEIEVE_TIME = "target_receieve_time";
    String PUSH_ESM_TARGET_IN_TIME = "target_in_time";
    String PUSH_ESM_TARGET_RECEIEVE_SITUATION = "target_receieve_situation";
    String PUSH_ESM_TARGET_RECEIEVE_PLACE = "target_receieve_place";
    String PUSH_ESM_TARGET_READ_SITUATION = "target_read_situation";
    String PUSH_ESM_TARGET_READ_PLACE = "target_read_place";

    String TEST_USER_COLLECTION = "test_users";
    String PUSH_ESM_COLLECTION = "push_esm";
    String PUSH_DIARY_COLLECTION = "push_diary";
    String PUSH_DIARY_DONE = "done";
    String PUSH_DIARY_SUBMIT_TIME = "submit_timestamp";
    String PUSH_DIARY_RESULT = "result";
    String PUSH_DIARY_INOPPORTUNE_TARGET_READ = "inopportune_read_target";
    String PUSH_DIARY_OPPORTUNE_TARGET_RAED = "opportune_read_target";
    String PUSH_DIARY_INOPPORTUNE_TARGET_NOTI = "inopportune_noti_target";
    String PUSH_DIARY_OPPORTUNE_TARGET_NOTI = "opportune_noti_target";


    String DIARY_READ_HISTORY_CANDIDATE = "DiaryTargetOptionArrayRead";
    String DIARY_NOTI_HISTORY_CANDIDATE = "DiaryTargetOptionArrayNoti";
    String ZERO_RESULT_STRING = "zero_result";

    String TO_DIARY_LIST = "DiaryList";

    String SAMPLE_ID = "sample_id";
    String SAMPLE_TITLE = "sample_title";
    String SAMPLE_MEDIA = "sample_media";
    String SAMPLE_RECEIEVE = "sample_receieve";
    String SAMPLE_IN = "sample_in";
    String EXIST_READ = "exist_read";//esm
    String ESM_TYPE = "esm_type";
    String EXIST_ESM = "exist_esm";//diary

    List<String> in_re = new ArrayList<>();
    List<String> op_re = new ArrayList<>();
    List<String> in_no = new ArrayList<>();
    List<String> op_no = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public SubmitViewHolder(@NonNull View itemView) {
        super(itemView);
        submitButton = itemView.findViewById(R.id.submit_button);
    }

    public void bind(final SubmitData submitData, final AnswerProvider answerProvider, final SubmitSurveyHandler submitSurveyHandler) {
        submitButton.setText(submitData.buttonTitle);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                submitSurveyHandler.submit(submitData.url, answerProvider.allAnswersJson());
                String device_id = Settings.Secure.getString(((Activity)v.getContext()).getContentResolver(), Settings.Secure.ANDROID_ID);
                if (((Activity)v.getContext()).getIntent().getExtras() != null) {
                    Bundle b = ((Activity)v.getContext()).getIntent().getExtras();
                    type = Objects.requireNonNull(b.getString(NOTIFICATION_TYPE_KEY));
                    if (type.equals(LOADING_PAGE_TYPE_ESM)){
                        esm_id = Objects.requireNonNull(b.getString(SURVEY_PAGE_ID));
                        if(!esm_id.equals("")){
                            is_esm = true;
                        }
                    } else if (type.equals(LOADING_PAGE_TYPE_DIARY)){
                        diary_id = Objects.requireNonNull(b.getString(SURVEY_PAGE_ID));
                        if(!diary_id.equals("")){
                            is_diary = true;
                        }
                    }
                }
                result_json = answerProvider.allAnswersJson();
                final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences((Activity)v.getContext());
                if(is_esm){
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    Calendar calendar = Calendar.getInstance();
                    int day_index = calendar.get(Calendar.DAY_OF_YEAR);
                    int esm_done = sharedPrefs.getInt(ESM_DONE_TOTAL, 0);
                    int esm_day_done = sharedPrefs.getInt(ESM_DAY_DONE_PREFIX+ day_index, 0);
                    editor.putInt(ESM_DONE_TOTAL, esm_done+1);
                    editor.putInt(ESM_DAY_DONE_PREFIX + day_index, esm_day_done+1);
                    editor.apply();
//                    Boolean exist_read = sharedPrefs.getBoolean(EXIST_READ, false);
                    int esm_type = sharedPrefs.getInt(ESM_TYPE, 3);
                    if(esm_type==0 || esm_type==2){
                        try {
                            assert result_json != null;
                            JSONObject jsonRootObject = new JSONObject(result_json);
                            JSONObject jsonAnswerObject = jsonRootObject.getJSONObject("answers");
                            Iterator<String> keys = jsonAnswerObject.keys();
                            Boolean notice = false;
                            if(jsonAnswerObject.has("base_1")){
                                if(jsonAnswerObject.getString("base_1").equals("有，我有印象看到此則通知出現")){
                                    notice = true;
                                }
                            }
                            if (notice && jsonAnswerObject.has("base_2")) {
                                //base 1 A or B 有，我有印象看到此則通知出現
                                if(jsonAnswerObject.getString("base_2").equals("我有點入閱讀")){
                                    //A ############################################################
                                    target_id = sharedPrefs.getString(SAMPLE_ID, "NA");
                                    target_title = sharedPrefs.getString(SAMPLE_TITLE, "NA");
                                    target_in_time = sharedPrefs.getString(SAMPLE_IN, "NA");
                                    target_receieve_time = sharedPrefs.getString(SAMPLE_RECEIEVE, "NA");
                                    if (jsonAnswerObject.has("recieve_moment_2")) {
                                        receieve_situation = jsonAnswerObject.getString("recieve_moment_2");
                                    }
                                    if (jsonAnswerObject.has("recieve_moment_6")) {
                                        receieve_place =  jsonAnswerObject.getString("recieve_moment_6");
                                    }
                                    if (jsonAnswerObject.has("read_moment_2")) {
                                        String answer = jsonAnswerObject.getString("read_moment_2");
                                        if(answer.equals("活動與地點皆相同")){
                                            //do nothing
                                            read_situation = receieve_situation;
                                            read_place = receieve_place;
                                        } else if (answer.equals("活動相同，地點不同")){
                                            read_situation = receieve_situation;
                                            if (jsonAnswerObject.has("read_moment_8")) {
                                                read_place =  jsonAnswerObject.getString("read_moment_8");
                                            }
                                        } else if (answer.equals("活動不同，地點相同")){
                                            read_place = receieve_place;
                                            if (jsonAnswerObject.has("read_moment_4")) {
                                                read_situation = jsonAnswerObject.getString("read_moment_4");
                                            }
                                        } else { //活動、地點均不相同
                                            if (jsonAnswerObject.has("read_moment_8")) {
                                                read_place =  jsonAnswerObject.getString("read_moment_8");
                                            }
                                            if (jsonAnswerObject.has("read_moment_4")) {
                                                read_situation = jsonAnswerObject.getString("read_moment_4");
                                            }
                                        }
                                    }
                                } else if (jsonAnswerObject.getString("base_2").equals("我沒有點入閱讀")){
                                    //B ############################################################
                                    target_id = sharedPrefs.getString(SAMPLE_ID, "NA");
                                    target_title = sharedPrefs.getString(SAMPLE_TITLE, "NA");
                                    target_receieve_time = sharedPrefs.getString(SAMPLE_RECEIEVE, "NA");
                                    if (jsonAnswerObject.has("not_read_recieve_moment_2")) {
                                        receieve_situation = jsonAnswerObject.getString("not_read_recieve_moment_2");
                                    }
                                    if (jsonAnswerObject.has("not_read_recieve_moment_6")) {
                                        receieve_place =  jsonAnswerObject.getString("not_read_recieve_moment_6");
                                    }
                                }
                            } else {
                                //base 1 C 沒有，我沒有注意到這則通知出現 #############################
                                target_id = sharedPrefs.getString(SAMPLE_ID, "NA");
                                target_title = sharedPrefs.getString(SAMPLE_TITLE, "NA");
                                target_receieve_time = sharedPrefs.getString(SAMPLE_RECEIEVE, "NA");
                                if (jsonAnswerObject.has("miss_recieve_moment_2")) {
                                    receieve_situation = jsonAnswerObject.getString("miss_recieve_moment_2");
                                }
                                if (jsonAnswerObject.has("miss_recieve_moment_6")) {
                                    receieve_place =  jsonAnswerObject.getString("miss_recieve_moment_6");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (esm_type==1){
                        try{
                            assert result_json != null;
                            JSONObject jsonRootObject = new JSONObject(result_json);
                            JSONObject jsonAnswerObject = jsonRootObject.getJSONObject("answers");
                            if(jsonAnswerObject.has("active_base_1")){
                                if(jsonAnswerObject.getString("active_base_1").equals("有")){
                                    target_id = sharedPrefs.getString(SAMPLE_ID, "NA");
                                    target_title = sharedPrefs.getString(SAMPLE_TITLE, "NA");
                                    target_in_time = sharedPrefs.getString(SAMPLE_IN, "NA");
                                    if (jsonAnswerObject.has("active_read_moment_3")) {
                                        read_situation = jsonAnswerObject.getString("active_read_moment_3");
                                    }
                                    if (jsonAnswerObject.has("active_read_moment_5")) {
                                        read_place =  jsonAnswerObject.getString("active_read_moment_5");
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    final Timestamp current = Timestamp.now();
                    final DocumentReference rbRef = db.collection(PUSH_ESM_COLLECTION).document(device_id + " " + esm_id);
                    rbRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    rbRef.update(
                                            PUSH_ESM_TARGET_NEWS_ID, target_id,
                                            PUSH_ESM_TARGET_TITLE, target_title,
                                            PUSH_ESM_TARGET_IN_TIME, target_in_time,
                                            PUSH_ESM_TARGET_RECEIEVE_TIME, target_receieve_time,
                                            PUSH_ESM_TARGET_RECEIEVE_SITUATION, receieve_situation,
                                            PUSH_ESM_TARGET_RECEIEVE_PLACE, receieve_place,
                                            PUSH_ESM_TARGET_READ_SITUATION, read_situation,
                                            PUSH_ESM_TARGET_READ_PLACE, read_place,
                                            PUSH_ESM_SUBMIT_TIME, current,
                                            PUSH_ESM_RESULT, result_json)//another field
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
                    //sqlite
                    ESM myesm = new ESM();
                    myesm.setKEY_DOC_ID(device_id + " " + esm_id);
                    myesm.setKEY_SUBMIT_TIMESTAMP(current.getSeconds());
                    myesm.setKEY_RESULT(result_json);
                    myesm.setKEY_NOTI_READ_NEWS_ID(target_id);
                    myesm.setKEY_NOTI_READ_TITLE(target_title);
                    myesm.setKEY_NOTI_READ_IN_TIME(target_in_time);
                    myesm.setKEY_NOTI_READ_RECEIEVE_TIME(target_receieve_time);
                    myesm.setKEY_NOTI_READ_SITUATION(receieve_situation);
                    myesm.setKEY_NOTI_READ_PLACE(receieve_place);
//                    myesm.setKEY_NOTI_NOT_READ_NEWS_ID(device_id + " " + esm_id);
//                    myesm.setKEY_NOTI_NOT_READ_TITLE(device_id + " " + esm_id);
//                    myesm.setKEY_NOTI_NOT_READ_RECEIEVE_TIME(device_id + " " + esm_id);

//                    myesm.setKEY_SELF_READ_NEWS_ID(device_id + " " + esm_id);
//                    myesm.setKEY_SELF_READ_TITLE(device_id + " " + esm_id);
//                    myesm.setKEY_SELF_READ_IN_TIME(device_id + " " + esm_id);
//                    myesm.setKEY_SELF_READ_RECEIEVE_TIME(device_id + " " + esm_id);
                    myesm.setKEY_SELF_READ_SITUATION(read_situation);
                    myesm.setKEY_SELF_READ_PLACE(read_place);


                    ESMDbHelper dbHandler = new ESMDbHelper((Activity)v.getContext());
                    dbHandler.UpdatePushESMDetailsSubmit(myesm);
                } else if(is_diary) {
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    Calendar calendar = Calendar.getInstance();
                    int day_index = calendar.get(Calendar.DAY_OF_YEAR);
                    int diary_done = sharedPrefs.getInt(DIARY_DONE_TOTAL, 0);
                    int diary_day_done = sharedPrefs.getInt(DIARY_DAY_DONE_PREFIX+ day_index, 0);
                    editor.putInt(DIARY_DONE_TOTAL, diary_done+1);
                    editor.putInt(DIARY_DAY_DONE_PREFIX + day_index, diary_day_done+1);
                    editor.apply();
                    final Timestamp current = Timestamp.now();
                    final DocumentReference rbRef = db.collection(PUSH_DIARY_COLLECTION).document(device_id + " " + diary_id);
                    Boolean exist_esm = sharedPrefs.getBoolean(EXIST_ESM, false);
                    String diary_read = sharedPrefs.getString(DIARY_READ_HISTORY_CANDIDATE, ZERO_RESULT_STRING);
                    String diary_noti = sharedPrefs.getString(DIARY_NOTI_HISTORY_CANDIDATE, ZERO_RESULT_STRING);
                    List<String> diary_read_array = new ArrayList<String>(Arrays.asList(diary_read.split("#")));
                    List<String> diary_noti_array = new ArrayList<String>(Arrays.asList(diary_noti.split("#")));
                    //DiaryLoadingPage
                    //{news_title}\n{news_time}\n{news_situation}\n{news_place}\n{news_id}#
                    in_re.clear();
                    op_re.clear();
                    in_no.clear();
                    op_no.clear();
                    if(exist_esm){
                        try {
                            assert result_json != null;
                            JSONObject jsonRootObject = new JSONObject(result_json);
                            JSONObject jsonAnswerObject = jsonRootObject.getJSONObject("answers");
                            if (jsonAnswerObject.has("inopportune_noti_1")) {
                                if(!jsonAnswerObject.getString("inopportune_noti_1").equals("無")){
                                    List<String> view_array = new ArrayList<String>(Arrays.asList(jsonAnswerObject.getString("inopportune_noti_1").split("\n")));
                                    //{news_title}\n{news_time}\n{news_situation}\n{news_place}#
                                    for (int j=0; j<diary_read_array.size(); j++){
                                        List<String> data_array = new ArrayList<String>(Arrays.asList(diary_read_array.get(j).split("\n")));
                                        //把標題分出來
                                        if(data_array.get(0).equals(view_array.get(0))){
                                            in_no.add(data_array.get(4));//id
                                            in_no.add(data_array.get(0));//title
                                            in_no.add(data_array.get(1));//in time
                                            in_no.add(data_array.get(2));//situation
                                            in_no.add(data_array.get(3));//place
                                            break;
                                        }
                                    }
                                }
                            }
                            if (jsonAnswerObject.has("opportune_noti_1")) {
                                if(!jsonAnswerObject.getString("opportune_noti_1").equals("無")){
                                    List<String> view_array = new ArrayList<String>(Arrays.asList(jsonAnswerObject.getString("opportune_noti_1").split("\n")));
                                    for (int j=0; j<diary_read_array.size(); j++){
                                        List<String> data_array = new ArrayList<String>(Arrays.asList(diary_read_array.get(j).split("\n")));
                                        if(data_array.get(0).equals(view_array.get(0))){
                                            op_no.add(data_array.get(4));//id
                                            op_no.add(data_array.get(0));//title
                                            op_no.add(data_array.get(1));//in time
                                            op_no.add(data_array.get(2));//situation
                                            op_no.add(data_array.get(3));//place
                                            break;
                                        }
                                    }
                                }
                            }
                            if (jsonAnswerObject.has("inopportune_read_1")) {
                                if(!jsonAnswerObject.getString("inopportune_read_1").equals("無")){
                                    List<String> view_array = new ArrayList<String>(Arrays.asList(jsonAnswerObject.getString("inopportune_read_1").split("\n")));
                                    //{news_title}\n{news_time}\n{news_situation}\n{news_place}#
                                    for (int j=0; j<diary_read_array.size(); j++){
                                        List<String> data_array = new ArrayList<String>(Arrays.asList(diary_read_array.get(j).split("\n")));
                                        //把標題分出來
                                        if(data_array.get(0).equals(view_array.get(0))){
                                            in_re.add(data_array.get(4));//id
                                            in_re.add(data_array.get(0));//title
                                            in_re.add(data_array.get(1));//in time
                                            in_re.add(data_array.get(2));//situation
                                            in_re.add(data_array.get(3));//place
                                            break;
                                        }
                                    }
                                }
                            }
                            if (jsonAnswerObject.has("opportune_read_1")) {
                                if(!jsonAnswerObject.getString("opportune_read_1").equals("無")){
                                    List<String> view_array = new ArrayList<String>(Arrays.asList(jsonAnswerObject.getString("opportune_read_1").split("\n")));
                                    for (int j=0; j<diary_read_array.size(); j++){
                                        List<String> data_array = new ArrayList<String>(Arrays.asList(diary_read_array.get(j).split("\n")));
                                        if(data_array.get(0).equals(view_array.get(0))){
                                            op_re.add(data_array.get(4));//id
                                            op_re.add(data_array.get(0));//title
                                            op_re.add(data_array.get(1));//in time
                                            op_re.add(data_array.get(2));//situation
                                            op_re.add(data_array.get(3));//place
                                            break;
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    //sqlite
                    Diary mydiary = new Diary();
                    mydiary.setKEY_DOC_ID(device_id + " " + diary_id);
                    mydiary.setKEY_SUBMIT_TIMESTAMP(current.getSeconds());
                    mydiary.setKEY_RESULT(result_json);
                    mydiary.setKEY_INOPPORTUNTE_RESULT_READ(String.join("#", in_re));
                    mydiary.setKEY_OPPORTUNTE_RESULT_READ(String.join("#", op_re));
                    DiaryDbHelper dbHandler = new DiaryDbHelper((Activity)v.getContext());
                    dbHandler.UpdatePushDiaryDetailsSubmit(mydiary);

                    rbRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    rbRef.update(
//                                            PUSH_DIARY_DONE, 1,
                                            PUSH_DIARY_SUBMIT_TIME, current,
                                            PUSH_DIARY_RESULT, result_json,
                                            PUSH_DIARY_INOPPORTUNE_TARGET_READ, in_re,
                                            PUSH_DIARY_OPPORTUNE_TARGET_RAED, op_re,
                                            PUSH_DIARY_INOPPORTUNE_TARGET_NOTI, in_no,
                                            PUSH_DIARY_OPPORTUNE_TARGET_NOTI, op_no
                                             )//another field
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
                }
//                Toast.makeText((Activity)v.getContext(), "Post Inserted Successfully", Toast.LENGTH_SHORT).show();
                ((Activity)v.getContext()).finish();

            }
        });

    }
}
