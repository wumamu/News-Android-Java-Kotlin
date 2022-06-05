//package com.recoveryrecord.surveyandroid.example;
//
//import android.annotation.SuppressLint;
//import android.annotation.TargetApi;
//import android.app.TimePickerDialog;
//import android.content.ClipboardManager;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.CompoundButton;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.TimePicker;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.SwitchCompat;
//import androidx.fragment.app.DialogFragment;
//
//public class NotificationRangeActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
//
//    protected void onCreate (Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setTitle("通知時間調整");
//        setContentView(R.layout.activity_notification_range);
//        Button button_s = (Button) findViewById(R.id.button_s);
//        Button button_e = (Button) findViewById(R.id.button_e);
//        button_s.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DialogFragment timePicker = new TimePickerFragment();
//                timePicker.show(getSupportFragmentManager(), "time picker");
//            }
//        });
//        button_e.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DialogFragment timePicker = new TimePickerFragment();
//                timePicker.show(getSupportFragmentManager(), "time picker");
//            }
//        });
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//    }
//    @SuppressLint("SetTextI18n")
//    @Override
//    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//        TextView textView = (TextView) findViewById(R.id.textView_s);
//        textView.setText(hourOfDay + " : "  + minute);
//    }
//
//}

package com.recoveryrecord.surveyandroid.example;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import static com.recoveryrecord.surveyandroid.example.Constants.ESM_END_TIME_HOUR;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_END_TIME_MIN;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_PASSWORD;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_SET_ONCE;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_START_TIME_HOUR;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_START_TIME_MIN;
import static com.recoveryrecord.surveyandroid.example.Constants.RESTART_ALARM_ACTION;
import static com.recoveryrecord.surveyandroid.example.Constants.SCHEDULE_ALARM_ACTION;
import static com.recoveryrecord.surveyandroid.example.Constants.SWITCH_STATE;

//import static com.recoveryrecord.surveyandroid.example.Constants.ESM_NOTI_BLOCK;

//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.SwitchCompat;

public class NotificationRangeActivity extends AppCompatActivity {

    String TAG = "RemindMe";
    LocalData localData;
    Boolean set_once = false;//, noti_block = false;
    Boolean switch_state = false;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch switchButton;
    TextView tvTime, ee_tvTime;

    LinearLayout ll_set_time;//, ll_terms;
    LinearLayout ee_set_time;//, ee_terms;

    int s_hour, s_min;
    int e_hour, e_min;

//    ClipboardManager myClipboard;
    LinearLayout coordinatorLayout;

    private int count = 0;
    private long startMillis=0;
    private String m_Text = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("問卷推播時間設定");
        setContentView(R.layout.activity_notification_range);
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        set_once = sharedPrefs.getBoolean(ESM_SET_ONCE, false);
//        noti_block = sharedPrefs.getBoolean(ESM_NOTI_BLOCK, false);
        switch_state = sharedPrefs.getBoolean(SWITCH_STATE, false);
        localData = new LocalData(getApplicationContext());
        ll_set_time = findViewById(R.id.ll_set_time);
        ee_set_time = findViewById(R.id.ee_set_time);
        tvTime = findViewById(R.id.tv_reminder_time_desc);
        ee_tvTime = findViewById(R.id.ee_tv_reminder_time_desc);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        switchButton = findViewById(R.id.switch_Above);

        switchButton.setChecked(switch_state);
        switchButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                switch_state = true;
                switchButton.setChecked(true);
//                noti_block = false;
                ll_set_time.setAlpha(1);
                ee_set_time.setAlpha(1);
                ((TextView) findViewById(R.id.tv_reminder_time_label)).setTypeface(null, Typeface.BOLD);
                ((TextView) findViewById(R.id.tv_reminder_time_label_e)).setTypeface(null, Typeface.BOLD);
                ((TextView) findViewById(R.id.tv_reminder_time_desc)).setTypeface(null, Typeface.BOLD);
                ((TextView) findViewById(R.id.ee_tv_reminder_time_desc)).setTypeface(null, Typeface.BOLD);
                // The toggle is enabled
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putBoolean(SWITCH_STATE, true);
                if(!set_once){
                    editor.putBoolean(ESM_SET_ONCE, true);
                    editor.apply();
                    Intent intent_schedule = new Intent(getApplicationContext(), AlarmReceiver.class);
                    intent_schedule.setAction(SCHEDULE_ALARM_ACTION);
                    AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1001, intent_schedule, 0);
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.SECOND, 2);
                    assert alarmManager != null;
                    alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() , pendingIntent);
                    set_once = true;
                } else {
                    editor.apply();
                    Intent intent_restart = new Intent(NotificationRangeActivity.this, AlarmReceiver.class);
                    intent_restart.setAction(RESTART_ALARM_ACTION);
                    AlarmManager alarmManager = (AlarmManager)NotificationRangeActivity.this.getSystemService(Context.ALARM_SERVICE);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(NotificationRangeActivity.this, 1050, intent_restart, 0);
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.SECOND, 2);
                    assert alarmManager != null;
                    alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() , pendingIntent);
                }
                Toast.makeText(getApplicationContext(), "問卷將於 " + sharedPrefs.getInt(ESM_START_TIME_HOUR, 9) + "-" + sharedPrefs.getInt(ESM_END_TIME_HOUR, 21) + " 區間內發送", Toast.LENGTH_SHORT).show();
            } else {
                switch_state = false;
                switchButton.setChecked(false);
//                noti_block = true;
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putBoolean(SWITCH_STATE, false);
                editor.apply();
                ll_set_time.setAlpha(0.4f);
                ee_set_time.setAlpha(0.4f);
            }
        });

        s_hour = sharedPrefs.getInt(ESM_START_TIME_HOUR, 9);
        e_hour = sharedPrefs.getInt(ESM_END_TIME_HOUR, 21);
        s_min = sharedPrefs.getInt(ESM_START_TIME_MIN, 0);
        e_min = sharedPrefs.getInt(ESM_END_TIME_MIN, 0);
        tvTime.setText(getFormatedTime(s_hour, s_min, true));
        ee_tvTime.setText(getFormatedTime(e_hour, e_min, false));




        ll_set_time.setOnClickListener(view -> {
            if(!set_once){
                showTimePickerDialog(localData.get_hour(), localData.get_min(), true);
            } else {
                Toast.makeText(NotificationRangeActivity.this, "抱歉不能修改咯", Toast.LENGTH_SHORT).show();
            }


        });
        ee_set_time.setOnClickListener(view -> {
            if(!set_once){
                showTimePickerDialog(localData.get_hour(), localData.get_min(), false);
            } else {
                Toast.makeText(NotificationRangeActivity.this, "抱歉不能修改咯", Toast.LENGTH_SHORT).show();
            }
        });



    }


    private void showTimePickerDialog(int h, int m, final boolean s_or_e) {

        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.timepicker_header, null);

        TimePickerDialog builder = new TimePickerDialog(this, R.style.DialogTheme,
                (timePicker, hour, min) -> {
                    Log.d(TAG, "onTimeSet: hour " + hour);
                    Log.d(TAG, "onTimeSet: min " + min);
//                        localData.set_hour(hour);
//                        localData.set_min(min);
                    if(s_or_e){
                        tvTime.setText(getFormatedTime(hour, min, s_or_e));
                    } else {
                        ee_tvTime.setText(getFormatedTime(hour, min, false));
                    }
                }, h, m, false);

        builder.setCustomTitle(view);
        builder.show();

    }

    public String getFormatedTime(int h, int m, boolean s_e) {
        final String OLD_FORMAT = "HH:mm";
        final String NEW_FORMAT = "hh:mm a";

        String oldDateString = h + ":" + m;
        String newDateString = "";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, getCurrentLocale());
            Date d = sdf.parse(oldDateString);
            sdf.applyPattern(NEW_FORMAT);
            assert d != null;
            newDateString = sdf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        if(s_e){
            editor.putInt(ESM_START_TIME_HOUR, h);
            editor.putInt(ESM_START_TIME_MIN, m);
        } else {
            editor.putInt(ESM_END_TIME_HOUR, h);
            editor.putInt(ESM_END_TIME_MIN, m);
        }
        editor.apply();


        return newDateString;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public Locale getCurrentLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return getResources().getConfiguration().locale;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(NotificationRangeActivity.this, SettingsActivity.class);
//        startActivity(intent);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int eventaction = event.getAction();
        if (eventaction == MotionEvent.ACTION_UP) {

            //get system current milliseconds
            long time= System.currentTimeMillis();


            //if it is the first time, or if it has been more than 3 seconds since the first tap ( so it is like a new try), we reset everything
            if (startMillis==0 || (time-startMillis> 3000) ) {
                startMillis=time;
                count=1;
            }
            //it is not the first, and it has been  less than 3 seconds since the first
            else{ //  time-startMillis< 3000
                count++;
            }

            if (count==5) {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String currentPassword = sharedPrefs.getString(ESM_PASSWORD, "715");


                //do whatever you need
//                Toast.makeText(NotificationRangeActivity.this, "嘿嘿嘿小彩蛋", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("通關密語");

                // Set up the input
                final EditText input = new EditText(this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", (dialog, which) -> {
                    m_Text = input.getText().toString();
                    if(m_Text.equals(currentPassword)){

                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        editor.putBoolean(ESM_SET_ONCE, false);
                        if(currentPassword.equals("715")){
                            editor.putString(ESM_PASSWORD, "443");
                        } else {
                            editor.putString(ESM_PASSWORD, "000");
                        }
                        editor.apply();
                        set_once = false;
                        Toast.makeText(NotificationRangeActivity.this, "密碼正確~", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(NotificationRangeActivity.this, "錯啦~有問題麻煩聯繫我們", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

                builder.show();
            }
            return true;
        }
        return false;
    }
}

