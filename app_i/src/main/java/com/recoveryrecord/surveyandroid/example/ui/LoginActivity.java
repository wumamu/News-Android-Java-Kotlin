package com.recoveryrecord.surveyandroid.example.ui;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.recoveryrecord.surveyandroid.example.NewsHybridActivity;
import com.recoveryrecord.surveyandroid.example.R;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import static com.recoveryrecord.surveyandroid.example.config.Constants.SHARE_PREFERENCE_USER_ID;
import static com.recoveryrecord.surveyandroid.example.config.Constants.USER_NUM;

//import android.view.View;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import androidx.annotation.NonNull;

public class LoginActivity extends AppCompatActivity {
    private EditText useremail, password;
    private FirebaseAuth mAuth;
//    Boolean isLogin = false;
//    SharedPreferences sharedPrefs_Login;
    //    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
//        sharedPrefs_Login = getSharedPreferences(SHARE_PREFERENCE_IS_LOGIN, MODE_PRIVATE);
//        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        isLogin = sharedPrefs.getBoolean(SHARE_PREFERENCE_IS_LOGIN, false);
//        String tmp_sign = sharedPrefs.getString(SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號");
//        Log.d("555 LoginActivity", String.valueOf(sharedPrefs.getInt(SHARE_PREFERENCE_IS_LOGIN, 0)));
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
//            String[] split = user.getEmail().split("@");
            addInfoToSharePreference(user.getUid(), Objects.requireNonNull(user.getEmail()).split("@"));
            startActivity(new Intent(LoginActivity.this, NewsHybridActivity.class));
            finish();
        }

        Button button = findViewById(R.id.bt_login);
        useremail = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        button.setOnClickListener(v -> user_login());

//        if(sharedPrefs_Login.getBoolean(SHARE_PREFERENCE_IS_LOGIN, false)){
//        if(isLogin){
//            startActivity(new Intent(LoginActivity.this, NewsHybridActivity.class));
//            finish();
//        }

    }

    @SuppressLint("HardwareIds")
    private void addInfoToSharePreference(String userId, String[] split) {
//        Map<String, Object> toFirestore = new HashMap<>();
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        toFirestore.put(USER_FIRESTORE_ID, userId);
//        toFirestore.put(USER_SURVEY_NUMBER, split[0]);
//        db.collection(USER_COLLECTION)
//                .document(Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID))
//                .set(toFirestore);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPrefs.edit();
//                editor.putBoolean(SHARE_PREFERENCE_IS_LOGIN, true);
        editor.putString(SHARE_PREFERENCE_USER_ID, split[0]);
        editor.apply();
    }

    @SuppressLint("ApplySharedPref")
    private void user_login() {
        String string_useremail = useremail.getText().toString().trim();
        String string_password = password.getText().toString().trim();
        if(string_useremail.isEmpty()){
            useremail.setError("帳號還沒打阿");
            useremail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(string_useremail).matches()){
            useremail.setError("帳號格式好像錯了阿");
            useremail.requestFocus();
            return;
        }
        if(string_password.isEmpty()){
            password.setError("密碼還沒打阿");
            password.requestFocus();
            return;
        }
//        if(!Patterns.EMAIL_ADDRESS.matcher(string_password).matches()){
//            password.setError("Please enter a valid password");
//            password.requestFocus();
//            return;
//        }
        mAuth.signInWithEmailAndPassword(string_useremail, string_password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                FirebaseUser user = mAuth.getCurrentUser();
                assert user != null;
                addInfoToSharePreference(user.getUid(), user.getEmail().split("@"));
//                sharedPrefs_Login.edit().putBoolean(SHARE_PREFERENCE_IS_LOGIN, true).apply();
//                String[] split = string_useremail.split("@");
//                isLogin = true;
                Toast.makeText(LoginActivity.this, "登入成功", Toast.LENGTH_LONG).show();
                Intent i = new Intent(LoginActivity.this, NewsHybridActivity.class);
                i.putExtra(USER_NUM,user.getEmail().split("@")[0]);
                startActivity(i);
//                startActivity(new Intent(LoginActivity.this, NewsHybridActivity.class));
            } else {
                Toast.makeText(LoginActivity.this, "登入失敗QQ，需要幫忙請來信詢問", Toast.LENGTH_LONG).show();
            }
        });
    }

}
