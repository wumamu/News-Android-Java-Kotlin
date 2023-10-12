package com.recoveryrecord.surveyandroid.example.CSVDataRecord;

import static com.recoveryrecord.surveyandroid.example.config.Config.DATE_FORMAT_for_storing;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "TransportationModeDataRecord")
public class TransportationModeDataRecord implements DataRecord{
    @PrimaryKey(autoGenerate = true)
    private long _id;

    @ColumnInfo(name = "creationTime")
    public long creationTime;

    @ColumnInfo(name = "ConfirmedActivityString")
    public String ConfirmedActivityString; //
    @ColumnInfo(name = "SuspectTime")
    public Long SuspectTime; //
    @ColumnInfo(name = "suspectedStartActivity")
    public String suspectedStartActivity; //
    @ColumnInfo(name = "suspectedEndActivity")
    public String suspectedEndActivity; //

    @ColumnInfo(name = "readable")
    public Long readable;
    @ColumnInfo(name = "sycStatus")
    public Integer syncStatus;

    @ColumnInfo(name = "sessionid")
    public String sessionid;

    @ColumnInfo(name = "phone_sessionid")
    public Long phone_sessionid;
    @ColumnInfo(name = "screenshot")
    public String screenshot;

    @ColumnInfo(name = "ImageName")
    public String ImageName;

//    @ColumnInfo(name = "AccessibilityUrl")
//    public String AccessibilityUrl;
//
//    @ColumnInfo(name = "NotificationUrl")
//    public String NotificationUrl;

    public TransportationModeDataRecord(String ConfirmedActivityString,Long SuspectTime,String suspectedStartActivity,String suspectedEndActivity){
        this.creationTime = new Date().getTime();
        this.ConfirmedActivityString = ConfirmedActivityString;
        this.SuspectTime = SuspectTime;
        this.suspectedStartActivity = suspectedStartActivity;
        this.suspectedEndActivity = suspectedEndActivity;
        this.readable = getReadableTimeLong(this.creationTime);
        this.syncStatus = 0;
        this.sessionid = sessionid;
        this.phone_sessionid = phone_sessionid;
        this.screenshot = screenshot;
        this.ImageName = ImageName;
//        this.AccessibilityUrl = AccessibilityUrl;
//        this.NotificationUrl = NotificationUrl;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    public String getConfirmedActivityString(){
        return ConfirmedActivityString;
    }
    public Long getSuspectTime(){
        return SuspectTime;
    }
    public String getsuspectedStartActivity(){
        return suspectedStartActivity;
    }
    public String getsuspectedEndActivity(){
        return suspectedEndActivity;
    }

    public void setsyncStatus(Integer syncStatus){
        this.syncStatus = syncStatus;
    }

    public Integer getsyncStatus(){
        return this.syncStatus;
    }

    public Long getReadable(){
        return this.readable;
    }

    public String getSessionid() {
        return sessionid;
    }

    public static Long getReadableTimeLong(long time){

        SimpleDateFormat sdf_now = new SimpleDateFormat(DATE_FORMAT_for_storing);
        String currentTimeString = sdf_now.format(time);
        String[] splited = currentTimeString.split("\\s+");
        String[]day = splited[0].split("-");
        Long year = Long.valueOf(day[0]);
        Long month = Long.valueOf(day[1]);
        Long dayLong = Long.valueOf(day[2]);

        String[]timeString = splited[1].split(":");
        Long hour = Long.valueOf(timeString[0]);
        Long minute = Long.valueOf(timeString[1]);
        Long second = Long.valueOf(timeString[2]);
        Long finalTime = year*1000000+month*10000+dayLong*100+hour;

        return finalTime;
    }
}
