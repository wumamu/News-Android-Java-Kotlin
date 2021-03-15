//package com.recoveryrecord.surveyandroid.example;
//
//import android.app.ActivityManager;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.util.Log;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import androidx.annotation.NonNull;
//
//public final class AppInfoUtil {
//
//    public AppInfoUtil() { }
//
//    /**
//     * 获取应用程序信息
//     */
//    public static PackageInfo getPackageInfo(@NonNull Context context) {
//        try {
//            PackageManager packageManager = context.getPackageManager();
//            return packageManager.getPackageInfo(context.getPackageName(), 0);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * 获取应用程序名称
//     */
//    public static String getAppName(@NonNull Context context) {
//        try {
//            PackageManager packageManager = context.getPackageManager();
//            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
//            int labelRes = packageInfo.applicationInfo.labelRes;
//            return context.getResources().getString(labelRes);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * 获取应用程序版本名称
//     */
//    public static String getVersionName(@NonNull Context context) {
//        try {
//            PackageManager packageManager = context.getPackageManager();
//            PackageInfo info = packageManager.getPackageInfo(context.getPackageName(), 0);
//            if (null != info) {
//                return info.versionName;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * 获取应用程序版本号
//     */
//    public static int getVersionCode(@NonNull Context context) {
//        try {
//            PackageManager packageManager = context.getPackageManager();
//            PackageInfo info = packageManager.getPackageInfo(context.getPackageName(), 0);
//            if (null != info) {
//                return info.versionCode;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return -1;
//    }
//
//    /**
//     * 获取应用程序包名
//     */
//    public static String getPackageName(@NonNull Context context) {
//        try {
//            PackageManager packageManager = context.getPackageManager();
//            PackageInfo info = packageManager.getPackageInfo(context.getPackageName(), 0);
//            if (null != info) {
//                return info.packageName;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * 判断当前应用程序是否处于后台
//     * <pre>需要权限：&lt;uses-permission android:name="android.permission.GET_TASKS" /&gt;  </pre>
//     */
//    @SuppressWarnings("deprecation")
//    @Deprecated
//    public static boolean isApplicationToBackground(@NonNull Context context) {
//        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        if (null == am) {
//            return false;
//        }
//        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
//        if (!tasks.isEmpty()) {
//            ComponentName topActivity = tasks.get(0).topActivity;
//            if (!topActivity.getPackageName().equals(context.getPackageName())) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 获取当前运行的进程名
//     */
//    public static String getProcessName(@NonNull Context context) {
//        int pid = android.os.Process.myPid();
//        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        if (null == activityManager) {
//            return null;
//        }
//        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
//            if (appProcess.pid == pid) {
//                return appProcess.processName;
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 获取当前运行的所有进程名
//     */
//    public static List<String> getProcessName(@NonNull Context context, @NonNull String packageNameT) {
//        List<String> list = new ArrayList<>();
//        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        if (null == activityManager) {
//            return null;
//        }
//        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
//            Log.e("log: app info1", appProcess.processName);
////            if (appProcess.processName.startsWith(packageName)) {
////                list.add(appProcess.processName);
//////                list.add(appProcess.processName);
////            }
//        }
//        final PackageManager packageManager = context.getPackageManager();
//        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
//        for (ActivityManager.RunningTaskInfo runningTaskInfo : runningTasks) {
//            String packageName = runningTaskInfo.baseActivity.getPackageName();
//            try {
//                String appName = packageManager.getApplicationInfo(packageName, 0).loadLabel(packageManager).toString();
//                Log.e("log: app info2", appName);
////                hashSet.add(appName);
//            } catch (PackageManager.NameNotFoundException exception) {
//                // handle Exception
//            }
//        }
//        return list;
//    }
//
//    /**
//     * 获取当前运行界面的包名
//     */
//    @SuppressWarnings("deprecation")
//    @Deprecated
//    public static String getTopPackageName(@NonNull Context context) {
//        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        if (activityManager != null) {
//            ComponentName cn = activityManager.getRunningTasks(1).get(0).topActivity;
//            return cn.getPackageName();
//        }
//        return null;
//    }
//}