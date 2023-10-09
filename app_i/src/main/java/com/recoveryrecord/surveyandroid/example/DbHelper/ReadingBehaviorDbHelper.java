//package com.recoveryrecord.surveyandroid.example.DbHelper;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import com.recoveryrecord.surveyandroid.example.model.ReadingBehavior;
//
//public class ReadingBehaviorDbHelper extends SQLiteOpenHelper {
//    private static final int DATABASE_VERSION = 1;
//    private static final String DATABASE_NAME = "reading_behavior.db";
//    private static final String TABLE_NAME_READING_BEHAVIOR = "reading_behavior";
//    private static final String KEY_ID = "id";
////    private static final String KEY_UPLOAD = "upload";
//    private static final String KEY_DOC_ID = "doc_id";
//    private static final String KEY_DEVICE_ID = "device_id";
//    private static final String KEY_USER_ID = "user_id";
//    private static final String KEY_SELECT_ESM_ID = "select_esm_id";
//    private static final String KEY_TRIGGER_BY = "trigger_by";
//    private static final String KEY_NEWS_ID = "news_id";
//    private static final String KEY_TITLE = "title";
//    private static final String KEY_MEDIA = "media";
//    private static final String KEY_HAS_IMG = "has_img";
//    private static final String KEY_PUBDATE = "pubdate";
//    private static final String KEY_ROW_SPACING = "row_spacing";
//    private static final String KEY_BYTE_PER_LINE = "byte_per_line";
//    private static final String KEY_FONT_SIZE = "font_size";
//    private static final String KEY_CONTENT_LENGTH = "content_length";
//    private static final String KEY_DISPLAY_WIDTH = "display_width";
//    private static final String KEY_DISPLAY_HEIGHT = "display_height";
//    private static final String KEY_IN_TIMESTAMP = "in_timestamp";
//    private static final String KEY_OUT_TIMESTAMP = "out_timestamp";
//    private static final String KEY_TIME_ON_PAGE = "time_on_page";
//    private static final String KEY_PAUSE_ON_PAGE = "pause_on_page";
//    private static final String KEY_VIEW_PORT_NUM = "view_port_num";
//    private static final String KEY_VIEW_PORT_RECORD = "view_port_record";
//    private static final String KEY_FLING_NUM = "fling_num";
//    private static final String KEY_FLING_RECORD = "fling_record";
//    private static final String KEY_DRAG_NUM = "drag_num";
//    private static final String KEY_DRAG_RECORD = "drag_counter";
//    private static final String KEY_SHARE = "share";
//    private static final String KEY_TIME_SERIES = "time_series";
//
//    public ReadingBehaviorDbHelper(Context context){
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//    @Override
//    public void onCreate(SQLiteDatabase db){
//        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME_READING_BEHAVIOR + "("
//                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
////                + KEY_UPLOAD + " INT,"
//                + KEY_DOC_ID + " TEXT,"
//                + KEY_DEVICE_ID + " TEXT,"
//                + KEY_USER_ID + " TEXT,"
//                + KEY_SELECT_ESM_ID + " TEXT,"
//                + KEY_TRIGGER_BY + " TEXT,"
//                + KEY_NEWS_ID + " TEXT,"
//                + KEY_TITLE + " TEXT,"
//                + KEY_MEDIA + " TEXT,"
//                + KEY_HAS_IMG + " INT,"
//                + KEY_PUBDATE + " INT,"
//                + KEY_ROW_SPACING + " INT,"
//                + KEY_BYTE_PER_LINE + " INT,"
//                + KEY_FONT_SIZE + " TEXT,"//string
//                + KEY_CONTENT_LENGTH + " INT,"
//                + KEY_DISPLAY_WIDTH + " FLOAT,"
//                + KEY_DISPLAY_HEIGHT + " FLOAT,"
//                + KEY_IN_TIMESTAMP + " INT,"
//                + KEY_OUT_TIMESTAMP + " INT,"
//                + KEY_TIME_ON_PAGE + " INT,"//long
//                + KEY_PAUSE_ON_PAGE + " INT,"
//                + KEY_VIEW_PORT_NUM + " INT,"
//                + KEY_VIEW_PORT_RECORD + " TEXT,"
//                + KEY_FLING_NUM + " INT,"
//                + KEY_FLING_RECORD + " TEXT,"
//                + KEY_DRAG_NUM + " INT,"
//                + KEY_DRAG_RECORD + " TEXT,"
//                + KEY_SHARE + " TEXT,"
//                + KEY_TIME_SERIES + " TEXT"+ ")";
//        db.execSQL(CREATE_TABLE);
//    }
//
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
//        // Drop older table if exist
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_READING_BEHAVIOR);
//        // Create tables again
//        onCreate(db);
//    }
//
//    public void insertReadingBehaviorDetailsCreate(ReadingBehavior readingBehavior){
//        //Get the Data Repository in write mode
//        SQLiteDatabase db = this.getWritableDatabase();
//        //Create a new map of values, where column names are the keys
//        ContentValues cValues = new ContentValues();
//        cValues.put(KEY_DOC_ID, readingBehavior.kEY_DOC_ID);
//        cValues.put(KEY_DEVICE_ID, readingBehavior.getKEY_DEVICE_ID());
//        cValues.put(KEY_USER_ID, readingBehavior.getKEY_USER_ID());
//        cValues.put(KEY_SELECT_ESM_ID, readingBehavior.getKEY_SELECT_ESM_ID());
//        cValues.put(KEY_TRIGGER_BY, readingBehavior.getKEY_TRIGGER_BY());
//        cValues.put(KEY_NEWS_ID, readingBehavior.getKEY_NEWS_ID());
//        cValues.put(KEY_TITLE, readingBehavior.getKEY_TITLE());
//        cValues.put(KEY_MEDIA, readingBehavior.getKEY_MEDIA());
//        cValues.put(KEY_HAS_IMG, readingBehavior.getKEY_HAS_IMG());
//        cValues.put(KEY_PUBDATE, readingBehavior.getKEY_PUBDATE());
//        cValues.put(KEY_ROW_SPACING, readingBehavior.getKEY_ROW_SPACING());
//        cValues.put(KEY_BYTE_PER_LINE, readingBehavior.getKEY_BYTE_PER_LINE());
//        cValues.put(KEY_FONT_SIZE, readingBehavior.getKEY_FONT_SIZE());
//        cValues.put(KEY_CONTENT_LENGTH, readingBehavior.getKEY_CONTENT_LENGTH());
//        cValues.put(KEY_DISPLAY_WIDTH, readingBehavior.getKEY_DISPLAY_WIDTH());
//        cValues.put(KEY_DISPLAY_HEIGHT, readingBehavior.getKEY_DISPLAY_HEIGHT());
//        cValues.put(KEY_IN_TIMESTAMP, readingBehavior.getKEY_IN_TIMESTAMP());
//        cValues.put(KEY_OUT_TIMESTAMP, readingBehavior.getKEY_OUT_TIMESTAMP());
//        cValues.put(KEY_TIME_ON_PAGE, readingBehavior.getKEY_TIME_ON_PAGE());
//        cValues.put(KEY_PAUSE_ON_PAGE, readingBehavior.getKEY_PAUSE_ON_PAGE());
//        cValues.put(KEY_VIEW_PORT_NUM, readingBehavior.getKEY_VIEW_PORT_NUM());
//        cValues.put(KEY_VIEW_PORT_RECORD, readingBehavior.getKEY_VIEW_PORT_RECORD());
//        cValues.put(KEY_FLING_NUM, readingBehavior.getKEY_FLING_NUM());
//        cValues.put(KEY_FLING_RECORD, readingBehavior.getKEY_FLING_RECORD());
//        cValues.put(KEY_DRAG_NUM, readingBehavior.getKEY_DRAG_NUM());
//        cValues.put(KEY_DRAG_RECORD, readingBehavior.getKEY_DRAG_RECORD());
//        cValues.put(KEY_SHARE, readingBehavior.getKEY_SHARE());
//        cValues.put(KEY_TIME_SERIES, readingBehavior.getKEY_TIME_SERIES());
//        // Insert the new row, returning the primary key value of the new row
//        long newRowId = db.insert(TABLE_NAME_READING_BEHAVIOR,null, cValues);
//        db.close();
//    }
//
//    public boolean UpdateReadingBehaviorDetails(ReadingBehavior readingBehavior){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cValues = new ContentValues();
////        String row_id
//        //KEY_DOC_ID
//        //KEY_DEVICE_ID
//        //KEY_USER_ID
//        //KEY_SELECT_ESM_ID
//        //KEY_TRIGGER_BY
//        //KEY_NEWS_ID
//        cValues.put(KEY_TITLE, readingBehavior.getKEY_TITLE());
//        //KEY_MEDIA
//        cValues.put(KEY_HAS_IMG, readingBehavior.getKEY_HAS_IMG());
//        cValues.put(KEY_PUBDATE, readingBehavior.getKEY_PUBDATE());
//        cValues.put(KEY_ROW_SPACING, readingBehavior.getKEY_ROW_SPACING());
//        cValues.put(KEY_BYTE_PER_LINE, readingBehavior.getKEY_BYTE_PER_LINE());
//        cValues.put(KEY_CONTENT_LENGTH, readingBehavior.getKEY_CONTENT_LENGTH());
//        //KEY_DISPLAY_WIDTH
//        //KEY_DISPLAY_HEIGHT
//        //KEY_IN_TIMESTAMP
//        cValues.put(KEY_OUT_TIMESTAMP, readingBehavior.getKEY_OUT_TIMESTAMP());
//        cValues.put(KEY_TIME_ON_PAGE, readingBehavior.getKEY_TIME_ON_PAGE());
//        cValues.put(KEY_PAUSE_ON_PAGE, readingBehavior.getKEY_PAUSE_ON_PAGE());
//        cValues.put(KEY_VIEW_PORT_NUM, readingBehavior.getKEY_VIEW_PORT_NUM());
//        cValues.put(KEY_VIEW_PORT_RECORD, readingBehavior.getKEY_VIEW_PORT_RECORD());
//        cValues.put(KEY_FLING_NUM, readingBehavior.getKEY_FLING_NUM());
//        cValues.put(KEY_FLING_RECORD, readingBehavior.getKEY_FLING_RECORD());
//        cValues.put(KEY_DRAG_NUM, readingBehavior.getKEY_DRAG_NUM());
//        cValues.put(KEY_DRAG_RECORD, readingBehavior.getKEY_DRAG_RECORD());
////        cValues.put(KEY_SHARE, readingBehavior.getKEY_SHARE());
//        cValues.put(KEY_TIME_SERIES, readingBehavior.getKEY_TIME_SERIES());
//
//        return db.update(TABLE_NAME_READING_BEHAVIOR, cValues, KEY_DOC_ID + " = ?", new String[]{String.valueOf(readingBehavior.kEY_DOC_ID)}) > 0;
//    }
//
//    public Boolean UpdateReadingBehaviorDetailsShare(ReadingBehavior readingBehavior, String type){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cValues = new ContentValues();
//        String doc_id = "\"" + readingBehavior.kEY_DOC_ID + "\"";
//        String apptype = "\"" + type + "\"";
//        cValues.put(KEY_SHARE, apptype);
//        return db.update(TABLE_NAME_READING_BEHAVIOR, cValues, KEY_DOC_ID + " = ?", new String[]{String.valueOf(readingBehavior.kEY_DOC_ID)}) > 0;
//    }
//
//    public Cursor getShare(ReadingBehavior readingBehavior) {
//        SQLiteDatabase db_r = this.getReadableDatabase();
//        String doc_id = "\"" + readingBehavior.kEY_DOC_ID + "\"";
//        Cursor res = db_r.rawQuery("SELECT DISTINCT rb.title, rb.share\n" +
//                        "FROM reading_behavior rb\n" +
//                        "WHERE rb.doc_id = " + doc_id + "\n" +
//                        "ORDER BY rb.in_timestamp DESC;"
//                , null);
//        return res;
//    }
//
//    public Cursor getReadingDataForESM(long now_timestamp) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "SELECT tmp.news_id, tmp.title, tmp.media, tmp.share, tmp.trigger_by, tmp.in_timestamp\n" +
//                        "FROM\n" +
//                        "(\n" +
//                        "\tSELECT DISTINCT rb.news_id, \n" +
//                        "\t\t\t\t\trb.title, \n" +
//                        "\t\t\t\t\trb.media,\n" +
//                        "\t\t\t\t\trb.share,\n" +
//                        "\t\t\t\t\trb.trigger_by,\n" +
//                        "\t\t\t\t\trb.in_timestamp,\n" +
//                        "\t\t\t\t\t(" + now_timestamp + "-rb.in_timestamp) as diff\n" +
//                        "\tFROM reading_behavior rb\n" +
//                        "\tWHERE rb.time_on_page > 1\n" +
//                        ") as tmp\n" +
//                        "WHERE tmp.diff <= 1800 AND tmp.diff >=0\n" +
//                        "ORDER BY tmp.in_timestamp DESC;"
//                , null );
//        return res;
//    }
//    //only need in time
//    public Cursor getReadingDataFromNoti(long now_timestamp, String target_news_id) {//45
////        Stringtarget_news_id
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "SELECT tmp.in_timestamp\n" +
//                        "FROM\n" +
//                        "(\n" +
//                        "\tSELECT DISTINCT rb.news_id, \n" +
////                        "\t\t\t\t\trb.title, \n" +
////                        "\t\t\t\t\trb.media,\n" +
////                        "\t\t\t\t\trb.share,\n" +
////                        "\t\t\t\t\trb.trigger_by,\n" +
//                        "\t\t\t\t\trb.time_on_page,\n" +
//                        "\t\t\t\t\trb.in_timestamp,\n" +
//                        "\t\t\t\t\t(" + now_timestamp + "-rb.in_timestamp) as diff\n" +
//                        "\tFROM reading_behavior rb\n" +
////                        "\tWHERE rb.time_on_page > 1\n" +
//                        "\tWHERE rb.news_id = " + target_news_id + "\n" +
//                        ") as tmp\n" +
//                        "WHERE tmp.diff <= 1800 AND tmp.diff >=0\n" +
//                        "ORDER BY tmp.time_on_page DESC LIMIT 1;"
//                , null );
//        return res;
//    }
//    //id title media in_time
//    public Cursor getReadingDataSelf(long now_timestamp) {//60
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "SELECT tmp.in_timestamp, tmp.title, tmp.media, tmp.news_id\n" +
//                        "FROM\n" +
//                        "(\n" +
//                        "\tSELECT DISTINCT rb.news_id, \n" +
//                        "\t\t\t\t\trb.time_on_page,\n" +
//                        "\t\t\t\t\trb.media,\n" +
//                        "\t\t\t\t\trb.title,\n" +
//                        "\t\t\t\t\trb.in_timestamp,\n" +
//                        "\t\t\t\t\t(" + now_timestamp + "-rb.in_timestamp) as diff\n" +
//                        "\tFROM reading_behavior rb\n" +
//                        "\tWHERE rb.time_on_page > 1\n" +
////                        "\tWHERE rb.news_id = " + target_news_id + "\n" +
//                        ") as tmp\n" +
//                        "WHERE tmp.diff <= 1800 AND tmp.diff >=0\n" +
////                        "ORDER BY tmp.in_timestamp DESC;"
//                        " ORDER BY RANDOM() LIMIT 1;"
//                , null );
//        return res;
//    }
//
//    public Cursor checkReadDataForESM(long now_timestamp) {//45
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "SELECT tmp.in_timestamp, tmp.title, tmp.media, tmp.news_id\n" +
//                        "FROM\n" +
//                        "(\n" +
//                        "\tSELECT DISTINCT rb.news_id, \n" +
//                        "\t\t\t\t\trb.time_on_page,\n" +
//                        "\t\t\t\t\trb.media,\n" +
//                        "\t\t\t\t\trb.title,\n" +
//                        "\t\t\t\t\trb.in_timestamp,\n" +
//                        "\t\t\t\t\t(" + now_timestamp + "-rb.in_timestamp) as diff\n" +
//                        "\tFROM reading_behavior rb\n" +
//                        "\tWHERE rb.time_on_page > 1\n" +
////                        "\tWHERE rb.news_id = " + target_news_id + "\n" +
//                        ") as tmp\n" +
//                        "WHERE tmp.diff <= 1800 AND tmp.diff >=0\n" +
//                        "LIMIT 1;"
//                , null );
//        return res;
//    }
//
//    public Cursor getALL() {
//        SQLiteDatabase db = this.getReadableDatabase();
////        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
////        Cursor res =  db.rawQuery( "SELECT  * FROM " + TABLE_NAME_READING_BEHAVIOR + " as tmp WHERE tmp.user_id <> 'upload';", null );
//        Cursor res =  db.rawQuery( "SELECT  * FROM " + TABLE_NAME_READING_BEHAVIOR + ";", null );
//        return res;
//    }
//
//    public void UpdateAll(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cValues = new ContentValues();
//        cValues.put(KEY_USER_ID, "upload");
//        db.update(TABLE_NAME_READING_BEHAVIOR, cValues, null, null);
//
//    }
//
//    public void deleteDb() {
//        // on below line we are creating
//        // a variable to write our database.
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("delete from "+ TABLE_NAME_READING_BEHAVIOR);
//        db.close();
//
//    }
//
//
//}
