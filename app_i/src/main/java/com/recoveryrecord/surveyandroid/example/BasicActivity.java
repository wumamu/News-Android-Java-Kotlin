package com.recoveryrecord.surveyandroid.example;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

import com.recoveryrecord.surveyandroid.example.receiever.BlueToothReceiver;
import com.recoveryrecord.surveyandroid.example.receiever.NetworkChangeReceiver;

import static android.media.AudioManager.STREAM_ALARM;
import static android.media.AudioManager.STREAM_MUSIC;
import static android.media.AudioManager.STREAM_NOTIFICATION;
import static android.media.AudioManager.STREAM_RING;
//import android.support.v4.app.NotificationCompat ;
//import android.support.v7.app.AppCompatActivity ;


public class BasicActivity extends AppCompatActivity {

    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
//    private Button btn_show;
    private Context mContext;
    private BlueToothReceiver mBluetoothReceiver;
    private NetworkChangeReceiver mNetworkReceiver;
    private AudioManager myAudioManager;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    NotificationManager manager;
//    private AlarmManager alarmMgr;
//    private PendingIntent alarmIntent;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ESMDbHelper esmDbHelper = new ESMDbHelper(this);
        setTitle("Homepage");
//        addAdaLovelace();
        setContentView(R.layout.activity_basic);
        Button btn_to_news = (Button) findViewById(R.id.btn_to_news);
        Button btn_to_diary = (Button) findViewById(R.id.btn_to_diary);
        Button btn_to_noti_list = (Button) findViewById(R.id.btn_to_noti_list);
        Button btn_to_test = (Button) findViewById(R.id.btn_to_test);
        //audio ####################################################################################
        myAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        //network checker###########################################################################
        mNetworkReceiver = new NetworkChangeReceiver();
        registerNetworkBroadcastForNougat();//register
        mContext = BasicActivity.this;
        btn_to_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(BasicActivity.this, NewsModuleActivity.class);
            intent.putExtra("trigger_from", "MainActivity");
            startActivity(intent);
            //MainActivity.this.finish();
            }
        });
        btn_to_diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(BasicActivity.this, ExampleSurveyActivity.class);
//            intent.setClass(MainActivity.this, ESMJsonViewActivity.class);
            startActivity(intent);
            //MainActivity.this.finish();
            }
        });
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        btn_to_noti_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(BasicActivity.this, NotificationSettingActivity.class);
//                intent.setClass(MainActivity.this, NewsMainActivity.class);
//                intent.setClass(MainActivity.this, TmpMainActivity.class);
                startActivity(intent);
                //MainActivity.this.finish();
            }
        });
        btn_to_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTimeInMillis(System.currentTimeMillis());
//                calendar.set(Calendar.HOUR_OF_DAY, 1);
//                calendar.set(Calendar.MINUTE, 48);
                int mode_ring =myAudioManager.getRingerMode();//ringtone mode
                if(mode_ring==AudioManager.RINGER_MODE_VIBRATE){
                    Log.e("log: ring mode", "Vibrate");
//            Toast.makeText(MainActivity.this,"Now in Vibrate Mode", Toast.LENGTH_LONG).show();
                } else if(mode_ring==AudioManager.RINGER_MODE_NORMAL){
                    Log.e("log: ring mode", "Ringing");
//            Toast.makeText(MainActivity.this,"Now in Ringing Mode", Toast.LENGTH_LONG).show();
                } else if (mode_ring==AudioManager.RINGER_MODE_SILENT){
                    Log.e("log: ring mode", "Silent");
//            Toast.makeText(MainActivity.this,"Now in Vibrate Mode", Toast.LENGTH_LONG).show();
                } else {
                    Log.e("log: ring mode", "Unknown");
                }
                int mode_audio =myAudioManager.getMode();
                if(mode_audio==AudioManager.MODE_NORMAL){
                    Log.e("log: audio mode", "normal");
                    //            Toast.makeText(MainActivity.this,"Now in Vibrate Mode", Toast.LENGTH_LONG).show();
                } else if(mode_audio==AudioManager.MODE_RINGTONE){
                    Log.e("log: audio mode", "is ringing");
                    //            Toast.makeText(MainActivity.this,"Now in Ringing Mode", Toast.LENGTH_LONG).show();
                } else if (mode_audio==AudioManager.MODE_IN_CALL){
                    Log.e("log: audio mode", "in call");
                    //            Toast.makeText(MainActivity.this,"Now in Vibrate Mode", Toast.LENGTH_LONG).show();
                } else if (mode_audio==AudioManager.MODE_IN_COMMUNICATION){
                    Log.e("log: audio mode", "in internet chat");
                    //            Toast.makeText(MainActivity.this,"Now in Vibrate Mode", Toast.LENGTH_LONG).show();
                } else {
                    Log.e("log: audio mode", "Unknown");
                }
                int volume_ring = myAudioManager.getStreamVolume(STREAM_RING);
                int volume_noti = myAudioManager.getStreamVolume(STREAM_NOTIFICATION);
                int volume_alarm = myAudioManager.getStreamVolume(STREAM_ALARM);
                int volume_music = myAudioManager.getStreamVolume(STREAM_MUSIC);
                int volume_max = myAudioManager.getStreamMaxVolume(STREAM_MUSIC);
                int volume_min = myAudioManager.getStreamMinVolume(STREAM_MUSIC);
                Log.e("log: volume ring", String.valueOf(volume_ring));
                Log.e("log: volume noti", String.valueOf(volume_noti));
                Log.e("log: volume alarm", String.valueOf(volume_alarm));
                Log.e("log: volume music", String.valueOf(volume_music));
                Log.e("log: volume max", String.valueOf(volume_max));
                Log.e("log: volume min", String.valueOf(volume_min));
                boolean music = myAudioManager.isMusicActive();
                Log.e("log: music", String.valueOf(music));
                AudioDeviceInfo[] devices = myAudioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
                for (AudioDeviceInfo device: devices) {
                    if (device.getType() == AudioDeviceInfo.TYPE_WIRED_HEADPHONES){
                        Log.e("log: AudioDevice type", "headphone");
//                        Toast.makeText(MainActivity.this,"123", Toast.LENGTH_LONG).show();
                    } else if (device.getType() == AudioDeviceInfo.TYPE_WIRED_HEADSET){
                        Log.e("log: AudioDevice type", "headset");
//                        Toast.makeText(MainActivity.this,"456", Toast.LENGTH_LONG).show();
                    } else if (device.getType() == AudioDeviceInfo.TYPE_USB_HEADSET) {
                        Log.e("log: AudioDevice type", "headset usb");
//                        Toast.makeText(MainActivity.this,"789", Toast.LENGTH_LONG).show();
                    } else if (device.getType() == AudioDeviceInfo.TYPE_BLUETOOTH_SCO){
                        Log.e("log: AudioDevice type", "Bluetooth device typically used for telephony");
                    } else if (device.getType() == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP){
                        Log.e("log: AudioDevice type", "Bluetooth device supporting the A2DP profile.");
                    } else {
                        Log.e("log: AudioDevice type", "unknown device");
                    }
                }

//                Intent intent = new Intent();
//                intent.setClass(TestActivity.this, NewsMainActivity.class);
////                intent.setClass(MainActivity.this, TmpMainActivity.class);
//                startActivity(intent);
//                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 20, alarmIntent);
//                scheduleNotification_repeat(getNotification("my repeat alarm"), calendar);
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTimeInMillis(System.currentTimeMillis());
//                calendar.set(Calendar.HOUR_OF_DAY, 8);
//                calendar.set(Calendar.MINUTE, 30);
//                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 20, alarmIntent);
//                Calendar cldr = Calendar.getInstance();
//                int hour = cldr.get(Calendar.HOUR_OF_DAY);
//                int minutes = cldr.get(Calendar.MINUTE);
//                // time picker dialog
//                picker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
////                                eText.setText(sHour + ":" + sMinute);
//                                Calendar c = Calendar.getInstance();
//                                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                                c.set(Calendar.MINUTE, minute);
//                                c.set(Calendar.SECOND, 0);
////                                updateTimeText(c);
////                                startAlarm(c);
//                            }
//                        }, hour, minutes, true);
//                picker.show();
            }
        });
        //ring mode ################################################################################
//        int mode_ring =myAudioManager.getRingerMode();//ringtone mode
//        if(mode_ring==AudioManager.RINGER_MODE_VIBRATE){
//            Log.e("log: ring mode", "Vibrate");
////            Toast.makeText(MainActivity.this,"Now in Vibrate Mode", Toast.LENGTH_LONG).show();
//        } else if(mode_ring==AudioManager.RINGER_MODE_NORMAL){
//            Log.e("log: ring mode", "Ringing");
////            Toast.makeText(MainActivity.this,"Now in Ringing Mode", Toast.LENGTH_LONG).show();
//        } else if (mode_ring==AudioManager.RINGER_MODE_SILENT){
//            Log.e("log: ring mode", "Silent");
////            Toast.makeText(MainActivity.this,"Now in Vibrate Mode", Toast.LENGTH_LONG).show();
//        } else {
//            Log.e("log: ring mode", "Unknown");
//        }
//        int mode_audio =myAudioManager.getMode();
//        if(mode_audio==AudioManager.MODE_NORMAL){
//            Log.e("log: audio mode", "normal");
//            //            Toast.makeText(MainActivity.this,"Now in Vibrate Mode", Toast.LENGTH_LONG).show();
//        } else if(mode_audio==AudioManager.MODE_RINGTONE){
//            Log.e("log: audio mode", "is ringing");
//            //            Toast.makeText(MainActivity.this,"Now in Ringing Mode", Toast.LENGTH_LONG).show();
//        } else if (mode_audio==AudioManager.MODE_IN_CALL){
//            Log.e("log: audio mode", "in call");
//            //            Toast.makeText(MainActivity.this,"Now in Vibrate Mode", Toast.LENGTH_LONG).show();
//        } else if (mode_audio==AudioManager.MODE_IN_COMMUNICATION){
//            Log.e("log: audio mode", "in internet chat");
//            //            Toast.makeText(MainActivity.this,"Now in Vibrate Mode", Toast.LENGTH_LONG).show();
//        } else {
//            Log.e("log: audio mode", "Unknown");
//        }
//        int volume_ring = myAudioManager.getStreamVolume(STREAM_RING);
//        int volume_noti = myAudioManager.getStreamVolume(STREAM_NOTIFICATION);
//        int volume_alarm = myAudioManager.getStreamVolume(STREAM_ALARM);
//        int volume_music = myAudioManager.getStreamVolume(STREAM_MUSIC);
//        int volume_max = myAudioManager.getStreamMaxVolume(STREAM_MUSIC);
//        int volume_min = myAudioManager.getStreamMinVolume(STREAM_MUSIC);
//        Log.e("log: volume ring", String.valueOf(volume_ring));
//        Log.e("log: volume noti", String.valueOf(volume_noti));
//        Log.e("log: volume alarm", String.valueOf(volume_alarm));
//        Log.e("log: volume music", String.valueOf(volume_music));
//        Log.e("log: volume max", String.valueOf(volume_max));
//        Log.e("log: volume min", String.valueOf(volume_min));
//        boolean music = myAudioManager.isMusicActive();
//        Log.e("log: music", String.valueOf(music));
//        AudioDeviceInfo[] devices = myAudioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
//        for (AudioDeviceInfo device: devices) {
//            if (device.getType() == AudioDeviceInfo.TYPE_WIRED_HEADPHONES){
//                Log.e("log: AudioDevice type", "headphone");
////                        Toast.makeText(MainActivity.this,"123", Toast.LENGTH_LONG).show();
//            } else if (device.getType() == AudioDeviceInfo.TYPE_WIRED_HEADSET){
//                Log.e("log: AudioDevice type", "headset");
////                        Toast.makeText(MainActivity.this,"456", Toast.LENGTH_LONG).show();
//            } else if (device.getType() == AudioDeviceInfo.TYPE_USB_HEADSET) {
//                Log.e("log: AudioDevice type", "headset usb");
////                        Toast.makeText(MainActivity.this,"789", Toast.LENGTH_LONG).show();
//            } else if (device.getType() == AudioDeviceInfo.TYPE_BLUETOOTH_SCO){
//                Log.e("log: AudioDevice type", "Bluetooth device typically used for telephony");
//            } else if (device.getType() == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP){
//                Log.e("log: AudioDevice type", "Bluetooth device supporting the A2DP profile.");
//            } else {
//                Log.e("log: AudioDevice type", "unknown device");
//            }
//        }
        // device info #############################################################################
        String  details =  "VERSION.RELEASE : "+Build.VERSION.RELEASE
                +"\nVERSION.INCREMENTAL : "+Build.VERSION.INCREMENTAL
                +"\nVERSION.SDK.NUMBER : "+Build.VERSION.SDK_INT
                +"\nBOARD : "+Build.BOARD
                +"\nBOOTLOADER : "+Build.BOOTLOADER
                +"\nBRAND : "+Build.BRAND
                +"\nCPU_ABI : "+Build.CPU_ABI
                +"\nCPU_ABI2 : "+Build.CPU_ABI2
                +"\nDISPLAY : "+Build.DISPLAY
                +"\nFINGERPRINT : "+Build.FINGERPRINT
                +"\nHARDWARE : "+Build.HARDWARE
                +"\nHOST : "+Build.HOST
                +"\nID : "+Build.ID
                +"\nMANUFACTURER : "+Build.MANUFACTURER
                +"\nMODEL : "+Build.MODEL
                +"\nPRODUCT : "+Build.PRODUCT
                +"\nSERIAL : "+Build.SERIAL
                +"\nTAGS : "+Build.TAGS
                +"\nTIME : "+Build.TIME
                +"\nTYPE : "+Build.TYPE
                +"\nUNKNOWN : "+Build.UNKNOWN
                +"\nUSER : "+Build.USER;

        Log.d("log: Device Details\n",details);
//        DeviceUtil.printInfo();
//        // app backgroung info #####################################################################
//        AppInfoUtil.getProcessName(mContext, "123");
        // device orientation #######################################################################
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            Log.e("log: device orientation", "landscape");//橫
        } else {
            // In portrait
            Log.e("log: device orientation", "portrait");
        }
        // bluetooth ################################################################################
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            //Android M Permission check
            if(this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }
        }
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, filter);
        mBluetoothAdapter.startDiscovery();
  }


    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //discovery starts, we can show progress dialog or perform other tasks
                Log.e("log: bluetooth device", "ACTION_DISCOVERY_STARTED");
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //discovery finishes, dismis progress dialog
                Log.e("log: bluetooth device", "ACTION_DISCOVERY_FINISHED");
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found
                Log.e("log: bluetooth device", "ACTION_FOUND");
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                Toast("Found device " + device.getName());
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Log.e("log: bluetooth device", deviceName + " " + deviceHardwareAddress);
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
//        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        Log.e("log: bluetooth discover", String.valueOf(mBluetoothAdapter.startDiscovery()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
        unregisterReceiver(mBluetoothReceiver);
        unregisterReceiver(receiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("log: bluetooth device", "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                            }
                        });
                    }
                    builder.show();
                }
                return;
            }
        }
    }

    //    public void notificationButtonOnClick(View view) {
    //        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
    //        startActivity(intent);
    //    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_news, menu);
        //getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_5 :
                scheduleNotification(getNotification("news (5 second delay)" ), 5000 );
                scheduleNotification_esm(getNotification_esm("Please fill out the questionnaire" ), 30000 );
                return true;
            case R.id.action_10 :
                scheduleNotification(getNotification("news (10 second delay)" ), 10000 );
                scheduleNotification_esm(getNotification_esm("Please fill out the questionnaire" ), 30000 );
                return true;
            case R.id.action_30 :
                scheduleNotification(getNotification("news (30 second delay)" ), 30000 );
                scheduleNotification_esm(getNotification_esm("Please fill out the questionnaire" ), 30000 );
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void scheduleNotification (Notification notification, int delay) {
        int nid = (int) System.currentTimeMillis();
        Log.d("log: notification", "news id" + nid);
        Intent notificationIntent = new Intent(this, MyNotificationPublisherNews.class);
        notificationIntent.putExtra(MyNotificationPublisherNews.NOTIFICATION_ID, 1 ) ;
        notificationIntent.putExtra(MyNotificationPublisherNews.NOTIFICATION, notification) ;
        int randomNum = ThreadLocalRandom.current().nextInt(0, 1000000 + 1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast( this, randomNum, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }
    private Notification getNotification (String content) {
        int nid = (int) System.currentTimeMillis();
        Log.d("log: notification", "news id" + nid);
        Intent intent_news = new Intent();
        intent_news.setClass(BasicActivity.this, NewsModuleActivity.class);
        intent_news.putExtra("trigger_from", "Notification");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, nid, intent_news, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, default_notification_channel_id);
        builder.setContentTitle("Scheduled NEWS");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        return builder.build() ;
    }
    @SuppressLint("ShortAlarm")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void scheduleNotification_repeat (Notification notification, Calendar cc) {
        int nid = (int) System.currentTimeMillis();
        Log.d("log: notification", "news id" + nid);
        Intent notificationIntent = new Intent(this, MyNotificationPublisherNews.class);
        notificationIntent.putExtra(MyNotificationPublisherESM.NOTIFICATION_ID, 1 ) ;
        notificationIntent.putExtra(MyNotificationPublisherESM.NOTIFICATION, notification) ;
        int randomNum = ThreadLocalRandom.current().nextInt(0, 1000000 + 1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast( this, randomNum, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cc.getTimeInMillis(), 1000 * 60, pendingIntent);
//        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000 * 20, 1000 * 20, pendingIntent);
    }
    private Notification getNotification_esm (String content) {
        int nid = (int) System.currentTimeMillis();
        Intent intent_esm = new Intent();
        intent_esm.setClass(BasicActivity.this, ExampleSurveyActivity.class);
        intent_esm.putExtra("trigger_from", "Notification");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, nid, intent_esm, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, default_notification_channel_id);
        builder.setContentTitle("Scheduled ESM");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        return builder.build() ;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void scheduleNotification_esm (Notification notification, int delay) {
        int nid = (int) System.currentTimeMillis();
        Log.d("log: notification", "news id" + nid);
        Intent notificationIntent = new Intent(this, MyNotificationPublisherNews.class);
        notificationIntent.putExtra(MyNotificationPublisherESM.NOTIFICATION_ID, 1 ) ;
        notificationIntent.putExtra(MyNotificationPublisherESM.NOTIFICATION, notification) ;
        int randomNum = ThreadLocalRandom.current().nextInt(0, 1000000 + 1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast( this, randomNum, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

}
