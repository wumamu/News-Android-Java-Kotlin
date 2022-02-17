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

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.preference.PreferenceManager;

import static com.recoveryrecord.surveyandroid.example.Constants.CANCEL_ALARM_ACTION;
import static com.recoveryrecord.surveyandroid.example.Constants.RESTART_ALARM_ACTION;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_END_TIME_HOUR;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_END_TIME_MIN;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_SET_ONCE;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_START_TIME_HOUR;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_START_TIME_MIN;
import static com.recoveryrecord.surveyandroid.example.Constants.SCHEDULE_ALARM_ACTION;

public class NotificationRangeActivity extends AppCompatActivity {

    String TAG = "RemindMe";
    LocalData localData;
    Boolean set_once = false;
    Boolean alarm = false;

    SwitchCompat reminderSwitch;
    TextView tvTime, ee_tvTime;

    LinearLayout ll_set_time, ll_terms;
    LinearLayout ee_set_time, ee_terms;

    int s_hour, s_min;
    int e_hour, e_min;

//    ClipboardManager myClipboard;
    LinearLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_range);
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        set_once = sharedPrefs.getBoolean(ESM_SET_ONCE, false);
        localData = new LocalData(getApplicationContext());
        ll_set_time = (LinearLayout) findViewById(R.id.ll_set_time);
        ee_set_time = (LinearLayout) findViewById(R.id.ee_set_time);
        tvTime = (TextView) findViewById(R.id.tv_reminder_time_desc);
        ee_tvTime = (TextView) findViewById(R.id.ee_tv_reminder_time_desc);

        coordinatorLayout = (LinearLayout) findViewById(R.id.coordinatorLayout);

        final Button button_s = (Button) findViewById(R.id.button_s);
        final Button button_restart = (Button) findViewById(R.id.button_restart);
        button_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(NotificationRangeActivity.this)
                        .setTitle("儲存設定")
                        .setMessage("您設定的時間區間為 " + sharedPrefs.getInt(ESM_START_TIME_HOUR, 9) + "-" + sharedPrefs.getInt(ESM_END_TIME_HOUR, 21))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                SharedPreferences.Editor editor = sharedPrefs.edit();
                                editor.putBoolean(ESM_SET_ONCE, true);
                                editor.apply();
                                Intent intent_schedule = new Intent(getApplicationContext(), AlarmReceiver.class);
                                intent_schedule.setAction(SCHEDULE_ALARM_ACTION);
                                AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1001, intent_schedule, 0);
                                Calendar cal = Calendar.getInstance();
                                cal.add(Calendar.SECOND, 2);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() , pendingIntent);
                                ll_set_time.setAlpha(1);
                                ee_set_time.setAlpha(1);
                                set_once = true;
                                Toast.makeText(getApplicationContext(), "通知區間設定完成", Toast.LENGTH_SHORT).show();
                            }})
                        .setNegativeButton("取消", null).show();

//                Snackbar snackbar = Snackbar
//                        .make(coordinatorLayout, "設定完成", Snackbar.LENGTH_LONG)
//                        .setAction("UNDO", view -> {
//                            Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "設定取消，請重新設定", Snackbar.LENGTH_SHORT);
//                            snackbar1.show();
//                            SharedPreferences.Editor editor = sharedPrefs.edit();
//                            editor.putBoolean(ESM_SET_ONCE, false);
//                            editor.apply();
//                            set_once = false;
//                            Intent intent_cancel = new Intent(getApplicationContext(), AlarmReceiver.class);
//                            intent_cancel.setAction(CANCEL_ALARM_ACTION);
//                            AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1001, intent_cancel, 0);
//                            Calendar cal = Calendar.getInstance();
//                            cal.add(Calendar.SECOND, 2);
//                            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() , pendingIntent);
////                                Toast.makeText(NotificationRangeActivity.this, "設定完成", Toast.LENGTH_SHORT).show();
//                            ll_set_time.setAlpha(0.4f);
//                            ee_set_time.setAlpha(0.4f);
//                            button_s.setEnabled(true);
////                                Log.i("dshfdfhjsdjfd","**********  odsfdfsdfds***********");
//                        });
////                Log.i("dshfdfhjsdjfd","**********  onNotificationPosted ***********");
//                SharedPreferences.Editor editor = sharedPrefs.edit();
//                editor.putBoolean(ESM_SET_ONCE, true);
//                editor.apply();
//                set_once = true;
//                Intent intent_schedule = new Intent(getApplicationContext(), AlarmReceiver.class);
//                intent_schedule.setAction(SCHEDULE_ALARM_ACTION);
//                AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1001, intent_schedule, 0);
//                Calendar cal = Calendar.getInstance();
//                cal.add(Calendar.SECOND, 2);
//                alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() , pendingIntent);
////                Toast.makeText(NotificationRangeActivity.this, "設定完成", Toast.LENGTH_SHORT).show();
//                ll_set_time.setAlpha(1);
//                ee_set_time.setAlpha(1);
//                button_s.setEnabled(false);
////                set_once = true;
//                snackbar.show();
            }


//            }
        });

        button_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(set_once){
                    Toast.makeText(NotificationRangeActivity.this, "重置成功", Toast.LENGTH_SHORT).show();
//                    new AlertDialog.Builder(NotificationRangeActivity.this)
//                            .setTitle("重新配置問卷發送時間")
////                            .setMessage("請勿在非指示下刪除資料!\n可能會影響app運行")
//                            .setIcon(android.R.drawable.ic_dialog_alert)
//                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int whichButton) {
//                                    Intent intent_restart = new Intent(NotificationRangeActivity.this, AlarmReceiver.class);
//                                    intent_restart.setAction(RESTART_ALARM_ACTION);
//                                    AlarmManager alarmManager = (AlarmManager)NotificationRangeActivity.this.getSystemService(Context.ALARM_SERVICE);
//                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(NotificationRangeActivity.this, 1050, intent_restart, 0);
//                                    Calendar cal = Calendar.getInstance();
//                                    cal.add(Calendar.SECOND, 2);
//                                    alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() , pendingIntent);
//                                    Toast.makeText(NotificationRangeActivity.this, "重置成功", Toast.LENGTH_SHORT).show();
//                                }})
//                            .setNegativeButton("取消", null).show();
                } else {
                    Toast.makeText(getApplicationContext(), "請先設定區間", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        hour = localData.get_hour();
//        min = localData.get_min();

        s_hour = sharedPrefs.getInt(ESM_START_TIME_HOUR, 9);
        e_hour = sharedPrefs.getInt(ESM_END_TIME_HOUR, 21);
        s_min = sharedPrefs.getInt(ESM_START_TIME_MIN, 0);
        e_min = sharedPrefs.getInt(ESM_END_TIME_MIN, 0);
        tvTime.setText(getFormatedTime(s_hour, s_min, true));
        ee_tvTime.setText(getFormatedTime(e_hour, e_min, false));
        if(!set_once){
            //not set
            ll_set_time.setAlpha(0.4f);
            ee_set_time.setAlpha(0.4f);
        } else {
//            button_s.setEnabled(false);
        }

//        if (!localData.getReminderStatus()){

//        }


        ll_set_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(localData.get_hour(), localData.get_min(), true);
                Toast.makeText(NotificationRangeActivity.this, "調整後記得按儲存才會生效喔", Toast.LENGTH_SHORT).show();
//                if(!set_once){
//                    showTimePickerDialog(localData.get_hour(), localData.get_min(), true);
//                    Log.d("lognewsselect", "onClick ");
//                }
            }
        });
        ee_set_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(localData.get_hour(), localData.get_min(), false);
                Toast.makeText(NotificationRangeActivity.this, "調整後記得按儲存才會生效喔", Toast.LENGTH_SHORT).show();
//                if(!set_once){
//                    showTimePickerDialog(localData.get_hour(), localData.get_min(), false);
//                    Log.d("lognewsselect", "onClick ");
//                }
            }
        });



    }


    private void showTimePickerDialog(int h, int m, final boolean s_or_e) {

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.timepicker_header, null);

        TimePickerDialog builder = new TimePickerDialog(this, R.style.DialogTheme,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
                        Log.d(TAG, "onTimeSet: hour " + hour);
                        Log.d(TAG, "onTimeSet: min " + min);
//                        localData.set_hour(hour);
//                        localData.set_min(min);
                        if(s_or_e){
                            tvTime.setText(getFormatedTime(hour, min, s_or_e));
                        } else {
                            ee_tvTime.setText(getFormatedTime(hour, min, s_or_e));
                        }
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
}

