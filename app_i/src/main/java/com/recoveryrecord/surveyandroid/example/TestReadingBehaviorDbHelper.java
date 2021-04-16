package com.recoveryrecord.surveyandroid.example;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.recoveryrecord.surveyandroid.example.sqlite.ReadingBehavior;

public class TestReadingBehaviorDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "reading_behavior.db";
    private static final String TABLE_NAME_READING_BEHAVIOR = "reading_behaviors";
    private static final String KEY_ID = "id";
    private static final String KEY_NEWS_ID = "news_id";
    private static final String KEY_TRIGGER_BY = "trigger_by";
    private static final String KEY_TIME_IN = "time_in";
    private static final String KEY_TIME_OUT = "time_out";
    private static final String KEY_CONTENT_LENGTH = "content_length";
    private static final String KEY_DISPLAY_WIDTH = "display_width";
    private static final String KEY_DISPLAY_HEIGHT = "display_height";
    private static final String KEY_TIME_ON_PAGE = "time_on_page";
    private static final String KEY_PAUSE_ON_PAGE = "pause_on_page";
    private static final String KEY_VIEW_PORT_NUM = "view_port_num";
    private static final String KEY_VIEW_PORT_RECORD = "view_port_record";
    private static final String KEY_FLING_NUM = "fling_num";
    private static final String KEY_FLING_RECORD = "fling_record";
    private static final String KEY_DRAG_NUM = "drag_num";
    private static final String KEY_DRAG_RECORD = "drag_counter";
    private static final String KEY_SHARE = "share";
    private static final String KEY_TIME_SERIES = "time_series";

    public TestReadingBehaviorDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME_READING_BEHAVIOR + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NEWS_ID + " TEXT,"
                + KEY_TRIGGER_BY + " TEXT,"
                + KEY_TIME_IN + " TEXT,"
                + KEY_TIME_OUT + " TEXT,"
                + KEY_CONTENT_LENGTH + " TEXT,"
                + KEY_DISPLAY_WIDTH + " TEXT,"
                + KEY_DISPLAY_HEIGHT + " TEXT,"
                + KEY_TIME_ON_PAGE + " TEXT,"
                + KEY_PAUSE_ON_PAGE + " INT,"
                + KEY_VIEW_PORT_NUM + " INT,"
                + KEY_VIEW_PORT_RECORD + " TEXT,"
                + KEY_FLING_NUM + " INT,"
                + KEY_FLING_RECORD + " TEXT,"
                + KEY_DRAG_NUM + " INT,"
                + KEY_DRAG_RECORD + " INT,"
                + KEY_SHARE + " INT,"
                + KEY_TIME_SERIES + " TEXT"+ ")";
        db.execSQL(CREATE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_READING_BEHAVIOR);
        // Create tables again
        onCreate(db);
    }
    // **** CRUD (Create, Read, Update, Delete) Operations ***** //

    // Adding new User Details
    void insertReadingBehaviorDetails(ReadingBehavior readingBehavior){
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_NEWS_ID, readingBehavior.getKEY_NEWS_ID());
        cValues.put(KEY_TRIGGER_BY, readingBehavior.getKEY_TRIGGER_BY());
        cValues.put(KEY_TIME_IN, readingBehavior.getKEY_TIME_IN());
        cValues.put(KEY_TIME_OUT, readingBehavior.getKEY_TIME_OUT());
        cValues.put(KEY_CONTENT_LENGTH, readingBehavior.getKEY_CONTENT_LENGTH());
        cValues.put(KEY_DISPLAY_WIDTH, readingBehavior.getKEY_DISPLAY_WIDTH());
        cValues.put(KEY_DISPLAY_HEIGHT, readingBehavior.getKEY_DISPLAY_HEIGHT());
        cValues.put(KEY_TIME_ON_PAGE, readingBehavior.getKEY_TIME_ON_PAGE());
        cValues.put(KEY_PAUSE_ON_PAGE, readingBehavior.getKEY_PAUSE_ON_PAGE());
        cValues.put(KEY_VIEW_PORT_NUM, readingBehavior.getKEY_VIEW_PORT_NUM());
        cValues.put(KEY_VIEW_PORT_RECORD, readingBehavior.getKEY_VIEW_PORT_RECORD());
        cValues.put(KEY_FLING_NUM, readingBehavior.getKEY_FLING_NUM());
        cValues.put(KEY_FLING_RECORD, readingBehavior.getKEY_FLING_RECORD());
        cValues.put(KEY_DRAG_NUM, readingBehavior.getKEY_DRAG_NUM());
        cValues.put(KEY_DRAG_RECORD, readingBehavior.getKEY_DRAG_RECORD());
        cValues.put(KEY_SHARE, readingBehavior.getKEY_SHARE());
        cValues.put(KEY_TIME_SERIES, readingBehavior.getKEY_TIME_SERIES());
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_NAME_READING_BEHAVIOR,null, cValues);
        db.close();
    }



    // Get User Details
//    public ArrayList<HashMap<String, String>> GetNotifications(){
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        ArrayList<HashMap<String, String>> notificationList = new ArrayList<>();
////        String query = "SELECT packagename, tickertext, time, notititle, notitext FROM "+ TABLE_NAME_NOTIFICATION;
//        String query = "SELECT * FROM "+ TABLE_NAME_READING_BEHAVIOR;
//        Cursor cursor = db.rawQuery(query,null);
//        while (cursor.moveToNext()){
//            HashMap<String, String> notification = new HashMap<>();
//            notification.put("packagename",cursor.getString(cursor.getColumnIndex(KEY_NEWS_ID)));
//            notification.put("time",cursor.getString(cursor.getColumnIndex(KEY_TIME_IN)));
//            notification.put("tickertext",cursor.getString(cursor.getColumnIndex(KEY_TRIGGER_BY)));
//            notification.put("notititle",cursor.getString(cursor.getColumnIndex(KEY_DISPLAY_WIDTH)));
//            notification.put("notitext",cursor.getString(cursor.getColumnIndex(KEY_TIME_OUT)));
//            notificationList.add(notification);
//        }
//        return  notificationList;
//    }


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
