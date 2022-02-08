package com.recoveryrecord.surveyandroid.example;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
//import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

//import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import static com.recoveryrecord.surveyandroid.example.Constants.SHARE_PREFERENCE_IS_LOGIN;
import static com.recoveryrecord.surveyandroid.example.Constants.SHARE_PREFERENCE_USER_ID;

public class LoginActivity extends AppCompatActivity {
    private EditText useremail, password;
    private FirebaseAuth mAuth;
    //    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Boolean isLogin = sharedPrefs.getBoolean(SHARE_PREFERENCE_IS_LOGIN, false);
        if(isLogin){
            startActivity(new Intent(LoginActivity.this, NewsHybridActivity.class));
            finish();
        }
        Button button = (Button) findViewById(R.id.bt_login);
        useremail = (EditText)findViewById(R.id.et_username);
        password = (EditText)findViewById(R.id.et_password);

        button.setOnClickListener(v -> {
            user_login();
        });

        mAuth = FirebaseAuth.getInstance();

    }

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
//                    Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putBoolean(SHARE_PREFERENCE_IS_LOGIN, true);
                String[] split = string_useremail.split("@");
                editor.putString(SHARE_PREFERENCE_USER_ID, split[0]);

                editor.apply();
                Toast.makeText(LoginActivity.this, "登入成功", Toast.LENGTH_LONG).show();
                startActivity(new Intent(LoginActivity.this, NewsHybridActivity.class));
            } else {
                Toast.makeText(LoginActivity.this, "登入失敗QQ，需要幫忙請來信詢問", Toast.LENGTH_LONG).show();
            }
        });
    }
}
