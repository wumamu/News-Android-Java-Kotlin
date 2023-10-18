//package com.recoveryrecord.surveyandroid.example.DbHelper;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.recoveryrecord.surveyandroid.example.sqlite.FBMovement;
//
//import java.util.Collections;
//import java.util.Date;
//
//public class FBMovementDbHelper extends SQLiteOpenHelper {
//    private static final int DATABASE_VERSION = 1;
//    private static final String DATABASE_NAME = "FB_movement.db";
//    private static final String TABLE_NAME_FB_MOVEMENT = "FB_movement";
//    private static final String KEY_ID = "id";
//    private static final String KEY_DOC_ID = "doc_id";
//    private static final String KEY_DEVICE_ID = "device_id";
//    private static final String KEY_USER_ID = "user_id";
//    private static final String KEY_EVENT = "event";
//    private static final String KEY_STATE = "state";
//    private static final String KEY_STATE_TEXT = "state_text";
//    private static final String KEY_CONTENT = "content";
//    private static final String KEY_TIMESTAMP = "timestamp";
//
//    public FBMovementDbHelper(Context context){
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//    @Override
//    public void onCreate(SQLiteDatabase db){
//        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME_FB_MOVEMENT + "("
//                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                + KEY_DOC_ID + " TEXT,"
//                + KEY_DEVICE_ID + " TEXT,"
//                + KEY_USER_ID + " TEXT,"
//                + KEY_EVENT + " TEXT,"
//                + KEY_STATE + " TEXT,"
//                + KEY_STATE_TEXT + " TEXT,"
//                + KEY_CONTENT + " TEXT,"
//                + KEY_TIMESTAMP + " INT"+ ")";
//        db.execSQL(CREATE_TABLE);
//    }
//
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
//        // Drop older table if exist
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FB_MOVEMENT);
//        // Create tables again
//        onCreate(db);
//    }
//
//    public void insertFBMovement(FBMovement fbMovement){
//        //Get the Data Repository in write mode
//        SQLiteDatabase db = this.getWritableDatabase();
//        //Create a new map of values, where column names are the keys
//        ContentValues cValues = new ContentValues();
//        cValues.put(KEY_DOC_ID, fbMovement.getKEY_DOC_ID());
//        cValues.put(KEY_DEVICE_ID, fbMovement.getKEY_DEVICE_ID());
//        cValues.put(KEY_USER_ID, fbMovement.getKEY_USER_ID());
//        cValues.put(KEY_EVENT, fbMovement.getKEY_EVENT());
//        cValues.put(KEY_STATE, fbMovement.getKEY_STATE());
//        cValues.put(KEY_STATE_TEXT, fbMovement.getKEY_STATE_TEXT());
//        cValues.put(KEY_CONTENT, fbMovement.getKEY_CONTENT());
//        cValues.put(KEY_TIMESTAMP, fbMovement.getKEY_TIMESTAMP());
//        // Insert the new row, returning the primary key value of the new row
//        long newRowId = db.insert(TABLE_NAME_FB_MOVEMENT,null, cValues);
//        db.close();
//    }
//
//    public Cursor getALL(){
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "SELECT  * FROM " + TABLE_NAME_FB_MOVEMENT + ";", null );
//        return res;
//    }
//
//    public void deleteByDocId(String docid){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_NAME_FB_MOVEMENT,"doc_id = ?",new String[] {docid});
//    }
//
//    public void deleteByDocIds(String[] docids){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String whereClause = String.format("doc_id" + " in (%s)", new Object[] { TextUtils.join(",", Collections.nCopies(docids.length, "?")) });
//        db.delete(TABLE_NAME_FB_MOVEMENT, whereClause, docids);
//    }
//
//    public void UpdateAll(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cValues = new ContentValues();
//        cValues.put(KEY_USER_ID, "upload");
//        db.update(TABLE_NAME_FB_MOVEMENT, cValues, null, null);
//    }
//
//    public void deleteDb() {
//        // on below line we are creating
//        // a variable to write our database.
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("delete from "+ TABLE_NAME_FB_MOVEMENT);
//        db.close();
//    }
//
//
//}
