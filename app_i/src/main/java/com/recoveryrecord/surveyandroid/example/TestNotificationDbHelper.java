package com.recoveryrecord.surveyandroid.example;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class TestNotificationDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "notifications.db";
    private static final String TABLE_NAME_NOTIFICATION = "notifications";
    private static final String KEY_ID = "id";
    private static final String KEY_PACKAGE_NAME = "packagename";
    private static final String KEY_TICKER_TEXT = "tickertext";
    private static final String KEY_TIME = "time";
    private static final String KEY_TEXT = "notitext";
    private static final String KEY_TITLE = "notititle";
    public TestNotificationDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME_NOTIFICATION + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_PACKAGE_NAME + " TEXT,"
                + KEY_TICKER_TEXT + " TEXT,"
                + KEY_TIME + " TEXT,"
                + KEY_TITLE + " TEXT,"
                + KEY_TEXT + " TEXT"+ ")";
        db.execSQL(CREATE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_NOTIFICATION);
        // Create tables again
        onCreate(db);
    }
    // **** CRUD (Create, Read, Update, Delete) Operations ***** //

    // Adding new User Details
    void insertNotificationDetails(String packagename, String tickertext, String time, String notititle, String notitext){
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_PACKAGE_NAME, packagename);
        cValues.put(KEY_TICKER_TEXT, tickertext);
        cValues.put(KEY_TIME, time);
        cValues.put(KEY_TITLE, notititle);
        cValues.put(KEY_TEXT, notitext);
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_NAME_NOTIFICATION,null, cValues);
        db.close();
    }



    // Get User Details
    public ArrayList<HashMap<String, String>> GetNotifications(){

        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> notificationList = new ArrayList<>();
//        String query = "SELECT packagename, tickertext, time, notititle, notitext FROM "+ TABLE_NAME_NOTIFICATION;
        String query = "SELECT * FROM "+ TABLE_NAME_NOTIFICATION;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String, String> notification = new HashMap<>();
            notification.put("packagename",cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_NAME)));
            notification.put("time",cursor.getString(cursor.getColumnIndex(KEY_TIME)));
            notification.put("tickertext",cursor.getString(cursor.getColumnIndex(KEY_TICKER_TEXT)));
            notification.put("notititle",cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
            notification.put("notitext",cursor.getString(cursor.getColumnIndex(KEY_TEXT)));
            notificationList.add(notification);
        }
        return  notificationList;
    }


//    // Get User Details based on userid
//    public ArrayList<HashMap<String, String>> GetUserByUserId(int notificationid){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ArrayList<HashMap<String, String>> notificationList = new ArrayList<>();
//        String query = "SELECT packagename, tickertext, time, title, text FROM "+ TABLE_Notifications;
//        Cursor cursor = db.query(TABLE_Notifications, new String[]{KEY_PACKAGE_NAME, KEY_TICKER_TEXT, KEY_TIME, KEY_TITLE, KEY_TEXT}, KEY_ID+ "=?",new String[]{String.valueOf(notificationid)},null, null, null, null);
//        if (cursor.moveToNext()){
//            HashMap<String, String> notification = new HashMap<>();
//            notification.put("packagename",cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_NAME)));
//            notification.put("time",cursor.getString(cursor.getColumnIndex(KEY_TIME)));
//            notification.put("tickertext",cursor.getString(cursor.getColumnIndex(KEY_TICKER_TEXT)));
//            notification.put("title",cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
//            notification.put("text",cursor.getString(cursor.getColumnIndex(KEY_TEXT)));
//            notificationList.add(notification);
//        }
//        return  notificationList;
//    }
//    // Delete User Details
//    public void DeleteUser(int userid){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_Notifications, KEY_ID+" = ?",new String[]{String.valueOf(userid)});
//        db.close();
//    }
//    // Update User Details
//    public int UpdateUserDetails(String location, String designation, int id){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cVals = new ContentValues();
//        cVals.put(KEY_TICKER_TEXT, location);
//        cVals.put(KEY_TIME, designation);
//        int count = db.update(TABLE_Notifications, cVals, KEY_ID+" = ?",new String[]{String.valueOf(id)});
//        return  count;
//    }

}