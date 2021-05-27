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
    String target_read_title_situation = "NA";
    String target_read_title_place = "NA";
    List<String> sample_read_Array = new ArrayList<>();

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
    String PUSH_ESM_TARGET_TITLE_DIARY = "target_read_title_diary";
    String PUSH_ESM_TARGET_IN_TIME = "target_in_time";
    String PUSH_ESM_TARGET_SITUATION = "target_situation";
    String PUSH_ESM_TARGET_PLACE = "target_place";


    String TEST_USER_COLLECTION = "test_users";
    String PUSH_ESM_COLLECTION = "push_esm";
    String PUSH_DIARY_COLLECTION = "push_diary";
    String PUSH_DIARY_DONE = "done";
    String PUSH_DIARY_SUBMIT_TIME = "submit_timestamp";
    String PUSH_DIARY_RESULT = "result";
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
                    DocumentReference docRef = db.collection("test_users").document(device_id).collection("push_esm").document(esm_id);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    if(document.get("ReadNewsTitle")!=null){
                                        sample_read_Array = (List<String>) document.get("ReadNewsTitle");
                                        if(!sample_read_Array.get(0).equals("zero_result")){
                                            try {
                                                assert result_json != null;
                                                JSONObject jsonRootObject = new JSONObject(result_json);
                                                JSONObject jsonAnswerObject = jsonRootObject.getJSONObject("answers");
                                                Iterator<String> keys = jsonAnswerObject.keys();
                                                while(keys.hasNext()) {
                                                    String key = keys.next();
                                                    try {
                                                        if(jsonAnswerObject.get(key).equals("有印象，且沒看過相同的新聞")){
                                                            List<String> add_what = new ArrayList<String>(Arrays.asList(key.split("_")));
                                                            target_read_title = sample_read_Array.get(Integer.parseInt(add_what.get(1)));
                                                            if (jsonAnswerObject.has("read_13")) {
                                                                target_read_title_situation = jsonAnswerObject.getString("read_13");
                                                            }
                                                            if (jsonAnswerObject.has("read_15")) {
                                                                target_read_title_place =  jsonAnswerObject.getString("read_15");
                                                            }
                                                            break;
                                                        }
                                                    } catch (JSONException e) {
                                                        // Something went wrong!
                                                    }
                                                }
                                                Log.d("lognewsselect", "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$finish");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            Log.d("lognewsselect", "document.get(\"ReadNewsTitle\")==zero_result");
                                        }
                                    } else {
                                        Log.d("lognewsselect", "document.get(\"ReadNewsTitle\")==null");
                                    }
                                } else {
                                    Log.d("lognewsselect", "No such document");
                                }
                            } else {
                                Log.d("lognewsselect", "get failed with ", task.getException());
                            }
                        }
                    });

                    final Timestamp current = Timestamp.now();
                    final DocumentReference rbRef = db.collection(TEST_USER_COLLECTION).document(device_id).collection(PUSH_ESM_COLLECTION).document(esm_id);
                    rbRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    int sample = 1;
                                    String diary_title = "";
                                    if(!target_read_title.equals("NA")){
                                        sample = 2;
                                        String TARGET_NEWS_TITLE = "TargetNewsTitleArray";
                                        String target_read_title_array_string = sharedPrefs.getString(TARGET_NEWS_TITLE, "");
                                        SharedPreferences.Editor editor = sharedPrefs.edit();
                                        if(target_read_title_array_string.equals("")){//no history
                                            editor.putString(TARGET_NEWS_TITLE, target_read_title);
                                        } else {
                                            editor.putString(TARGET_NEWS_TITLE, target_read_title_array_string + "#" + target_read_title );
                                        }
                                        List<String> tmp = new ArrayList<String>(Arrays.asList(target_read_title.split("¢")));
                                        target_read_title_in_time = tmp.get(4);
                                        diary_title = tmp.get(0);
                                    }

                                    rbRef.update(PUSH_ESM_SAMPLE, sample,
                                            PUSH_ESM_TARGET_TITLE_DIARY, diary_title,
                                            PUSH_ESM_TARGET_TITLE, target_read_title,
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
                    final DocumentReference rbRef = db.collection(TEST_USER_COLLECTION).document(device_id).collection(PUSH_DIARY_COLLECTION).document(diary_id);
                    rbRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    rbRef.update(PUSH_DIARY_DONE, 1,
                                            PUSH_DIARY_SUBMIT_TIME, current,
                                            PUSH_DIARY_RESULT, result_json)//another field
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
