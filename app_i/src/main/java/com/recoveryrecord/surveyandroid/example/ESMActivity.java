package com.recoveryrecord.surveyandroid.example;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceManager;

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
import com.recoveryrecord.surveyandroid.Answer;
import com.recoveryrecord.surveyandroid.R;
import com.recoveryrecord.surveyandroid.condition.CustomConditionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.recoveryrecord.surveyandroid.example.Constants.ESM_TARGET_RANGE;
//import static com.recoveryrecord.surveyandroid.example.Constants.JSON_TEMPLATE;

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
            Log.d("logesm", String.valueOf(123));
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
        String file_name = "1.json";
        if (getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();
            if (b.getString("type").equals("esm")){

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        select_news_title_candidate();
                    }});

                t.start(); // spawn thread
                try {
                    t.join();  // wait for thread to finish
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
                String LastSelectedNewsTitle = sharedPrefs.getString("LastSelectedNewsTitle", "zero_result");
                Log.d("lognewsselect", "LastSelectedNewsTitle " + LastSelectedNewsTitle);
                if(!LastSelectedNewsTitle.equals("zero_result")){
                    Log.d("lognewsselect", "exist rb");
                    try {
                        file_name = generate_esm_json(LastSelectedNewsTitle, "2.json");
                    } catch (JSONException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    return file_name;
                } else {
                    Log.d("lognewsselect", "zero_result here");
                    try {
                        file_name = generate_esm_json(LastSelectedNewsTitle, "1.json");
                    } catch (JSONException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    return file_name;
                }
            } else if (b.getString("type").equals("diary")){
                try {
                    file_name = generate_esm_json("LastSelectedNewsTitle", "1.json");
                } catch (JSONException | InterruptedException e) {
                    e.printStackTrace();
                }
                return file_name;
            } else {
                try {
                    file_name = generate_esm_json("LastSelectedNewsTitle", "1.json");
                } catch (JSONException | InterruptedException e) {
                    e.printStackTrace();
                }
                return file_name;
            }
//            Bundle b = getIntent().getExtras();
//            Log.d("lognewsselect", "getJsonFilename() " + b.getString("json_file_name"));
//            return b.getString("json_file_name");
        }
        return "ExampleQuestions.json";
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

    private void select_news_title_candidate() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        @SuppressLint("HardwareIds")
        final String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String[] SelectedNewsTitle = {""};
        final Long now = System.currentTimeMillis();
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(now);
        db.collection("test_users")
                .document(device_id)
                .collection("reading_behaviors")
                .whereEqualTo("select", false)
//                .whereGreaterThanOrEqualTo("time_on_page(s)", 5)
                .orderBy("out_timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<String> news_title_array = new ArrayList<>();
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                if(!(d.getString("title") ==null) && !d.getString("title").equals("NA")){
                                    if (d.getDouble("time_on_page(s)")>=5) {
                                        if (Math.abs(now/1000 - d.getTimestamp("in_timestamp").getSeconds()) <= ESM_TARGET_RANGE) {
                                            news_title_array.add(d.getString("title"));
                                        }
                                    }
                                }
                                Log.d("lognewsselect", "title list select_news() " + d.getString("title"));
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
                        if(news_title_array.size()!=0){
                            String title_array = "";
                            for(int i=0; i<news_title_array.size(); i++){
                                title_array+=news_title_array.get(i) + "#";
                            }
//                            Random r=new Random();
//                            int randomNumber=r.nextInt(news_title_array.size());
//                            SelectedNewsTitle[0] = news_title_array.get(randomNumber);
                            SharedPreferences.Editor editor = sharedPrefs.edit();
//                            editor.putInt("FoundNewsTitle", news_title_array.size());
                            editor.putString("LastSelectedNewsTitle", title_array);
                            editor.apply();
                            String LastSelectedNewsTitle = sharedPrefs.getString("LastSelectedNewsTitle", "zero_result");
                            Log.d("lognewsselect", "@@@@@@@@@@@@@LastSelectedNewsTitle " + LastSelectedNewsTitle);
                        } else {
                            SharedPreferences.Editor editor = sharedPrefs.edit();
                            editor.putString("LastSelectedNewsTitle", "zero_result");
                            editor.apply();
                            String LastSelectedNewsTitle = sharedPrefs.getString("LastSelectedNewsTitle", "zero_result");
                            Log.d("lognewsselect", "@@@@@@@@@@@@@LastSelectedNewsTitle " + LastSelectedNewsTitle);
                        }
//                        return "123";
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

        Log.d("lognewsselect", "select news finish################################# ");
    }

    private String generate_esm_json(String news_title_array_string, String file_name) throws JSONException, InterruptedException {
//        final FirebaseFirestore db = FirebaseFirestore.getInstance();
//        @SuppressLint("HardwareIds")
//        final String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//        final String[] SelectedNewsTitle = {""};
//        final Long now = System.currentTimeMillis();
//        final Calendar c = Calendar.getInstance();
//        c.setTimeInMillis(now);
//        db.collection("test_users")
//                .document(device_id)
//                .collection("reading_behaviors")
//                .whereEqualTo("select", false)
////                .whereGreaterThanOrEqualTo("time_on_page(s)", 5)
//                .orderBy("out_timestamp", Query.Direction.DESCENDING)
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        List<String> news_title_array = new ArrayList<>();
//                        if (!queryDocumentSnapshots.isEmpty()) {
//                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
//                            for (DocumentSnapshot d : list) {
//                                if(!(d.getString("title") ==null) && !d.getString("title").equals("NA")){
//                                    if (d.getDouble("time_on_page(s)")>=5) {
//                                        if (Math.abs(now/1000 - d.getTimestamp("in_timestamp").getSeconds()) <= ESM_TARGET_RANGE) {
//                                            news_title_array.add(d.getString("title"));
////                                            Log.d("lognewsselect", String.valueOf(d.getTimestamp("in_timestamp")));
//                                        }
//                                    }
//                                }
//                                Log.d("lognewsselect", "title list select_news() " + d.getString("title"));
//                                //mark as check
//                                db.collection("test_users")
//                                        .document(device_id)
//                                        .collection("reading_behaviors")
//                                        .document(d.getId())
//                                        .update("select", true)
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//                                                Log.d("lognewsselect", "DocumentSnapshot successfully updated!");
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                Log.w("lognewsselect", "Error updating document", e);
//                                            }
//                                        });
//                            }
//                        }
//                        if(news_title_array.size()!=0){
//                            String title_array = "";
//                            for(int i=0; i<news_title_array.size(); i++){
//                                title_array+=news_title_array.get(i) + "#";
//                            }
////                            Random r=new Random();
////                            int randomNumber=r.nextInt(news_title_array.size());
////                            SelectedNewsTitle[0] = news_title_array.get(randomNumber);
//                            SharedPreferences.Editor editor = sharedPrefs.edit();
////                            editor.putInt("FoundNewsTitle", news_title_array.size());
//                            editor.putString("LastSelectedNewsTitle", title_array);
//                            editor.apply();
//                            String LastSelectedNewsTitle = sharedPrefs.getString("LastSelectedNewsTitle", "zero_result");
//                            Log.d("lognewsselect", "@@@@@@@@@@@@@LastSelectedNewsTitle " + LastSelectedNewsTitle);
//                        } else {
//                            SharedPreferences.Editor editor = sharedPrefs.edit();
//                            editor.putString("LastSelectedNewsTitle", "zero_result");
//                            editor.apply();
////                            Log.d("lognewsselect", "news_title_array.size()==0");
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("lognewsselect", String.valueOf(e));
//                // if we do not get any data or any error we are displaying
//                // a toast message that we do not get any data
////                Toast.makeText(TestNewsOneActivity.this, "Fail to get the data.", Toast.LENGTH_SHORT).show();
//            }
//        });
//        Log.d("lognewsselect", "select news finish%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
//        String LastSelectedNewsTitle = sharedPrefs.getString("LastSelectedNewsTitle", "zero_result");
//        Log.d("lognewsselect", "LastSelectedNewsTitle " + LastSelectedNewsTitle);
//        if (LastSelectedNewsTitle.equals("zero_result")){
//            file_name = "1.json";
//        } else {
//            file_name = "2.json";
//        }

        Log.d("lognewsselect", "generate_json START %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        if(file_name.equals("1.json")){
            JSONObject jsonRootObject = new JSONObject(loadJSONFromAsset(file_name));
            File file;
            file = new File(getFilesDir(),"test.json");
            try{
                FileOutputStream fos = new FileOutputStream(file);//创建一个文件输出流
                fos.write(jsonRootObject.toString().getBytes());//将生成的JSON数据写出
                fos.close();//关闭输出流
                Toast.makeText(getApplicationContext(),"创建成功無閱讀紀錄！",Toast.LENGTH_SHORT).show();
                Log.d("lognewsselect", "!!!!!!!!!!!!!!!!!!!()" + file.getAbsolutePath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file.getAbsolutePath();
        } else {
            List<String> news_title_array_json = new ArrayList<String>(Arrays.asList(news_title_array_string.split("#")));
            Log.d("lognewsselect", "news_title_array " + news_title_array_json);
            Log.d("lognewsselect", "news_title_array size " + news_title_array_json.size());
            List<String> notifications = new ArrayList<>();
            notifications.add("標題一");
            notifications.add("標題二");
            notifications.add("標題三");
            notifications.add("沒有印象");
            JSONArray notifications_arr = new JSONArray();
            notifications_arr.put("1");
            notifications_arr.put("2");
            notifications_arr.put("3");
            notifications_arr.put("4");
            notifications_arr.put("沒有印象");
            String template_file_name = "1.json";
            int title_count = news_title_array_json.size();
            if (news_title_array_json.size()==1){
                template_file_name = "1.json";
            } else {
                template_file_name = "2.json";
            }
            //then use two template
            JSONObject jsonRootObject = new JSONObject(loadJSONFromAsset(file_name));
            JSONArray jsonQuestionObject = jsonRootObject.optJSONArray("questions");
            JSONArray addedArray = new JSONArray();
            if(title_count>2){
                //we have to add more
                String strJsons="    {\n" +
                        "      \"id\": \"add_2\",\n" +
                        "      \"header\": \"Question add_2(2_2)\",\n" +
                        "      \"question\": \"您對於「某某news title」這篇新聞，是否曾有印象閱讀過，且未看過其它媒體或平台「引用相同新聞稿，闡述字句幾乎相同」的新聞？\",\n" +
                        "      \"question_type\": \"single_select\",\n" +
                        "      \"options\": [\n" +
                        "        \"有印象，且沒看過相同的新聞\",\n" +
                        "        \"沒有印象或有看過相同的新聞\"\n" +
                        "      ],\n" +
                        "      \"show_if\": {\n" +
                        "        \"operation\": \"and\",\n" +
                        "        \"subconditions\": [\n" +
                        "          {\n" +
                        "            \"id\": \"add_0\",\n" +
                        "            \"operation\": \"equals\",\n" +
                        "            \"value\": \"沒有印象或有看過相同的新聞\"\n" +
                        "          },\n" +
                        "          {\n" +
                        "            \"id\": \"base_1\",\n" +
                        "            \"operation\": \"equals\",\n" +
                        "            \"value\": \"有\"\n" +
                        "          }\n" +
                        "        ]\n" +
                        "      }\n" +
                        "    }";
                for(int i = 2; i<title_count ; i++){
                    JSONObject tmp_jsonObject = new JSONObject(strJsons);
                    tmp_jsonObject.putOpt("id", "add_" + i);
                    tmp_jsonObject.putOpt("question", "您對於「" + news_title_array_json.get(i) + "」這篇新聞，是否曾有印象閱讀過，且未看過其它媒體或平台「引用相同新聞稿，闡述字句幾乎相同」的新聞？");
                    tmp_jsonObject.putOpt("header", "add_" + i);
                    JSONArray n_condi_for_ques = new JSONArray();
                    JSONObject n_condi_single = new JSONObject();
                    for(int j=0; j<i-1;j++){
                        n_condi_single.putOpt("id", "add_" + j);
                        n_condi_single.putOpt("operation", "equals");
                        n_condi_single.putOpt("value", "沒有印象或有看過相同的新聞");
                        n_condi_for_ques.put(n_condi_single);
                        n_condi_single = new JSONObject();
                    }
                    n_condi_single.putOpt("id", "base_1");
                    n_condi_single.putOpt("operation", "equals");
                    n_condi_single.putOpt("value", "有");
                    n_condi_for_ques.put(n_condi_single);
                    tmp_jsonObject.optJSONObject("show_if").putOpt("subconditions", n_condi_for_ques);
                    Log.d("lognewsselect", "new object " + i + " " + tmp_jsonObject);

                    jsonQuestionObject.put(tmp_jsonObject);//origin
                    addedArray.put(tmp_jsonObject);//append
                }

            }




            for (int i =0 ; i < jsonQuestionObject.length();i++){
                JSONObject one = jsonQuestionObject.getJSONObject(i);
                //add0
                if(title_count>=1 && one.optString("id").equals("add_0")){
                    one.putOpt("id", "add_0");
                    one.putOpt("question", "您對於「"+ news_title_array_json.get(0) +"」這篇新聞，是否曾有印象閱讀過，且未看過其它媒體或平台「引用相同新聞稿，闡述字句幾乎相同」的新聞？");
                    one.putOpt("header", "add_0");
                    continue;
                }
                //add1
                if(title_count>=2 && one.optString("id").equals("add_1")){
                    one.putOpt("id", "add_1");
                    one.putOpt("question", "您對於「"+ news_title_array_json.get(1) +"」這篇新聞，是否曾有印象閱讀過，且未看過其它媒體或平台「引用相同新聞稿，闡述字句幾乎相同」的新聞？");
                    one.putOpt("header", "add_1");
                    Log.d("lognewsselect", "old for title add1" + one);
                    JSONArray condi_for_ques = new JSONArray();
                    JSONObject condi_single = new JSONObject();
                    for(int j=0; j<1;j++){
                        condi_single.putOpt("id", "add_" + j);
                        condi_single.putOpt("operation", "equals");
                        condi_single.putOpt("value", "沒有印象或有看過相同的新聞");
                        condi_for_ques.put(condi_single);
                        condi_single = new JSONObject();
                    }
                    condi_single.putOpt("id", "base_1");
                    condi_single.putOpt("operation", "equals");
                    condi_single.putOpt("value", "有");
                    condi_for_ques.put(condi_single);
                    one.optJSONObject("show_if").putOpt("subconditions", condi_for_ques);
                    Log.d("lognewsselect", "new for title add1" + one);

                    if (title_count>2){
                        //move back one or more position
                        int difference = title_count-2;
                        for (int k = jsonQuestionObject.length()-1; k-difference>i; k--) {
                            jsonQuestionObject.put(k, jsonQuestionObject.get(k-difference));
                        }
                        int insert_pos = i+1;
                        //insert in the middle
                        for(int p = 0; p<addedArray.length();p++){
                            jsonQuestionObject.put(insert_pos, addedArray.get(p));
                            insert_pos++;
                        }
                    }
                    continue;
                }
//                //add2
//                if(one.optString("id").equals("add_2")){
//                    one.putOpt("id", "add_2");
//                    one.putOpt("question", "您對於「2某某news title」這篇新聞，是否曾有印象閱讀過，且未看過其它媒體或平台「引用相同新聞稿，闡述字句幾乎相同」的新聞？");
//                    one.putOpt("header", "add_2");
//                    Log.d("lognewsselect", "old for title add2" + one);
//                    JSONArray condi_for_ques = new JSONArray();
//                    JSONObject condi_single = new JSONObject();
//                    for(int j=0; j<2;j++){
//                        condi_single.putOpt("id", "add_" + j);
//                        condi_single.putOpt("operation", "equals");
//                        condi_single.putOpt("value", "沒有印象或有看過相同的新聞");
//                        condi_for_ques.put(condi_single);
//                        condi_single = new JSONObject();
//                    }
//                    condi_single.putOpt("id", "base_1");
//                    condi_single.putOpt("operation", "equals");
//                    condi_single.putOpt("value", "有");
//                    condi_for_ques.put(condi_single);
//                    one.optJSONObject("show_if").putOpt("subconditions", condi_for_ques);
//                    Log.d("lognewsselect", "new for title add2" + one);
//                    continue;
//                }
                //for notification unclick 24
                if(one.optString("id").equals("read_title_1")){
                    one.putOpt("options", notifications_arr);
                    continue;
                }
                //for notification unclick 46
                if(one.optString("id").equals("46")){
                    one.putOpt("options", notifications_arr);
                    continue;
                }
                //for 0
                if(one.optString("id").equals("read_0")){
                    Log.d("lognewsselect", "old for title 0" + one);
                    JSONArray my = new JSONArray();
                    JSONObject tt = new JSONObject();
                    for(int j=0; j<title_count;j++){
                        tt.putOpt("id", "add_" + j);
                        tt.putOpt("operation", "equals");
                        tt.putOpt("value", "有印象，且沒看過相同的新聞");
                        my.put(tt);
                        tt = new JSONObject();
                    }
                    one.optJSONObject("show_if").putOpt("subconditions", my);
                    Log.d("lognewsselect", "new for title 0" + one);
//                    one.optJSONObject("show_if").optJSONObject("subconditions");
                    continue;
                }
                //for title 45
                if(one.optString("id").equals("forget_or_seen_0")){
//                    JSONArray tmp = one.optJSONObject("show_if").getJSONArray("subconditions");
                    Log.d("lognewsselect", "old for title 45" + one);
                    JSONArray my = new JSONArray();
                    JSONObject tt = new JSONObject();
                    for(int j=0; j<title_count;j++){
                        tt.putOpt("id", "add_" + j);
                        tt.putOpt("operation", "equals");
                        tt.putOpt("value", "沒有印象或有看過相同的新聞");
                        my.put(tt);
                        tt = new JSONObject();
                    }
                    tt.putOpt("id", "base_1");
                    tt.putOpt("operation", "equals");
                    tt.putOpt("value", "有");
                    my.put(tt);
                    one.optJSONObject("show_if").putOpt("subconditions", my);
                    Log.d("lognewsselect", "new for title 45" + one);
//                    one.optJSONObject("show_if").optJSONObject("subconditions");
                    continue;
                }

//                if(one.optString("id").equals("read_2")){
//                    Log.d("lognewsselect", "!!!!!!!!!!!!!!!!!!!()" + one.optString("question"));
//                    one.putOpt ("question", "請回答關於新聞「" + news_title + "」的問題");
//                    Log.d("lognewsselect", "!!!!!!!!!!!!!!!!!!!()" + one.optString("question"));
//                    break;
//                }
            }
            Log.d("lognewsselect", "jsonRootObject" + jsonRootObject.toString());
            Map<String, Object> record_noti = new HashMap<>();
            record_noti.put("click", jsonRootObject.toString());
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("123")
                    .document(String.valueOf(Timestamp.now()))
                    .set(record_noti);
            File file;
            file = new File(getFilesDir(),"test.json");
//            file = new File(getFilesDir(),"test.json");
            try{
                FileOutputStream fos = new FileOutputStream(file);//创建一个文件输出流
                fos.write(jsonRootObject.toString().getBytes());//将生成的JSON数据写出
                fos.close();//关闭输出流
                Toast.makeText(getApplicationContext(),"创建成功！",Toast.LENGTH_SHORT).show();
                Log.d("lognewsselect", "!!!!!!!!!!!!!!!!!!!()" + file.getAbsolutePath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file.getAbsolutePath();

        }
    }

    public String loadJSONFromAsset(String file_name) {
        String json = null;
        try {
            InputStream is = getApplication().getAssets().open(file_name);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
