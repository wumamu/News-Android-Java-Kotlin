package com.recoveryrecord.surveyandroid.example;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static com.recoveryrecord.surveyandroid.example.Constants.APP_VERSION_VALUE;

public class CheckUpdateActivity extends AppCompatActivity {
//    Boolean set_once = false;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
//    String device_id = "";

    private Button mybutton;
    private TextView uptime;//, intro;
//    FirebaseStorage firebaseStorage;
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
                assert document != null;
                if (document.exists()) {
                    if(Objects.equals(document.getString("version"), APP_VERSION_VALUE)){
                        uptime.setText("目前已是最新版本\n當前版本: " + APP_VERSION_VALUE);
                        mybutton.setVisibility(View.GONE);
                    } else {
                        uptime.setText("已有更新版本供下載 \n" + "最新版本: " + document.getString("version") + "\n當前版本: " + APP_VERSION_VALUE);
                    }
//                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                }  //                    Log.d("TAG", "No such document");

            }  //                Log.d("TAG", "get failed with ", task.getException());

        });

        mybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download();
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
                    assert downloadManager != null;
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
