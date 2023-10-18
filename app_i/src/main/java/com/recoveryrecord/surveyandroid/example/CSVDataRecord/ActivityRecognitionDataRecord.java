//package com.recoveryrecord.surveyandroid.example.CSVDataRecord;
//
//import static com.recoveryrecord.surveyandroid.example.config.Config.DATE_FORMAT_NOW;
//import static com.recoveryrecord.surveyandroid.example.config.Config.DATE_FORMAT_for_storing;
//
//import androidx.room.ColumnInfo;
//import androidx.room.Entity;
//import androidx.room.Ignore;
//import androidx.room.PrimaryKey;
//
//import com.google.android.gms.location.DetectedActivity;
//
//import org.json.JSONObject;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//@Entity(tableName = "ActivityRecognitionDataRecord")
//public class ActivityRecognitionDataRecord implements DataRecord{
//    public static String TAG = "ActivityRecognitionDataRecord";
//
//    @PrimaryKey(autoGenerate = true)
//    public long _id;
//
//    @ColumnInfo(name = "creationTime")
//    public long creationTime;
//
//    @ColumnInfo(name = "MostProbableActivity")
//    public String MostProbableActivityString;
//
//    @ColumnInfo(name = "ProbableActivities")
//    public String ProbableActivitiesString;
//
//    @ColumnInfo(name = "Detectedtime")
//    public long Detectedtime;
//
//    @ColumnInfo(name = "_timestamp")
//    public long _timestamp;
//    @ColumnInfo(name = "mTimestring")
//    public String mTimestring;
//
//    @ColumnInfo (name = "sessionid")
//    public String sessionid;
//    @ColumnInfo (name = "data")
//    public String data;
//
//    @ColumnInfo(name = "readable")
//    public Long readable;
//    @ColumnInfo(name = "sycStatus")
//    public Integer syncStatus;
//
//    @Ignore
//    private static DetectedActivity MostProbableActivity;
//    @Ignore
//    private List<DetectedActivity> mProbableActivities;
//
//    @Ignore protected JSONObject mData;
//
//    @ColumnInfo(name = "phone_sessionid")
//    public Long phone_sessionid;
//    @ColumnInfo(name = "screenshot")
//    public String screenshot;
//
//    @ColumnInfo(name = "ImageName")
//    public String ImageName;
//
//    public void setsyncStatus(Integer syncStatus){
//        this.syncStatus = syncStatus;
//    }
//    public Integer getsyncStatus(){
//        return this.syncStatus;
//    }
//
//
//    public ActivityRecognitionDataRecord(){
//
//    }
//
//    public ActivityRecognitionDataRecord(DetectedActivity MostProbableActivity,long Detectedtime){
//        this.creationTime = new Date().getTime();
//        this.MostProbableActivity = MostProbableActivity;
//        this.Detectedtime = Detectedtime;
//        this.syncStatus = 0;
//        this.readable = getReadableTimeLong(this.creationTime);
//        this.sessionid = sessionid;
//        this.phone_sessionid = phone_sessionid;
//        this.screenshot = screenshot;
//        this.ImageName = ImageName;
////        this.AccessibilityUrl = AccessibilityUrl;
////        this.NotificationUrl = NotificationUrl;
//    }
//
//
//
//    public ActivityRecognitionDataRecord(DetectedActivity mostProbableActivity, List<DetectedActivity> mProbableActivities,long detectedtime){
//        this.creationTime = new Date().getTime();
//        this.MostProbableActivity = mostProbableActivity;
//        if(mostProbableActivity!=null) this.MostProbableActivityString = mostProbableActivity.toString();
//        this.mProbableActivities = mProbableActivities;
//        if(mProbableActivities!=null) this.ProbableActivitiesString = mProbableActivities.toString();
//        this.Detectedtime = detectedtime;
//        this.sessionid = sessionid;
//        this.readable = getReadableTimeLong(this.creationTime);
//        this.syncStatus = 0;
//        this.phone_sessionid = phone_sessionid;
//        this.screenshot = screenshot;
//        this.ImageName = ImageName;
////        this.AccessibilityUrl = AccessibilityUrl;
////        this.NotificationUrl = NotificationUrl;
//    }
//
//    public String getSessionid() {
//        return sessionid;
//    }
//
//    public DetectedActivity getMostProbableActivity(){return MostProbableActivity;}
//
//    public void setProbableActivities(List<DetectedActivity> probableActivities) {
//        mProbableActivities = probableActivities;
//
//    }
//
//    public void setMostProbableActivity(DetectedActivity mostProbableActivity) {
//        MostProbableActivity = mostProbableActivity;
//
//    }
//
//    public void setDetectedtime(long detectedtime){
//        Detectedtime = detectedtime;
//
//    }
//
//    private long getmillisecondToHour(long timeStamp){
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(timeStamp);
//
//        long mhour = calendar.get(Calendar.HOUR_OF_DAY);
//
//        return mhour;
//
//    }
//
//    public void setID(long id){
//        _id = id;
//    }
//
//    public long getID(){
//        return _id;
//    }
//
//    public long getDetectedtime(){return Detectedtime;}
//
//    @Override
//    public long getCreationTime() {
//        return creationTime;
//    }
//
//    public void setTimestamp(long t){
//        _timestamp = t;
//    }
//
//    public long getTimestamp(){
//        return _timestamp;
//    }
//
//    public JSONObject getData() {
//        return mData;
//    }
//
//    public Long getReadable(){
//        return this.readable;
//    }
//
//    public void setData(JSONObject data) {
//        this.mData = data;
//    }
//
//    public String getTimeString(){
//
//        SimpleDateFormat sdf_now = new SimpleDateFormat(DATE_FORMAT_NOW);
//        mTimestring = sdf_now.format(_timestamp);
//
//        return mTimestring;
//    }
//
//    public List<DetectedActivity> getProbableActivities() {
//        return mProbableActivities;
//    }
//
//    public static Long getReadableTimeLong(long time){
//
//        SimpleDateFormat sdf_now = new SimpleDateFormat(DATE_FORMAT_for_storing);
//        String currentTimeString = sdf_now.format(time);
//        String[] splited = currentTimeString.split("\\s+");
//        String[]day = splited[0].split("-");
//        Long year = Long.valueOf(day[0]);
//        Long month = Long.valueOf(day[1]);
//        Long dayLong = Long.valueOf(day[2]);
//
//        String[]timeString = splited[1].split(":");
//        Long hour = Long.valueOf(timeString[0]);
//        Long minute = Long.valueOf(timeString[1]);
//        Long second = Long.valueOf(timeString[2]);
//        Long finalTime = year*1000000+month*10000+dayLong*100+hour;
//
//        return finalTime;
//    }
//}
