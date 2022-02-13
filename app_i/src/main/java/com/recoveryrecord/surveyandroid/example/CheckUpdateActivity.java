package com.recoveryrecord.surveyandroid.example;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirestoreRegistrar;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.recoveryrecord.surveyandroid.example.DbHelper.ActivityRecognitionReceiverDbHelper;
import com.recoveryrecord.surveyandroid.example.DbHelper.AppUsageReceiverDbHelper;
import com.recoveryrecord.surveyandroid.example.DbHelper.DiaryDbHelper;
import com.recoveryrecord.surveyandroid.example.DbHelper.ESMDbHelper;
import com.recoveryrecord.surveyandroid.example.DbHelper.LightSensorReceiverDbHelper;
import com.recoveryrecord.surveyandroid.example.DbHelper.NetworkChangeReceiverDbHelper;
import com.recoveryrecord.surveyandroid.example.DbHelper.PushNewsDbHelper;
import com.recoveryrecord.surveyandroid.example.DbHelper.ReadingBehaviorDbHelper;
import com.recoveryrecord.surveyandroid.example.DbHelper.RingModeReceiverDbHelper;
import com.recoveryrecord.surveyandroid.example.DbHelper.ScreenStateReceiverDbHelper;
import com.recoveryrecord.surveyandroid.example.DbHelper.SessionDbHelper;
import com.recoveryrecord.surveyandroid.example.model.NewsModel;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static com.recoveryrecord.surveyandroid.example.Constants.*;

public class CheckUpdateActivity extends AppCompatActivity {
//    Boolean set_once = false;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    String device_id = "";

    private Button mybutton;
    private TextView uptime, intro;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    StorageReference ref;


    @SuppressLint({"HardwareIds", "SetTextI18n"})
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("App版本");
        setContentView(R.layout.activity_update);
        // Create a storage reference from our app

//        device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//        @SuppressLint("SimpleDateFormat")
//        final SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
////        intro = findViewById(R.id.intro);
        uptime = findViewById(R.id.uptextview);
        mybutton = findViewById(R.id.upbutton);

        DocumentReference docRef = db.collection("app_info").document("g8FD5IwuIX26rI8tmgHs");
        docRef.get().addOnCompleteListener((com.google.android.gms.tasks.OnCompleteListener<DocumentSnapshot>) task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    if(document.getString("version").equals(APP_VERSION_VALUE)){
                        uptime.setText("目前已是最新版本\n當前版本: " + APP_VERSION_VALUE);
                        mybutton.setVisibility(View.GONE);
                    } else {
                        uptime.setText("已有更新版本供下載 \n" + "最新版本: " + document.getString("version") + "\n當前版本: " + APP_VERSION_VALUE);
                    }
//                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                } else {
//                    Log.d("TAG", "No such document");
                }
            } else {
//                Log.d("TAG", "get failed with ", task.getException());
            }
        });


//        uptime.setText("當前版本");
//
        mybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download();
//                Toast.makeText(getApplicationContext(), "上傳資料完成", Toast.LENGTH_SHORT).show();
//
//                Long new_time = Timestamp.now().getSeconds();
//                Date new_date = new Date();
//                new_date.setTime(new_time*1000);
//                uptime.setText("此上傳機制為確保App資料完整性，您將上傳 新聞閱讀記錄、以及問卷填答記錄（ESM, Diary)。\n\n上次上傳時間\n" + formatter.format(new_date));
//
//                SharedPreferences.Editor editor = sharedPrefs.edit();
//                editor.putLong(UPLOAD_TIME, new_time);
//                editor.apply();
            }

            private void download() {
                storageReference = FirebaseStorage.getInstance().getReference();
                ref = storageReference.child("app_i-debug.apk");
                ref.getDownloadUrl().addOnSuccessListener(uri -> {
                    DownloadManager downloadManager = (DownloadManager) CheckUpdateActivity.this.getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri my_url = Uri.parse(uri.toString());
                    DownloadManager.Request request = new DownloadManager.Request(my_url);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir( DIRECTORY_DOWNLOADS, "app_i-debug.apk");
                    downloadManager.enqueue(request);
                    Toast.makeText(getApplicationContext(), "下載成功，請稍等一下", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "下載失敗", Toast.LENGTH_SHORT).show());
            }
        });






    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
