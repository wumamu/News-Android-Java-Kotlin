package com.recoveryrecord.surveyandroid.viewholder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SubmitViewHolder extends RecyclerView.ViewHolder {

    private Button submitButton;
    Boolean is_esm = false, is_diary = false;
    String esm_id = "", diary_id = "", type = "";
    String result_json = "";
    String sample_title = "";
    String target_read_title = "NA";
    String target_read_current_title = "NA";
    String target_read_title_in_time = "NA";
    String target_read_title_id = "NA";
    String target_read_title_situation = "NA";
    String target_read_title_place = "NA";

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
    String PUSH_ESM_TARGET_TITLE = "target_read_title";
    String PUSH_ESM_TARGET_TITLE_DIARY = "target";
    String PUSH_ESM_TARGET_IN_TIME = "target_in_time";
    String PUSH_ESM_TARGET_NEWS_ID = "target_news_id";
    String PUSH_ESM_TARGET_SITUATION = "target_situation";
    String PUSH_ESM_TARGET_PLACE = "target_place";


    String TEST_USER_COLLECTION = "test_users";
    String PUSH_ESM_COLLECTION = "push_esm";
    String PUSH_DIARY_COLLECTION = "push_diary";
    String PUSH_DIARY_DONE = "done";
    String PUSH_DIARY_SUBMIT_TIME = "submit_timestamp";
    String PUSH_DIARY_RESULT = "result";
    String PUSH_DIARY_OPPORTUNE_TARGET = "opportune_target";
    String PUSH_DIARY_OPPORTUNE_TARGET_TITLE = "opportune_target_title";
    String PUSH_DIARY_OPPORTUNE_TARGET_NEWS_ID = "opportune_target_news_id";
    String PUSH_DIARY_INOPPORTUNE_TARGET = "inopportune_target";
    String PUSH_DIARY_INOPPORTUNE_TARGET_TITLE = "inopportune_target_title";
    String PUSH_DIARY_INOPPORTUNE_TARGET_NEWS_ID = "inopportune_target_news_id";

    String DIARY_READ_HISTORY_CANDIDATE = "DiaryTargetOptionArray";
    String ZERO_RESULT_STRING = "zero_result";

    String TO_DIARY_LIST = "DiaryList";

    String SAMPLE_ID = "sample_id";
    String SAMPLE_TITLE = "sample_title";
    String SAMPLE_MEDIA = "sample_media";
    String SAMPLE_RECEIEVE = "sample_receieve";
    String SAMPLE_IN = "sample_in";
    String EXIST_READ = "exist_read";//esm

    String EXIST_ESM = "exist_esm";//diary

    List<String> in_re = new ArrayList<>();
    List<String> op_re = new ArrayList<>();

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
                    Boolean exist_read = sharedPrefs.getBoolean(EXIST_READ, false);
//                    DocumentReference docRef = db.collection("push_esm").document(device_id + " " + esm_id);
                    if(exist_read){
                        try {
                            assert result_json != null;
                            JSONObject jsonRootObject = new JSONObject(result_json);
                            JSONObject jsonAnswerObject = jsonRootObject.getJSONObject("answers");
                            Iterator<String> keys = jsonAnswerObject.keys();
                            if (jsonAnswerObject.has("base_2")) {
                                if(jsonAnswerObject.getString("base_2").equals("我有點入閱讀")){
                                    target_read_title_id = sharedPrefs.getString(SAMPLE_ID, "NA");;
                                    target_read_title_in_time = sharedPrefs.getString(SAMPLE_IN, "NA");;
                                    target_read_title = sharedPrefs.getString(SAMPLE_TITLE, "NA");;

                                    if (jsonAnswerObject.has("read_seen_1")) {
                                        String answer = jsonAnswerObject.getString("read_seen_1");
                                        if(answer.equals("活動與地點皆相同")){
                                            if (jsonAnswerObject.has("recieve_moment_2")) {
                                                target_read_title_situation = jsonAnswerObject.getString("recieve_moment_2");
                                            }
                                            if (jsonAnswerObject.has("recieve_moment_6")) {
                                                target_read_title_place =  jsonAnswerObject.getString("recieve_moment_6");
                                            }
                                        } else if (answer.equals("活動相同，地點不同")){
                                            if (jsonAnswerObject.has("recieve_moment_2")) {
                                                target_read_title_situation = jsonAnswerObject.getString("recieve_moment_2");
                                            }
                                            if (jsonAnswerObject.has("read_seen_7")) {
                                                target_read_title_place =  jsonAnswerObject.getString("read_seen_7");
                                            }
                                        } else if (answer.equals("活動不同，地點相同")){
                                            if (jsonAnswerObject.has("read_seen_3")) {
                                                target_read_title_situation = jsonAnswerObject.getString("read_seen_3");
                                            }
                                            if (jsonAnswerObject.has("recieve_moment_6")) {
                                                target_read_title_place =  jsonAnswerObject.getString("recieve_moment_6");
                                            }
                                        } else { //活動、地點均不相同
                                            if (jsonAnswerObject.has("read_seen_3")) {
                                                target_read_title_situation = jsonAnswerObject.getString("read_seen_3");
                                            }
                                            if (jsonAnswerObject.has("read_seen_7")) {
                                                target_read_title_place =  jsonAnswerObject.getString("read_seen_7");
                                            }
                                        }
                                    }
                                    if (jsonAnswerObject.has("read_moment_2")) {
                                        String answer = jsonAnswerObject.getString("read_moment_2");
                                        if(answer.equals("活動與地點皆相同")){
                                            //do nothing
                                        } else if (answer.equals("活動相同，地點不同")){
                                            if (jsonAnswerObject.has("read_seen_7")) {
                                                target_read_title_place =  jsonAnswerObject.getString("read_seen_7");
                                            }
                                        } else if (answer.equals("活動不同，地點相同")){
                                            if (jsonAnswerObject.has("read_seen_3")) {
                                                target_read_title_situation = jsonAnswerObject.getString("read_seen_3");
                                            }
                                        } else { //活動、地點均不相同
                                            if (jsonAnswerObject.has("read_seen_3")) {
                                                target_read_title_situation = jsonAnswerObject.getString("read_seen_3");
                                            }
                                            if (jsonAnswerObject.has("read_seen_7")) {
                                                target_read_title_place =  jsonAnswerObject.getString("read_seen_7");
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    //result without title so we have to trace
//                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                            if (task.isSuccessful()) {
//                                DocumentSnapshot document = task.getResult();
//                                if (document.exists()) {
//                                    if(document.get("ExistReadSample")!=null){
//                                        sample_read_Array = (List<String>) document.get("ReadNewsTitle");
////                                        if(!sample_read_Array.get(0).equals("zero_result")){
//                                        if(document.getBoolean("ExistReadSample")){
//                                            try {
//                                                assert result_json != null;
//                                                JSONObject jsonRootObject = new JSONObject(result_json);
//                                                JSONObject jsonAnswerObject = jsonRootObject.getJSONObject("answers");
//                                                Iterator<String> keys = jsonAnswerObject.keys();
//                                                while(keys.hasNext()) {
//                                                    String key = keys.next();
//                                                    try {
//                                                        if(jsonAnswerObject.get(key).equals("有點入閱讀，且沒看過相同的新聞")){
//                                                            List<String> add_what = new ArrayList<String>(Arrays.asList(key.split("_")));
//                                                            target_read_title = sample_read_Array.get(Integer.parseInt(add_what.get(1)));
//                                                            if (jsonAnswerObject.has("read_15")) {
//                                                                target_read_title_situation = jsonAnswerObject.getString("read_15");
//                                                            }
//                                                            if (jsonAnswerObject.has("read_17")) {
//                                                                target_read_title_place =  jsonAnswerObject.getString("read_17");
//                                                            }
//                                                            break;
//                                                        }
//                                                    } catch (JSONException e) {
//                                                        // Something went wrong!
//                                                    }
//                                                }
//                                                Log.d("lognewsselect", "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$finish");
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                        } else {
//                                            Log.d("lognewsselect", "document.get(\"ReadNewsTitle\")==zero_result");
//                                        }
//                                    } else {
//                                        Log.d("lognewsselect", "document.get(\"ReadNewsTitle\")==null");
//                                    }
//                                } else {
//                                    Log.d("lognewsselect", "No such document");
//                                }
//                            } else {
//                                Log.d("lognewsselect", "get failed with ", task.getException());
//                            }
//                        }
//                    });


                    final Timestamp current = Timestamp.now();
                    final DocumentReference rbRef = db.collection(PUSH_ESM_COLLECTION).document(device_id + " " + esm_id);
                    rbRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
//                                    int sample = 1;
//                                    String diary_title = "NA";
//                                    if(!target_read_title.equals("NA")){
//                                        sample = 2;
//                                        List<String> tmp = new ArrayList<String>(Arrays.asList(target_read_title.split("¢")));
//                                        target_read_title_in_time = tmp.get(4);
//                                        target_read_title_id = tmp.get(5);
//                                        diary_title = tmp.get(0);
//                                    }

                                    rbRef.update(
//                                            PUSH_ESM_SAMPLE, sample,
//                                            PUSH_ESM_TARGET_TITLE_DIARY, target_read_title,
                                            PUSH_ESM_TARGET_TITLE,  target_read_title,
                                            PUSH_ESM_TARGET_NEWS_ID, target_read_title_id,
                                            PUSH_ESM_TARGET_IN_TIME, target_read_title_in_time,
                                            PUSH_ESM_TARGET_SITUATION, target_read_title_situation,
                                            PUSH_ESM_TARGET_PLACE, target_read_title_place,
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
                    myesm.setKEY_NOTI_READ_NEWS_ID(target_read_title_id);
                    myesm.setKEY_NOTI_READ_TITLE(target_read_title);
                    myesm.setKEY_NOTI_READ_IN_TIME(target_read_title_in_time);
//                    myesm.setKEY_NOTI_READ_RECEIEVE_TIME(device_id + " " + esm_id);
                    myesm.setKEY_NOTI_READ_SITUATION(target_read_title_situation);
                    myesm.setKEY_NOTI_READ_PLACE(target_read_title_place);
//                    myesm.setKEY_NOTI_NOT_READ_NEWS_ID(device_id + " " + esm_id);
//                    myesm.setKEY_NOTI_NOT_READ_TITLE(device_id + " " + esm_id);
//                    myesm.setKEY_NOTI_NOT_READ_RECEIEVE_TIME(device_id + " " + esm_id);

//                    myesm.setKEY_SELF_READ_NEWS_ID(device_id + " " + esm_id);
//                    myesm.setKEY_SELF_READ_TITLE(device_id + " " + esm_id);
//                    myesm.setKEY_SELF_READ_IN_TIME(device_id + " " + esm_id);
//                    myesm.setKEY_SELF_READ_RECEIEVE_TIME(device_id + " " + esm_id);
//                    myesm.setKEY_SELF_READ_SITUATION(target_read_title_situation);
//                    myesm.setKEY_SELF_READ_PLACE(device_id + " " + esm_id);


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
                    String diary_option_string_list = sharedPrefs.getString(DIARY_READ_HISTORY_CANDIDATE, ZERO_RESULT_STRING);
                    List<String> diary_option_array = new ArrayList<String>(Arrays.asList(diary_option_string_list.split("#")));
                    //DiaryLoadingPage
                    //{news_title}\n{news_time}\n{news_situation}\n{news_place}\n{news_id}#

                    in_re.clear();
                    op_re.clear();
                    if(exist_esm){
                        try {
                            assert result_json != null;
                            JSONObject jsonRootObject = new JSONObject(result_json);
                            JSONObject jsonAnswerObject = jsonRootObject.getJSONObject("answers");
                            if (jsonAnswerObject.has("inopportune_1")) {
                                if(!jsonAnswerObject.getString("inopportune_1").equals("無")){
                                    List<String> view_array = new ArrayList<String>(Arrays.asList(jsonAnswerObject.getString("inopportune_1").split("\n")));
                                    //{news_title}\n{news_time}\n{news_situation}\n{news_place}#
                                    for (int j=0; j<diary_option_array.size(); j++){
                                        List<String> data_array = new ArrayList<String>(Arrays.asList(diary_option_array.get(j).split("\n")));
                                        //把標題分出來
                                        if(data_array.get(0).equals(view_array.get(0))){
                                            in_re.add(data_array.get(4));//id
                                            in_re.add(data_array.get(0));//title
                                            in_re.add(data_array.get(1));//in time
                                            in_re.add(data_array.get(2));//situation
                                            in_re.add(data_array.get(3));//place

//                                            target_inopportune_news_id = data_array.get(4);
//                                            target_inopportune_title = data_array.get(0);
//                                            target_inopportune = diary_option_array.get(j);
                                            break;
                                        }
                                    }
                                }

                            }
                            if (jsonAnswerObject.has("opportune_1")) {
                                if(!jsonAnswerObject.getString("opportune_1").equals("無")){
                                    List<String> view_array = new ArrayList<String>(Arrays.asList(jsonAnswerObject.getString("opportune_1").split("\n")));
                                    for (int j=0; j<diary_option_array.size(); j++){
                                        List<String> data_array = new ArrayList<String>(Arrays.asList(diary_option_array.get(j).split("\n")));
                                        if(data_array.get(0).equals(view_array.get(0))){
                                            op_re.add(data_array.get(4));//id
                                            op_re.add(data_array.get(0));//title
                                            op_re.add(data_array.get(1));//in time
                                            op_re.add(data_array.get(2));//situation
                                            op_re.add(data_array.get(3));//place
//                                            target_opportune_news_id = data_array.get(4);
//                                            target_opportune_title = data_array.get(0);
//                                            target_opportune = diary_option_array.get(j);
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
                    mydiary.setKEY_INOPPORTUNTE_RESULT(String.join("#", in_re));
                    mydiary.setKEY_OPPORTUNTE_RESULT(String.join("#", op_re));
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
                                            PUSH_DIARY_INOPPORTUNE_TARGET, in_re,
//                                            PUSH_DIARY_INOPPORTUNE_TARGET_TITLE, target_inopportune_title,
//                                            PUSH_DIARY_INOPPORTUNE_TARGET_NEWS_ID, target_inopportune_news_id,
                                            PUSH_DIARY_OPPORTUNE_TARGET, op_re
//                                            PUSH_DIARY_OPPORTUNE_TARGET_TITLE, target_opportune_title,
//                                            PUSH_DIARY_OPPORTUNE_TARGET_NEWS_ID, target_opportune_news_id
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
