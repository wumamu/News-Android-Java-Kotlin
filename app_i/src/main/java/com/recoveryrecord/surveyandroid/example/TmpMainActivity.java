package com.recoveryrecord.surveyandroid.example;

import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TmpMainActivity extends AppCompatActivity {
    EditText packagename, time, tickertext, title, text;
    Button saveBtn, listBtn, deleteBtn;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmp_main);
        packagename = (EditText)findViewById(R.id.txtPackageName);
        time = (EditText)findViewById(R.id.txtTime);
        tickertext = (EditText)findViewById(R.id.txtTickerText);
        title = (EditText)findViewById(R.id.txtTitle);
        text = (EditText)findViewById(R.id.txtText);
        saveBtn = (Button)findViewById(R.id.btnSave);
        listBtn = (Button)findViewById(R.id.btnList);
//        deleteBtn = (Button)findViewById(R.id.btnDelete);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pn = packagename.getText().toString();
                String t = time.getText().toString();
                String tt = tickertext.getText().toString();
                String ti = title.getText().toString();
                String te = text.getText().toString();

                if (pn.isEmpty()) {
                    packagename.setError("Please enter packagename!");
                    packagename.requestFocus();
                } else if (t.isEmpty()) {
                    time.setError("Please enter time!");
                    time.requestFocus();
                } else if (tt.isEmpty()) {
                    tickertext.setError("Please enter tickertext!");
                    tickertext.requestFocus();
                } else if (ti.isEmpty()){
                    title.setError("Please enter title!");
                    title.requestFocus();
                } else if (te.isEmpty()){
                    text.setError("Please enter text!");
                    text.requestFocus();
                } else {
                    NotificationDbHandler dbHandler = new NotificationDbHandler(TmpMainActivity.this);
                    dbHandler.insertUserDetails(pn, t, tt, ti, te);
//                    intent = new Intent(MainActivity.this, DetailsActivity.class);
//                    startActivity(intent);
                    packagename.getText().clear();
                    time.getText().clear();
                    tickertext.getText().clear();
                    title.getText().clear();
                    text.getText().clear();

                    Toast.makeText(getApplicationContext(), "Details Inserted Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
        listBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                intent = new Intent(TmpMainActivity.this, NotificationDetailsActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onBackPressed() {
        intent = new Intent(TmpMainActivity.this, NotificationListActivity.class);
        startActivity(intent);
    }
}