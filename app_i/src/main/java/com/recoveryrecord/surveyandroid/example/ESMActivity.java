package com.recoveryrecord.surveyandroid.example;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.recoveryrecord.surveyandroid.Answer;
import com.recoveryrecord.surveyandroid.R;
import com.recoveryrecord.surveyandroid.condition.CustomConditionHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ESMActivity extends com.recoveryrecord.surveyandroid.SurveyActivity implements CustomConditionHandler {
    String esm_id = "";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final Timestamp current_now = Timestamp.now();
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        final String time_now = formatter.format(date);
        //new
        Map<String, Object> esm = new HashMap<>();
        esm.put("open_time", time_now);
        esm.put("open_timestamp", current_now);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        esm_id = String.valueOf(date);
        if (getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();
            esm_id = Objects.requireNonNull(b.getString("esm_id"));
//            Log.d("logesm", Objects.requireNonNull(b.getString("trigger_from")));
//            Log.d("logesm", String.valueOf(Objects.requireNonNull(b.getInt("esm_id"))));
//            Log.d("logesm", String.valueOf(Objects.requireNonNull(b.get("noti_timestamp"))));
            Log.d("logesm", String.valueOf(123));
//            b.putString("esm_id", esm_id);
//            esm_id = b.getString("trigger_from");
        }
        //temp

        if (esm_id!=""){
//            db.collection("test_users")
//                    .document(device_id)
//                    .collection("esms")
//                    .document(esm_id)
//                    .set(esm);
            final DocumentReference rbRef = db.collection("test_users").document(device_id).collection("esms").document(esm_id);
            rbRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            rbRef.update("open_time", time_now, "open_timestamp", current_now)//another field
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
            Log.d("logesm", "in yes esm_id");
        } else {
            db.collection("test_users")
                    .document(device_id)
                    .collection("esms")
                    .document(time_now)
                    .set(esm);
            Log.d("logesm", "in no esm_id");
        }

    }
    //add survey activity
    //seems useless now
    @Override
    protected String getSurveyId() {
        return "123123";
    }

    @Override
    protected String getSurveyTitle() {
        return "ESM";
    }

    @Override
    protected String getJsonFilename() {
//        if (getIntent().getExtras() != null) {
//            Bundle b = getIntent().getExtras();
//            return b.getString("json_file_name");
//        }
//        return "ExampleQuestions.json";
        return "ESM.json";
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
            Integer wiggleRoom = Integer.valueOf(extra.get("wiggle_room"));
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
                    .setNeutralButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ESMActivity.super.onBackPressed();
                        }
            }).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final Timestamp current_end = Timestamp.now();
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        final String time_now = formatter.format(date);
        if (esm_id!="") {
            String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            final DocumentReference rbRef = db.collection("test_users").document(device_id).collection("esms").document(esm_id);
            rbRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            rbRef.update("close_timestamp", current_end, "close_time", time_now)//another field
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
            Log.d("logesm", "out yes esm_id");
        } else {
            Map<String, Object> esm = new HashMap<>();
            esm.put("close_timestamp", current_end);
            esm.put("close_time", time_now);
            String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            db.collection("test_users")
                    .document(device_id)
                    .collection("esms")
                    .document(time_now)
                    .set(esm);
            Log.d("logesm", "out no esm_id");
        }

    }
}
