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

    String result_json = "";
    String sample_title = "";
    String target_read_title = "NA";
    List<String> sample_read_Array = new ArrayList<>();

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
                String esm_id = "";
                if (((Activity)v.getContext()).getIntent().getExtras() != null) {
                    Bundle b = ((Activity)v.getContext()).getIntent().getExtras();
                    esm_id = Objects.requireNonNull(b.getString("esm_id"));
//                    Log.d("logesm", String.valueOf(456));
                }
                String ESM_DONE_TOTAL = "ESMDoneTotal";
                String ESM_DAY_DONE_PREFIX = "ESMDayDone_";
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences((Activity)v.getContext());
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Calendar calendar = Calendar.getInstance();
                int day_index = calendar.get(Calendar.DAY_OF_YEAR);
                int esm_done = sharedPrefs.getInt(ESM_DONE_TOTAL, 0);
                int esm_day_done = sharedPrefs.getInt(ESM_DAY_DONE_PREFIX+ day_index, 0);
                editor.putInt(ESM_DONE_TOTAL, esm_done+1);
                editor.putInt(ESM_DAY_DONE_PREFIX + day_index, esm_day_done+1);
                editor.apply();
                result_json = answerProvider.allAnswersJson();
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
                final DocumentReference rbRef = db.collection("test_users").document(device_id).collection("push_esm").document(esm_id);
                rbRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                int sample = 1;
                                if(!target_read_title.equals("NA")){
                                    sample = 2;
                                }
                                rbRef.update("sample", sample,
                                        "target_read_title", target_read_title,
                                        "submit_timestamp", current,
                                        "result", result_json)//another field
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
                Toast.makeText((Activity)v.getContext(), "Post Inserted Successfully", Toast.LENGTH_SHORT).show();
                ((Activity)v.getContext()).finish();

            }
        });

    }
}
