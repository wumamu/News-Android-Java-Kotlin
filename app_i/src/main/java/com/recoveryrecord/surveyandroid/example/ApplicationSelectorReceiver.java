package com.recoveryrecord.surveyandroid.example;//package com.example.test;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.util.Objects;

import androidx.annotation.RequiresApi;

public class ApplicationSelectorReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        System.out.println(action);
        for (String key : Objects.requireNonNull(intent.getExtras()).keySet()) {
            try {
                ComponentName componentInfo = (ComponentName) intent.getExtras().get(key);
                PackageManager packageManager = context.getPackageManager();
                assert componentInfo != null;
                String appName = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(componentInfo.getPackageName(), PackageManager.GET_META_DATA));
                Log.d("log: Selected App Name", appName);
                Intent intent_app = new Intent("com.recoveryrecord.surveyandroid.example.NOTIFICATION_LISTENER_EXAMPLE");
                intent_app.putExtra("share_app",appName);
                context.sendBroadcast(intent_app);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}