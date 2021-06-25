package com.recoveryrecord.surveyandroid.example.DbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.recoveryrecord.surveyandroid.example.sqlite.PushNews;

public class PushNewsDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "push_news.db";
    private static final String TABLE_NAME_PUSH_NEWS = "push_news";
    private static final String KEY_ID = "id";
    private static final String KEY_DOC_ID = "doc_id";
    private static final String KEY_DEVICE_ID = "device_id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_NEWS_ID = "news_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_MEDIA = "media";
    private static final String KEY_PUBDATE = "pubdate";
    private static final String KEY_NOTI_TIMESTAMP = "noti_timestamp";
    private static final String KEY_RECEIEVE_TIMESTAMP = "receieve_timestamp";
    private static final String KEY_OPEN_TIMESTAMP = "open_timestamp";
    private static final String KEY_REMOVE_TIMESTAMP = "remove_timestamp";
    private static final String KEY_REMOVE_TYPE = "remove_type";
    private static final String KEY_TYPE = "type";
    private static final String KEY_CLICK = "click";

    public PushNewsDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME_PUSH_NEWS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_DOC_ID + " TEXT,"
                + KEY_DEVICE_ID + " TEXT,"
                + KEY_USER_ID + " TEXT,"
                + KEY_NEWS_ID + " TEXT,"
                + KEY_TITLE + " TEXT,"
                + KEY_MEDIA + " TEXT,"
                + KEY_PUBDATE + " INT,"
                + KEY_NOTI_TIMESTAMP + " INT,"
                + KEY_RECEIEVE_TIMESTAMP + " INT,"
                + KEY_OPEN_TIMESTAMP + " INT,"
                + KEY_REMOVE_TIMESTAMP + " INT,"
                + KEY_REMOVE_TYPE + " TEXT,"
                + KEY_TYPE + " TEXT,"
                + KEY_CLICK + " INT"+ ")";
        db.execSQL(CREATE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PUSH_NEWS);
        // Create tables again
        onCreate(db);
    }
    // **** CRUD (Create, Read, Update, Delete) Operations ***** //

    // Adding new User Details
    public void insertPushNewsDetailsCreate(PushNews pushnews){
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_DOC_ID, pushnews.getKEY_DOC_ID());
        cValues.put(KEY_DEVICE_ID, pushnews.getKEY_DEVICE_ID());
        cValues.put(KEY_USER_ID, pushnews.getKEY_USER_ID());
        cValues.put(KEY_NEWS_ID, pushnews.getKEY_NEWS_ID());
        cValues.put(KEY_TITLE, pushnews.getKEY_TITLE());
        cValues.put(KEY_MEDIA, pushnews.getKEY_MEDIA());
        cValues.put(KEY_PUBDATE, pushnews.getKEY_PUBDATE());
        cValues.put(KEY_NOTI_TIMESTAMP, pushnews.getKEY_NOTI_TIMESTAMP());
        cValues.put(KEY_RECEIEVE_TIMESTAMP, pushnews.getKEY_RECEIEVE_TIMESTAMP());
        cValues.put(KEY_OPEN_TIMESTAMP, pushnews.getKEY_OPEN_TIMESTAMP());
        cValues.put(KEY_REMOVE_TIMESTAMP, pushnews.getKEY_REMOVE_TIMESTAMP());
        cValues.put(KEY_REMOVE_TYPE, pushnews.getKEY_REMOVE_TYPE());
        cValues.put(KEY_TYPE, pushnews.getKEY_TYPE());
        cValues.put(KEY_CLICK, pushnews.getKEY_CLICK());
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_NAME_PUSH_NEWS,null, cValues);
        db.close();
    }

    public void UpdatePushNewsDetailsReceieve(PushNews pushnews){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_RECEIEVE_TIMESTAMP, pushnews.getKEY_RECEIEVE_TIMESTAMP());
        cValues.put(KEY_CLICK, 0);
        db.update(TABLE_NAME_PUSH_NEWS, cValues, KEY_DOC_ID + " = ?", new String[]{String.valueOf(pushnews.getKEY_DOC_ID())});
    }

    public void UpdatePushNewsDetailsRemove(PushNews pushnews){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_REMOVE_TIMESTAMP, pushnews.getKEY_REMOVE_TIMESTAMP());
        cValues.put(KEY_REMOVE_TYPE, pushnews.getKEY_REMOVE_TYPE());
        db.update(TABLE_NAME_PUSH_NEWS, cValues, KEY_DOC_ID + " = ?", new String[]{String.valueOf(pushnews.getKEY_DOC_ID())});
    }

    public void UpdatePushNewsDetailsClick(PushNews pushnews){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_OPEN_TIMESTAMP, pushnews.getKEY_OPEN_TIMESTAMP());
        cValues.put(KEY_CLICK, 1);
        db.update(TABLE_NAME_PUSH_NEWS, cValues, KEY_DOC_ID + " = ?", new String[]{String.valueOf(pushnews.getKEY_DOC_ID())});
    }

    public Cursor getNotiDataForESM(long now_timestamp) {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        Cursor res =  db.rawQuery( "SELECT tmp.news_id, tmp.title\n" +
                        "                FROM\n" +
                        "                        (\n" +
                        "                                SELECT DISTINCT pn.news_id,\n" +
                        "                                pn.title,\n" +
                        "                                pn.noti_timestamp,\n" +
                        "                                (" + now_timestamp + "-pn.noti_timestamp) as diff\n" +
                        "                                FROM push_news pn\n" +
                        "                                WHERE pn.type = 'target add'" +
                        "                        ) as tmp\n" +
                        "                WHERE tmp.diff <= 3600\n" +
                        "                ORDER BY tmp.noti_timestamp DESC;"
        , null );
        return res;
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
