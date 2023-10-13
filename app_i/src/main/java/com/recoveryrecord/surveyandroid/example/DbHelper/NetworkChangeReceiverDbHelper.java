//package com.recoveryrecord.surveyandroid.example.DbHelper;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import com.recoveryrecord.surveyandroid.example.sqlite.AppUsage;
//import com.recoveryrecord.surveyandroid.example.sqlite.Network;
//
//public class NetworkChangeReceiverDbHelper extends SQLiteOpenHelper {
//    private static final int DATABASE_VERSION = 1;
//    private static final String DATABASE_NAME = "network.db";
//    private static final String TABLE_NAME_NETWORK = "network";
//    private static final String KEY_DOC_ID = "doc_id";
//    private static final String KEY_ID = "id";
//    private static final String KEY_TIMESTAMP = "timestamp";
//    private static final String KEY_DEVICE_ID = "device_id";
//    private static final String KEY_SESSION = "session";
//    private static final String KEY_USING_APP = "using_app";
//    private static final String KEY_NETWORK = "network";
//    private static final String KEY_USER_ID = "user_id";
//
//
//    public NetworkChangeReceiverDbHelper(Context context){
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME_NETWORK + "("
//                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                + KEY_DOC_ID + " TEXT,"
//                + KEY_TIMESTAMP + " INT,"
//                + KEY_DEVICE_ID + " TEXT,"
//                + KEY_USER_ID + " TEXT,"
//                + KEY_SESSION + " INT,"
//                + KEY_USING_APP + " TEXT,"
//                + KEY_NETWORK + " TEXT"+ ")";
//        db.execSQL(CREATE_TABLE);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        // Drop older table if exist
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_NETWORK);
//        // Create tables again
//        onCreate(db);
//    }
//    // **** CRUD (Create, Read, Update, Delete) Operations ***** //
//
//    // Adding new User Details
//    public void insertNetworkDetailsCreate(Network network){
//        //Get the Data Repository in write mode
//        SQLiteDatabase db = this.getWritableDatabase();
//        //Create a new map of values, where column names are the keys
//        ContentValues cValues = new ContentValues();
//        cValues.put(KEY_DOC_ID, network.getKEY_DOC_ID());
//        cValues.put(KEY_TIMESTAMP, network.getKEY_TIMESTAMP());
//        cValues.put(KEY_DEVICE_ID, network.getKEY_DEVICE_ID());
//        cValues.put(KEY_USER_ID, network.getKEY_USER_ID());
//        cValues.put(KEY_SESSION, network.getKEY_SESSION());
//        cValues.put(KEY_USING_APP, network.getKEY_USING_APP());
//        cValues.put(KEY_NETWORK, network.getKEY_NETWORK());
//        // Insert the new row, returning the primary key value of the new row
//        long newRowId = db.insert(TABLE_NAME_NETWORK,null, cValues);
//        db.close();
//    }
//
//    //    contentValues.put(key,values);
//    public void UpdateNetworkReceiver(Network network){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cValues = new ContentValues();
//        cValues.put(KEY_DOC_ID, network.getKEY_DOC_ID());
//        cValues.put(KEY_TIMESTAMP, network.getKEY_TIMESTAMP());
//        cValues.put(KEY_DEVICE_ID, network.getKEY_DEVICE_ID());
//        cValues.put(KEY_USER_ID, network.getKEY_USER_ID());
//        cValues.put(KEY_SESSION, network.getKEY_SESSION());
//        cValues.put(KEY_USING_APP, network.getKEY_USING_APP());
//        cValues.put(KEY_NETWORK, network.getKEY_NETWORK());
//        db.update(TABLE_NAME_NETWORK, cValues, KEY_DOC_ID + " = ?", new String[]{String.valueOf(network.getKEY_DOC_ID())});
//
//    }
//    public Cursor getALL() {
//        SQLiteDatabase db = this.getReadableDatabase();
////        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
//        Cursor res =  db.rawQuery( "SELECT  * FROM " + TABLE_NAME_NETWORK + " as tmp WHERE tmp.user_id <> 'upload';", null );
//        return res;
//    }
//
//    public void UpdateAll(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cValues = new ContentValues();
//        cValues.put(KEY_USER_ID, "upload");
//        db.update(TABLE_NAME_NETWORK, cValues, null, null);
//
//    }
//}
