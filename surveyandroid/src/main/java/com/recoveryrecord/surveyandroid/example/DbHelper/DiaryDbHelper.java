package com.recoveryrecord.surveyandroid.example.DbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.recoveryrecord.surveyandroid.example.sqlite.Diary;


public class DiaryDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Diary.db";
//    private SQLiteDatabase db;
    private static final String TABLE_NAME_PUSH_DIARY = "push_diary";
    private static final String KEY_ID = "id";

    private static final String KEY_DOC_ID = "doc_id";
    private static final String KEY_DEVICE_ID = "device_id";
    private static final String KEY_USER_ID = "user_id";

    //initiate
    private static final String KEY_DIARY_SCHEDULE_SOURCE = "diary_schedule_source";
    //open esm query db
    private static final String KEY_DIARY_SAMPLE_TIME = "diary_sample_time";
    private static final String KEY_ESM_RESULT_SAMPLE_READ = "esm_sample_read";
    private static final String KEY_ESM_RESULT_SAMPLE_NOTI = "esm_sample_noti";


    private static final String KEY_NOTI_TIMESTAMP = "noti_timestamp";
    private static final String KEY_RECEIEVE_TIMESTAMP = "receieve_timestamp";
    private static final String KEY_OPEN_TIMESTAMP = "open_timestamp";
    private static final String KEY_CLOSE_TIMESTAMP = "close_timestamp";
    private static final String KEY_SUBMIT_TIMESTAMP = "submit_timestamp";
    private static final String KEY_REMOVE_TIMESTAMP = "remove_timestamp";
    private static final String KEY_REMOVE_TYPE = "remove_type";

    private static final String KEY_RESULT = "result";
    private static final String KEY_INOPPORTUNTE_RESULT_READ = "inopportune_result_read";
    private static final String KEY_OPPORTUNTE_RESULT_READ = "opportune_result_read";
    private static final String KEY_INOPPORTUNTE_RESULT_NOTI = "inopportune_result_noti";
    private static final String KEY_OPPORTUNTE_RESULT_NOTI = "opportune_result_noti";

    public DiaryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME_PUSH_DIARY + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_DOC_ID + " TEXT,"
                + KEY_DEVICE_ID + " TEXT,"
                + KEY_USER_ID + " TEXT,"

                + KEY_ESM_RESULT_SAMPLE_READ + " TEXT,"
                + KEY_ESM_RESULT_SAMPLE_NOTI + " TEXT,"
                + KEY_DIARY_SCHEDULE_SOURCE + " TEXT,"
                + KEY_DIARY_SAMPLE_TIME + " INT,"//long


                + KEY_NOTI_TIMESTAMP + " INT,"
                + KEY_RECEIEVE_TIMESTAMP + " INT,"//string
                + KEY_OPEN_TIMESTAMP + " INT,"
                + KEY_CLOSE_TIMESTAMP + " INT,"
                + KEY_SUBMIT_TIMESTAMP + " INT,"
                + KEY_REMOVE_TIMESTAMP + " INT,"
                + KEY_REMOVE_TYPE + " TEXT,"

                + KEY_RESULT + " TEXT,"
                + KEY_INOPPORTUNTE_RESULT_NOTI + " TEXT,"
                + KEY_OPPORTUNTE_RESULT_NOTI + " TEXT,"
                + KEY_INOPPORTUNTE_RESULT_READ + " TEXT,"
                + KEY_OPPORTUNTE_RESULT_READ + " TEXT"+ ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PUSH_DIARY);
        // Create tables again
        onCreate(db);
    }

    public void insertPushDiaryDetailsCreate(Diary diary){
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_DOC_ID, diary.getKEY_DOC_ID());//initiate
        cValues.put(KEY_DEVICE_ID, diary.getKEY_DEVICE_ID());//initiate
        cValues.put(KEY_USER_ID, diary.getKEY_USER_ID());//initiate

        cValues.put(KEY_ESM_RESULT_SAMPLE_READ, diary.getKEY_ESM_RESULT_SAMPLE_READ());//click
        cValues.put(KEY_ESM_RESULT_SAMPLE_NOTI, diary.getKEY_ESM_RESULT_SAMPLE_NOTI());//click
        cValues.put(KEY_DIARY_SCHEDULE_SOURCE, diary.getKEY_DIARY_SCHEDULE_SOURCE());//initiate
        cValues.put(KEY_DIARY_SAMPLE_TIME, diary.getKEY_DIARY_SAMPLE_TIME());//click

        cValues.put(KEY_NOTI_TIMESTAMP, diary.getKEY_NOTI_TIMESTAMP());//initiate
        cValues.put(KEY_RECEIEVE_TIMESTAMP, diary.getKEY_RECEIEVE_TIMESTAMP());//receieve
        cValues.put(KEY_OPEN_TIMESTAMP, diary.getKEY_OPEN_TIMESTAMP());//click
        cValues.put(KEY_CLOSE_TIMESTAMP, diary.getKEY_CLOSE_TIMESTAMP());//close
        cValues.put(KEY_SUBMIT_TIMESTAMP, diary.getKEY_SUBMIT_TIMESTAMP());//submit
        cValues.put(KEY_REMOVE_TIMESTAMP, diary.getKEY_REMOVE_TIMESTAMP());//remove
        cValues.put(KEY_REMOVE_TYPE, diary.getKEY_REMOVE_TYPE());//remove

        cValues.put(KEY_RESULT, diary.getKEY_RESULT());//submit
        cValues.put(KEY_INOPPORTUNTE_RESULT_READ, diary.getKEY_INOPPORTUNTE_RESULT_READ());//submit
        cValues.put(KEY_OPPORTUNTE_RESULT_READ, diary.getKEY_OPPORTUNTE_RESULT_READ());//submit
        cValues.put(KEY_INOPPORTUNTE_RESULT_NOTI, diary.getKEY_INOPPORTUNTE_RESULT_NOTI());//submit
        cValues.put(KEY_OPPORTUNTE_RESULT_NOTI, diary.getKEY_OPPORTUNTE_RESULT_NOTI());//submit

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_NAME_PUSH_DIARY,null, cValues);
        db.close();
    }

    public void UpdatePushDiaryDetailsReceieve(Diary diary){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_RECEIEVE_TIMESTAMP, diary.getKEY_RECEIEVE_TIMESTAMP());
        db.update(TABLE_NAME_PUSH_DIARY, cValues, KEY_DOC_ID + " = ?", new String[]{String.valueOf(diary.getKEY_DOC_ID())});
    }

    public void UpdatePushDiaryDetailsClick(Diary diary){
        //esm loading page
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_ESM_RESULT_SAMPLE_READ, diary.getKEY_ESM_RESULT_SAMPLE_READ());
        cValues.put(KEY_ESM_RESULT_SAMPLE_NOTI, diary.getKEY_ESM_RESULT_SAMPLE_NOTI());
        cValues.put(KEY_DIARY_SAMPLE_TIME, diary.getKEY_DIARY_SAMPLE_TIME());

        db.update(TABLE_NAME_PUSH_DIARY, cValues, KEY_DOC_ID + " = ?", new String[]{String.valueOf(diary.getKEY_DOC_ID())});
    }

    public void UpdatePushDiaryDetailsOpen(Diary diary){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_OPEN_TIMESTAMP, diary.getKEY_OPEN_TIMESTAMP());
        db.update(TABLE_NAME_PUSH_DIARY, cValues, KEY_DOC_ID + " = ?", new String[]{String.valueOf(diary.getKEY_DOC_ID())});
    }

    public void UpdatePushDiaryDetailsClose(Diary diary){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_CLOSE_TIMESTAMP, diary.getKEY_CLOSE_TIMESTAMP());
        db.update(TABLE_NAME_PUSH_DIARY, cValues, KEY_DOC_ID + " = ?", new String[]{String.valueOf(diary.getKEY_DOC_ID())});
    }

    public void UpdatePushDiaryDetailsSubmit(Diary diary){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_SUBMIT_TIMESTAMP, diary.getKEY_SUBMIT_TIMESTAMP());
        cValues.put(KEY_RESULT, diary.getKEY_RESULT());
        cValues.put(KEY_INOPPORTUNTE_RESULT_READ, diary.getKEY_INOPPORTUNTE_RESULT_READ());
        cValues.put(KEY_OPPORTUNTE_RESULT_READ, diary.getKEY_OPPORTUNTE_RESULT_READ());
        cValues.put(KEY_INOPPORTUNTE_RESULT_NOTI, diary.getKEY_INOPPORTUNTE_RESULT_NOTI());
        cValues.put(KEY_OPPORTUNTE_RESULT_NOTI, diary.getKEY_OPPORTUNTE_RESULT_NOTI());
        db.update(TABLE_NAME_PUSH_DIARY, cValues, KEY_DOC_ID + " = ?", new String[]{String.valueOf(diary.getKEY_DOC_ID())});
    }

    public void UpdatePushESMDetailsRemove(Diary diary){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_REMOVE_TIMESTAMP, diary.getKEY_REMOVE_TIMESTAMP());
        cValues.put(KEY_REMOVE_TYPE, diary.getKEY_REMOVE_TYPE());
        db.update(TABLE_NAME_PUSH_DIARY, cValues, KEY_DOC_ID + " = ?", new String[]{String.valueOf(diary.getKEY_DOC_ID())});
    }

    public Cursor getALL() {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        Cursor res =  db.rawQuery( "SELECT  * FROM " + TABLE_NAME_PUSH_DIARY + ";", null );
        return res;
    }

    public void UpdateAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_USER_ID, "upload");
        db.update(TABLE_NAME_PUSH_DIARY, cValues, null, null);

    }

    public Cursor getAllPush() {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        Cursor res =  db.rawQuery( "SELECT * FROM " + TABLE_NAME_PUSH_DIARY + ";", null );
        return res;
    }

    public Cursor getAllDone() {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        Cursor res =  db.rawQuery( "SELECT * FROM " + TABLE_NAME_PUSH_DIARY + " WHERE push_diary.result <> 'NA' ;", null );
        return res;
    }

    public Cursor getDayPush(long todays, long todaye) {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        Cursor res =  db.rawQuery( "SELECT * FROM " + TABLE_NAME_PUSH_DIARY + " WHERE push_diary.noti_timestamp >= " + todays + " AND push_diary.noti_timestamp <= " + todaye + ";", null );
        return res;
    }

    public Cursor getDayDone(long todays, long todaye) {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        Cursor res =  db.rawQuery( "SELECT * FROM " + TABLE_NAME_PUSH_DIARY + " WHERE push_diary.noti_timestamp >= " + todays + " AND push_diary.noti_timestamp <= " + todaye + " AND push_diary.result <> 'NA';", null );
        return res;
    }

    public void deleteDb() {
        // on below line we are creating
        // a variable to write our database.
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME_PUSH_DIARY);
        db.close();
    }
}
