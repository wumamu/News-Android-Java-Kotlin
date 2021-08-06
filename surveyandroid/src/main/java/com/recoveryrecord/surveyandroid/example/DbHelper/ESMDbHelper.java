package com.recoveryrecord.surveyandroid.example.DbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//import com.recoveryrecord.surveyandroid.example.sqlite.ESM;
//import com.recoveryrecord.surveyandroid.example.sqlite.ESMContract;
//import com.recoveryrecord.surveyandroid.example.sqlite.PushNews;

import com.recoveryrecord.surveyandroid.example.sqlite.ESM;

import java.util.ArrayList;
import java.util.HashMap;

public class ESMDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ESM.db";
//    private SQLiteDatabase db;
    private static final String TABLE_NAME_PUSH_ESM = "push_esm";
    private static final String KEY_ID = "id";

    private static final String KEY_DOC_ID = "doc_id";
    private static final String KEY_DEVICE_ID = "device_id";
    private static final String KEY_USER_ID = "user_id";

    private static final String KEY_ESM_TYPE = "esm_type";//0 noti, 1 self read
    private static final String KEY_NOTI_SAMPLE = "noti_sample";//by notification
    private static final String KEY_SELF_READ_SAMPLE = "self_read_sample";//self trigger

    //initiate
    private static final String KEY_ESM_SCHEDULE_ID = "esm_schedule_id";
    private static final String KEY_ESM_SCHEDULE_SOURCE = "esm_schedule_source";
    //open esm query db
    private static final String KEY_ESM_SAMPLE_TIME = "esm_sample_time";
    private static final String KEY_SAMPLE = "sample";
    //default 0
    //answer without read 1
    //answer with read 2 -> 3
    //open diary
    private static final String KEY_SELECT_DIARY_ID = "select_diary_id";

    private static final String KEY_NOTI_TIMESTAMP = "noti_timestamp";
    private static final String KEY_RECEIEVE_TIMESTAMP = "receieve_timestamp";
    private static final String KEY_OPEN_TIMESTAMP = "open_timestamp";
    private static final String KEY_CLOSE_TIMESTAMP = "close_timestamp";
    private static final String KEY_SUBMIT_TIMESTAMP = "submit_timestamp";
    private static final String KEY_REMOVE_TIMESTAMP = "remove_timestamp";
    private static final String KEY_REMOVE_TYPE = "remove_type";

    private static final String KEY_RESULT = "result";
    //type 0
    private static final String KEY_NOTI_READ_NEWS_ID = "noti_read_news_id";
    private static final String KEY_NOTI_READ_TITLE = "noti_read_title";
    private static final String KEY_NOTI_READ_IN_TIME = "noti_read_in_time";
    private static final String KEY_NOTI_READ_RECEIEVE_TIME = "noti_read_receieve_time";
    private static final String KEY_NOTI_READ_SITUATION = "noti_read_situation";
    private static final String KEY_NOTI_READ_PLACE = "noti_read_place";
    private static final String KEY_NOTI_NOT_READ_NEWS_ID = "noti_not_read_news_id";
    private static final String KEY_NOTI_NOT_READ_TITLE = "noti_not_read_title";
    private static final String KEY_NOTI_NOT_READ_RECEIEVE_TIME = "noti_not_read_receieve_time";
    //type 1
    private static final String KEY_SELF_READ_NEWS_ID = "self_read_news_id";
    private static final String KEY_SELF_READ_TITLE = "self_read_title";
    private static final String KEY_SELF_READ_IN_TIME = "self_read_in_time";
    private static final String KEY_SELF_READ_RECEIEVE_TIME = "self_read_receieve_time";
    private static final String KEY_SELF_READ_SITUATION = "self_read_situation";
    private static final String KEY_SELF_READ_PLACE = "self_read_place";




    public ESMDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME_PUSH_ESM + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_DOC_ID + " TEXT,"
                + KEY_DEVICE_ID + " TEXT,"
                + KEY_USER_ID + " TEXT,"

                + KEY_ESM_TYPE + " INT,"//00 or 1
                + KEY_NOTI_SAMPLE + " TEXT,"
                + KEY_SELF_READ_SAMPLE + " TEXT,"

                + KEY_ESM_SCHEDULE_ID + " TEXT,"
                + KEY_ESM_SCHEDULE_SOURCE + " TEXT,"
                + KEY_ESM_SAMPLE_TIME + " INT,"//long
                + KEY_SAMPLE + " INT,"
                + KEY_SELECT_DIARY_ID + " TEXT,"

                + KEY_NOTI_TIMESTAMP + " INT,"
                + KEY_RECEIEVE_TIMESTAMP + " INT,"//string
                + KEY_OPEN_TIMESTAMP + " INT,"
                + KEY_CLOSE_TIMESTAMP + " INT,"
                + KEY_SUBMIT_TIMESTAMP + " INT,"
                + KEY_REMOVE_TIMESTAMP + " INT,"
                + KEY_REMOVE_TYPE + " TEXT,"

                + KEY_RESULT + " TEXT,"//long

                + KEY_NOTI_READ_NEWS_ID + " TEXT,"
                + KEY_NOTI_READ_TITLE + " TEXT,"
                + KEY_NOTI_READ_IN_TIME + " TEXT,"
                + KEY_NOTI_READ_RECEIEVE_TIME + " TEXT,"
                + KEY_NOTI_READ_SITUATION + " TEXT,"
                + KEY_NOTI_READ_PLACE + " TEXT,"
                + KEY_NOTI_NOT_READ_NEWS_ID + " TEXT,"
                + KEY_NOTI_NOT_READ_TITLE + " TEXT,"
                + KEY_NOTI_NOT_READ_RECEIEVE_TIME + " TEXT,"

                + KEY_SELF_READ_NEWS_ID + " TEXT,"
                + KEY_SELF_READ_TITLE + " TEXT,"
                + KEY_SELF_READ_IN_TIME + " INT,"
                + KEY_SELF_READ_RECEIEVE_TIME + " INT,"
                + KEY_SELF_READ_SITUATION + " TEXT,"
                + KEY_SELF_READ_PLACE + " TEXT"+ ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PUSH_ESM);
        // Create tables again
        onCreate(db);
    }

    public void insertPushESMDetailsCreate(ESM esm){
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_DOC_ID, esm.getKEY_DOC_ID());//initiate
        cValues.put(KEY_DEVICE_ID, esm.getKEY_DEVICE_ID());//initiate
        cValues.put(KEY_USER_ID, esm.getKEY_USER_ID());//initiate

        cValues.put(KEY_ESM_TYPE, esm.getKEY_ESM_TYPE());//click
        cValues.put(KEY_NOTI_SAMPLE, esm.getKEY_NOTI_SAMPLE());//click
        cValues.put(KEY_SELF_READ_SAMPLE, esm.getKEY_SELF_READ_SAMPLE());//click

        cValues.put(KEY_ESM_SCHEDULE_ID, esm.getKEY_ESM_SCHEDULE_ID());//initiate
        cValues.put(KEY_ESM_SCHEDULE_SOURCE, esm.getKEY_ESM_SCHEDULE_SOURCE());//initiate
        cValues.put(KEY_ESM_SAMPLE_TIME, esm.getKEY_ESM_SAMPLE_TIME());//click
        cValues.put(KEY_SAMPLE, esm.getKEY_SAMPLE());//unknow ????????
        cValues.put(KEY_SELECT_DIARY_ID, esm.getKEY_SELECT_DIARY_ID());//diary sample

        cValues.put(KEY_NOTI_TIMESTAMP, esm.getKEY_NOTI_TIMESTAMP());//initiate
        cValues.put(KEY_RECEIEVE_TIMESTAMP, esm.getKEY_RECEIEVE_TIMESTAMP());//receieve
        cValues.put(KEY_OPEN_TIMESTAMP, esm.getKEY_OPEN_TIMESTAMP());//click
        cValues.put(KEY_CLOSE_TIMESTAMP, esm.getKEY_CLOSE_TIMESTAMP());//close
        cValues.put(KEY_SUBMIT_TIMESTAMP, esm.getKEY_SUBMIT_TIMESTAMP());//submit
        cValues.put(KEY_REMOVE_TIMESTAMP, esm.getKEY_REMOVE_TIMESTAMP());//remove
        cValues.put(KEY_REMOVE_TYPE, esm.getKEY_REMOVE_TYPE());//remove

        cValues.put(KEY_RESULT, esm.getKEY_RESULT());//submit

        cValues.put(KEY_NOTI_READ_NEWS_ID, esm.getKEY_NOTI_READ_NEWS_ID());//submit
        cValues.put(KEY_NOTI_READ_TITLE, esm.getKEY_NOTI_READ_TITLE());//submit
        cValues.put(KEY_NOTI_READ_IN_TIME, esm.getKEY_NOTI_READ_IN_TIME());//submit
        cValues.put(KEY_NOTI_READ_RECEIEVE_TIME, esm.getKEY_NOTI_READ_RECEIEVE_TIME());//submit
        cValues.put(KEY_NOTI_READ_SITUATION, esm.getKEY_NOTI_READ_SITUATION());//submit
        cValues.put(KEY_NOTI_READ_PLACE, esm.getKEY_NOTI_READ_PLACE());//submit
        cValues.put(KEY_NOTI_NOT_READ_NEWS_ID, esm.getKEY_NOTI_NOT_READ_NEWS_ID());//submit
        cValues.put(KEY_NOTI_NOT_READ_TITLE, esm.getKEY_NOTI_NOT_READ_TITLE());//submit
        cValues.put(KEY_NOTI_NOT_READ_RECEIEVE_TIME, esm.getKEY_NOTI_NOT_READ_RECEIEVE_TIME());//submit

        cValues.put(KEY_SELF_READ_NEWS_ID, esm.getKEY_SELF_READ_NEWS_ID());//submit
        cValues.put(KEY_SELF_READ_TITLE, esm.getKEY_SELF_READ_TITLE());//submit
        cValues.put(KEY_SELF_READ_IN_TIME, esm.getKEY_SELF_READ_IN_TIME());//submit
        cValues.put(KEY_SELF_READ_RECEIEVE_TIME, esm.getKEY_SELF_READ_RECEIEVE_TIME());//submit
        cValues.put(KEY_SELF_READ_SITUATION, esm.getKEY_SELF_READ_SITUATION());//submit
        cValues.put(KEY_SELF_READ_PLACE, esm.getKEY_SELF_READ_PLACE());//submit
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_NAME_PUSH_ESM,null, cValues);
        db.close();
    }

    public void UpdatePushESMDetailsReceieve(ESM esm){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_RECEIEVE_TIMESTAMP, esm.getKEY_RECEIEVE_TIMESTAMP());
        db.update(TABLE_NAME_PUSH_ESM, cValues, KEY_DOC_ID + " = ?", new String[]{String.valueOf(esm.getKEY_DOC_ID())});
    }

    public void UpdatePushESMDetailsClick(ESM esm){
        //esm loading page
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_ESM_TYPE, esm.getKEY_ESM_TYPE());
        cValues.put(KEY_NOTI_SAMPLE, esm.getKEY_NOTI_SAMPLE());
        cValues.put(KEY_SELF_READ_SAMPLE, esm.getKEY_SELF_READ_SAMPLE());
        cValues.put(KEY_ESM_SAMPLE_TIME, esm.getKEY_ESM_SAMPLE_TIME());

        db.update(TABLE_NAME_PUSH_ESM, cValues, KEY_DOC_ID + " = ?", new String[]{String.valueOf(esm.getKEY_DOC_ID())});
    }

    public void UpdatePushESMDetailsOpen(ESM esm){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_OPEN_TIMESTAMP, esm.getKEY_OPEN_TIMESTAMP());
        db.update(TABLE_NAME_PUSH_ESM, cValues, KEY_DOC_ID + " = ?", new String[]{String.valueOf(esm.getKEY_DOC_ID())});
    }

    public void UpdatePushESMDetailsClose(ESM esm){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_CLOSE_TIMESTAMP, esm.getKEY_CLOSE_TIMESTAMP());
        db.update(TABLE_NAME_PUSH_ESM, cValues, KEY_DOC_ID + " = ?", new String[]{String.valueOf(esm.getKEY_DOC_ID())});
    }

    public void UpdatePushESMDetailsSubmit(ESM esm){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_SUBMIT_TIMESTAMP, esm.getKEY_SUBMIT_TIMESTAMP());
        cValues.put(KEY_RESULT, esm.getKEY_RESULT());

        cValues.put(KEY_NOTI_READ_NEWS_ID, esm.getKEY_NOTI_READ_NEWS_ID());
        cValues.put(KEY_NOTI_READ_TITLE, esm.getKEY_NOTI_READ_TITLE());
        cValues.put(KEY_NOTI_READ_IN_TIME, esm.getKEY_NOTI_READ_IN_TIME());
        cValues.put(KEY_NOTI_READ_RECEIEVE_TIME, esm.getKEY_NOTI_READ_RECEIEVE_TIME());
        cValues.put(KEY_NOTI_READ_SITUATION, esm.getKEY_NOTI_READ_SITUATION());
        cValues.put(KEY_NOTI_READ_PLACE, esm.getKEY_NOTI_READ_PLACE());
        cValues.put(KEY_NOTI_NOT_READ_NEWS_ID, esm.getKEY_NOTI_NOT_READ_NEWS_ID());
        cValues.put(KEY_NOTI_NOT_READ_TITLE, esm.getKEY_NOTI_NOT_READ_TITLE());
        cValues.put(KEY_NOTI_NOT_READ_RECEIEVE_TIME, esm.getKEY_NOTI_NOT_READ_RECEIEVE_TIME());

        cValues.put(KEY_SELF_READ_NEWS_ID, esm.getKEY_SELF_READ_NEWS_ID());
        cValues.put(KEY_SELF_READ_TITLE, esm.getKEY_SELF_READ_TITLE());
        cValues.put(KEY_SELF_READ_IN_TIME, esm.getKEY_SELF_READ_IN_TIME());
        cValues.put(KEY_SELF_READ_RECEIEVE_TIME, esm.getKEY_SELF_READ_RECEIEVE_TIME());
        cValues.put(KEY_SELF_READ_SITUATION, esm.getKEY_SELF_READ_SITUATION());
        cValues.put(KEY_SELF_READ_PLACE, esm.getKEY_SELF_READ_PLACE());

        db.update(TABLE_NAME_PUSH_ESM, cValues, KEY_DOC_ID + " = ?", new String[]{String.valueOf(esm.getKEY_DOC_ID())});
    }

    public void UpdatePushESMDetailsRemove(ESM esm){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_REMOVE_TIMESTAMP, esm.getKEY_REMOVE_TIMESTAMP());
        cValues.put(KEY_REMOVE_TYPE, esm.getKEY_REMOVE_TYPE());
        db.update(TABLE_NAME_PUSH_ESM, cValues, KEY_DOC_ID + " = ?", new String[]{String.valueOf(esm.getKEY_DOC_ID())});
    }

    public Cursor getNotiDataForDiaryNoti(long now_timestamp) {//15 hour
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        Cursor res =  db.rawQuery( "SELECT tmp.noti_read_news_id, tmp.noti_read_title, tmp.noti_read_receieve_time, tmp.noti_read_situation, tmp.noti_read_place, tmp.esm_type\n" +
                        "FROM\n" +
                        "(\n" +
                        "\tSELECT DISTINCT esm.noti_read_news_id, \n" +
                        "\t\t\t\t\tesm.noti_read_title,\n" +
                        "\t\t\t\t\tesm.noti_read_receieve_time,\n" +
                        "\t\t\t\t\tesm.noti_read_situation,\n" +
                        "\t\t\t\t\tesm.noti_read_place,\n" +
                        "\t\t\t\t\tesm.esm_type,\n" +
                        "\t\t\t\t\t(" + now_timestamp + "-esm.receieve_timestamp) as diff\n" +
                        "\tFROM push_esm esm\n" +
                        "\tWHERE esm.esm_type=0 OR esm.esm_type=2\n" +
                        ") as tmp\n" +
                        "WHERE tmp.diff <= 54000 AND tmp.diff >=0\n" +
                        "ORDER BY diff ASC;"
                , null );
        return res;
    }

    public Cursor getNotiDataForDiaryRead(long now_timestamp) {//15 hour
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        Cursor res =  db.rawQuery( "SELECT tmp.noti_read_news_id, tmp.noti_read_title, tmp.noti_read_in_time, tmp.self_read_situation, tmp.self_read_place, tmp.esm_type\n" +
                        "FROM\n" +
                        "(\n" +
                        "\tSELECT DISTINCT esm.noti_read_news_id, \n" +
                        "\t\t\t\t\tesm.noti_read_title,\n" +
                        "\t\t\t\t\tesm.noti_read_in_time,\n" +
                        "\t\t\t\t\tesm.self_read_situation,\n" +
                        "\t\t\t\t\tesm.self_read_place,\n" +
                        "\t\t\t\t\tesm.esm_type,\n" +
                        "\t\t\t\t\t(" + now_timestamp + "-esm.receieve_timestamp) as diff\n" +
                        "\tFROM push_esm esm\n" +
                        "\tWHERE esm.esm_type=0 OR esm.esm_type=1\n" +
                        ") as tmp\n" +
                        "WHERE tmp.diff <= 54000 AND tmp.diff >=0\n" +
                        "ORDER BY diff ASC;"
                , null );
        return res;
    }

    public Cursor getALL() {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        Cursor res =  db.rawQuery( "SELECT  * FROM " + TABLE_NAME_PUSH_ESM + ";", null );
        return res;
    }

    public void UpdateAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_USER_ID, "upload");
        db.update(TABLE_NAME_PUSH_ESM, cValues, null, null);

    }

    public Cursor getAllPush() {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        Cursor res =  db.rawQuery( "SELECT * FROM " + TABLE_NAME_PUSH_ESM + ";", null );
        return res;
    }

    public Cursor getAllDone() {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        Cursor res =  db.rawQuery( "SELECT * FROM " + TABLE_NAME_PUSH_ESM + " WHERE push_esm.result <> 'NA' ;", null );
        return res;
    }

    public Cursor getDayPush(long todays, long todaye) {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        Cursor res =  db.rawQuery( "SELECT * FROM " + TABLE_NAME_PUSH_ESM + " WHERE push_esm.noti_timestamp >= " + todays + " AND push_esm.noti_timestamp <= " + todaye + ";", null );
        return res;
    }

    public Cursor getDayDone(long todays, long todaye) {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        Cursor res =  db.rawQuery( "SELECT * FROM " + TABLE_NAME_PUSH_ESM + " WHERE push_esm.noti_timestamp >= " + todays + " AND push_esm.noti_timestamp <= " + todaye + " AND push_esm.result <> 'NA';", null );
        return res;
    }

    public void deleteDb() {
        // on below line we are creating
        // a variable to write our database.
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME_PUSH_ESM);
        db.close();
    }
}
