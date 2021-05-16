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
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
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

import static com.recoveryrecord.surveyandroid.example.Constants.ESM_TARGET_RANGE;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_TARGET_RANGE;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_UNCLICKED_CANDIDATE;
import static com.recoveryrecord.surveyandroid.example.Constants.READ_HISTORY_CANDIDATE;

public class LoadingPageActivity extends AppCompatActivity {
    private Button button;
    String output_file_name = "2.json";
    Task task, task2;
    Boolean finish = false;
    String esm_id = "";
    List<String> news_title_target_array = new ArrayList<>();
    List<String> notification_unclick_array = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_page);
        if (getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();
            esm_id = Objects.requireNonNull(b.getString("esm_id"));
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
        intent_esm.setClass(this, ESMActivity.class);
        intent_esm.putExtra("esm_id", esm_id);//******************
        intent_esm.putExtra("type", "esm");
        Log.d("lognewsselect", "openESM" + esm_id);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> esm = new HashMap<>();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String ReadNewsTitle = sharedPrefs.getString(READ_HISTORY_CANDIDATE, "zero_result");
        String NotiNewTitle = sharedPrefs.getString(NOTIFICATION_UNCLICKED_CANDIDATE, "zero_result");
        final List<String> noti_news_title_array_json = new ArrayList<String>(Arrays.asList(NotiNewTitle.split("#")));
        final List<String> read_news_title_array_json = new ArrayList<String>(Arrays.asList(ReadNewsTitle.split("#")));
        @SuppressLint("HardwareIds")
        String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        if (!esm_id.equals("")){
            final DocumentReference rbRef = db.collection("test_users").document(device_id).collection("push_esm").document(esm_id);
            rbRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        assert document != null;
                        if (document.exists()) {
                            rbRef.update( "ReadNewsTitle", read_news_title_array_json, "NotiNewTitle", noti_news_title_array_json)//another field
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
        esm_id = "";
        startActivity(intent_esm);
    }

    private void select_news_title_candidate() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        @SuppressLint("HardwareIds")
        final String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String[] SelectedNewsTitle = {""};
        final Long now = System.currentTimeMillis();
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(now);
        task =     db.collection("test_users")
                .document(device_id)
                .collection("reading_behaviors")
                .whereEqualTo("select", false)
                .orderBy("out_timestamp", Query.Direction.DESCENDING)
                .get();
        task2 =     db.collection("test_users")
                .document(device_id)
                .collection("push_news")
                .whereEqualTo("click", 0)
                .orderBy("noti_timestamp", Query.Direction.DESCENDING)
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
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        if(!(d.getString("title") ==null) && !d.getString("title").equals("NA")){
                            if (d.getDouble("time_on_page(s)")>=5) {
                                if (Math.abs(now/1000 - d.getTimestamp("in_timestamp").getSeconds()) <= ESM_TARGET_RANGE) {
                                    news_title_target_array.add(d.getString("title"));
                                    Log.d("lognewsselect", "task1 " + d.getString("title"));
                                }
                            }
                        }

                        //mark as check
                        db.collection("test_users")
                                .document(device_id)
                                .collection("reading_behaviors")
                                .document(d.getId())
                                .update("select", true)
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
//                            Random r=new Random();
//                            int randomNumber=r.nextInt(news_title_array.size());
//                            SelectedNewsTitle[0] = news_title_array.get(randomNumber);
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putString(READ_HISTORY_CANDIDATE, title_array);
                    editor.apply();
                    Log.d("lognewsselect", "@@@@@@@@@@@@@");
                } else {
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putString(READ_HISTORY_CANDIDATE, "zero_result");
                    editor.apply();
                }
                Log.d("lognewsselect", "**********COMPLETE TASK1");
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

        task2.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        if (Math.abs(now/1000 - d.getTimestamp("noti_timestamp").getSeconds()) <= NOTIFICATION_TARGET_RANGE) {
                            notification_unclick_array.add(d.getString("title"));
                            Log.d("lognewsselect", "task2 " + d.getString("title"));
                        }
                        //mark as check
                        db.collection("test_users")
                                .document(device_id)
                                .collection("push_news")
                                .document(d.getId())
                                .update("click", 4)
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
                    editor.putString(NOTIFICATION_UNCLICKED_CANDIDATE, notification_unclick_string);
                    editor.apply();
                } else {
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putString(NOTIFICATION_UNCLICKED_CANDIDATE, "zero_result");
                    editor.apply();
                }
                Log.d("lognewsselect", "**********COMPLETE TASK2");
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