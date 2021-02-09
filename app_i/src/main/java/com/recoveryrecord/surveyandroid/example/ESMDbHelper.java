package com.recoveryrecord.surveyandroid.example;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.jar.JarException;

import androidx.annotation.Nullable;

public class ESMDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ESM.db";
    private static final int DATABASE_VERSION = 1;
    Context context;
    private SQLiteDatabase db;
    private Resources mResources;

    public ESMDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mResources = context.getResources();
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.db = db;
        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                ESMContract.ESMTable.TABLE_NAME + " ( " +
                ESMContract.ESMTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ESMContract.ESMTable.COLUMN_BASE1 + " TEXT, " +
                ESMContract.ESMTable.COLUMN_BASE2 + " TEXT, " +
                ESMContract.ESMTable.COLUMN_NOT_READ1 + " TEXT, " +
                ESMContract.ESMTable.COLUMN_NOT_READ2 + " TEXT, " +
                ESMContract.ESMTable.COLUMN_NOT_READ3 + " TEXT, " +
                ESMContract.ESMTable.COLUMN_NOT_READ4 + " TEXT, " +
                ESMContract.ESMTable.COLUMN_NOT_READ5 + " INTEGER, " +
                ESMContract.ESMTable.COLUMN_READ1 + " TEXT, " +
                ESMContract.ESMTable.COLUMN_READ2 + " TEXT, " +
                ESMContract.ESMTable.COLUMN_READ3 + " INTEGER, " +
                ESMContract.ESMTable.COLUMN_READ4 + " INTEGER, " +
                ESMContract.ESMTable.COLUMN_READ5 + " INTEGER, " +
                ESMContract.ESMTable.COLUMN_READ6 + " INTEGER, " +
                ESMContract.ESMTable.COLUMN_READ7 + " INTEGER, " +
                ESMContract.ESMTable.COLUMN_READ8 + " INTEGER, " +
                ESMContract.ESMTable.COLUMN_READ9 + " INTEGER, " +
                ESMContract.ESMTable.COLUMN_READ10 + " INTEGER, " +
                ESMContract.ESMTable.COLUMN_READ11 + " TEXT, " +
                ESMContract.ESMTable.COLUMN_READ12 + " TEXT, " +
                ESMContract.ESMTable.COLUMN_READ13 + " TEXT, " +
                ESMContract.ESMTable.COLUMN_READ14 + " TEXT, " +
                ESMContract.ESMTable.COLUMN_READ15 + " TEXT, " +
                ESMContract.ESMTable.COLUMN_READ16 + " INTEGER, " +
                ESMContract.ESMTable.COLUMN_READ17 + " TEXT, " +
                ESMContract.ESMTable.COLUMN_NOT_SHARE1 + " INTEGER, " +
                ESMContract.ESMTable.COLUMN_SHARE1 + " TEXT, " +
                ESMContract.ESMTable.COLUMN_SHARE2 + " TEXT" +
                ");";
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        Log.d("log: database", "create db success");
        try {
            readDataToDb(db);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void readDataToDb(SQLiteDatabase db) throws IOException, JSONException {
        final String ESM_BASE1 = "base 1";
        final String ESM_BASE2 = "base 2";
        final String ESM_NOT_READ1 = "not read 1";
        final String ESM_NOT_READ2 = "not read 2";
        final String ESM_NOT_READ3 = "not read 3";
        final String ESM_NOT_READ4 = "not read 4";
        final String ESM_NOT_READ5 = "not read 5";
        final String ESM_READ1 = "read 1";
        final String ESM_READ2 = "read 2";
        final String ESM_READ3 = "read 3";
        final String ESM_READ4 = "read 4";
        final String ESM_READ5 = "read 5";
        final String ESM_READ6 = "read 6";
        final String ESM_READ7 = "read 7";
        final String ESM_READ8 = "read 8";
        final String ESM_READ9 = "read 9";
        final String ESM_READ10 = "read 10";
        final String ESM_READ11 = "read 11";
        final String ESM_READ12 = "read 12";
        final String ESM_READ13 = "read 13";
        final String ESM_READ14 = "read 14";
        final String ESM_READ15 = "read 15";
        final String ESM_READ16 = "read 16";
        final String ESM_READ17 = "read 17";
        final String ESM_NOT_SHARE1 = "not share 1";
        final String ESM_SHARE1 = "share 1";
        final String ESM_SHARE2 = "share 2";

        try {
            String jsonDataString = readJsonDataFromFile();
            JSONArray menuItemsJsonArray = new JSONArray(jsonDataString);
            for (int i = 0; i<menuItemsJsonArray.length(); ++i){
                String BASE1;
                String BASE2;
                String NOT_READ1;
                String NOT_READ2;
                String NOT_READ3;
                String NOT_READ4;
                String NOT_READ5;
                String READ1;
                String READ2;
                String READ3;
                String READ4;
                String READ5;
                String READ6;
                String READ7;
                String READ8;
                String READ9;
                String READ10;
                String READ11;
                String READ12;
                String READ13;
                String READ14;
                String READ15;
                String READ16;
                String READ17;
                String NOT_SHARE1;
                String SHARE1;
                String SHARE2;

                JSONObject menuItemObject = menuItemsJsonArray.getJSONObject(i);

                BASE1 = menuItemObject.getString(ESM_BASE1);
                BASE2 = menuItemObject.getString(ESM_BASE2);
                NOT_READ1 = menuItemObject.getString(ESM_NOT_READ1);
                NOT_READ2 = menuItemObject.getString(ESM_NOT_READ2);
                NOT_READ3 = menuItemObject.getString(ESM_NOT_READ3);
                NOT_READ4 = menuItemObject.getString(ESM_NOT_READ4);
                NOT_READ5 = menuItemObject.getString(ESM_NOT_READ5);
                READ1 = menuItemObject.getString(ESM_READ1);
                READ2 = menuItemObject.getString(ESM_READ2);
                READ3 = menuItemObject.getString(ESM_READ3);
                READ4 = menuItemObject.getString(ESM_READ4);
                READ5 = menuItemObject.getString(ESM_READ5);
                READ6 = menuItemObject.getString(ESM_READ6);
                READ7 = menuItemObject.getString(ESM_READ7);
                READ8 = menuItemObject.getString(ESM_READ8);
                READ9 = menuItemObject.getString(ESM_READ9);
                READ10 = menuItemObject.getString(ESM_READ10);
                READ11 = menuItemObject.getString(ESM_READ11);
                READ12 = menuItemObject.getString(ESM_READ12);
                READ13 = menuItemObject.getString(ESM_READ13);
                READ14 = menuItemObject.getString(ESM_READ14);
                READ15 = menuItemObject.getString(ESM_READ15);
                READ16 = menuItemObject.getString(ESM_READ16);
                READ17 = menuItemObject.getString(ESM_READ17);
                NOT_SHARE1 = menuItemObject.getString(ESM_NOT_SHARE1);
                SHARE1 = menuItemObject.getString(ESM_SHARE1);
                SHARE2 = menuItemObject.getString(ESM_SHARE2);

                ContentValues menuValues = new ContentValues();

                menuValues.put(ESMContract.ESMTable.COLUMN_BASE1, BASE1);
                menuValues.put(ESMContract.ESMTable.COLUMN_BASE2, BASE2);
                menuValues.put(ESMContract.ESMTable.COLUMN_NOT_READ1, NOT_READ1);
                menuValues.put(ESMContract.ESMTable.COLUMN_NOT_READ2, NOT_READ2);
                menuValues.put(ESMContract.ESMTable.COLUMN_NOT_READ3, NOT_READ3);
                menuValues.put(ESMContract.ESMTable.COLUMN_NOT_READ4, NOT_READ4);
                menuValues.put(ESMContract.ESMTable.COLUMN_NOT_READ5, NOT_READ5);
                menuValues.put(ESMContract.ESMTable.COLUMN_READ1, READ1);
                menuValues.put(ESMContract.ESMTable.COLUMN_READ2, READ2);
                menuValues.put(ESMContract.ESMTable.COLUMN_READ3, READ3);
                menuValues.put(ESMContract.ESMTable.COLUMN_READ4, READ4);
                menuValues.put(ESMContract.ESMTable.COLUMN_READ5, READ5);
                menuValues.put(ESMContract.ESMTable.COLUMN_READ6, READ6);
                menuValues.put(ESMContract.ESMTable.COLUMN_READ7, READ7);
                menuValues.put(ESMContract.ESMTable.COLUMN_READ8, READ8);
                menuValues.put(ESMContract.ESMTable.COLUMN_READ9, READ9);
                menuValues.put(ESMContract.ESMTable.COLUMN_READ10, READ10);
                menuValues.put(ESMContract.ESMTable.COLUMN_READ11, READ11);
                menuValues.put(ESMContract.ESMTable.COLUMN_READ12, READ12);
                menuValues.put(ESMContract.ESMTable.COLUMN_READ13, READ13);
                menuValues.put(ESMContract.ESMTable.COLUMN_READ14, READ14);
                menuValues.put(ESMContract.ESMTable.COLUMN_READ15, READ15);
                menuValues.put(ESMContract.ESMTable.COLUMN_READ16, READ16);
                menuValues.put(ESMContract.ESMTable.COLUMN_READ17, READ17);
                menuValues.put(ESMContract.ESMTable.COLUMN_NOT_SHARE1, NOT_SHARE1);
                menuValues.put(ESMContract.ESMTable.COLUMN_SHARE1, SHARE1);
                menuValues.put(ESMContract.ESMTable.COLUMN_SHARE2, SHARE2);

                db.insert(ESMContract.ESMTable.TABLE_NAME, null, menuValues);
                Log.d("log: database", "INSERT SUCCESS");
            }
        } catch (JSONException e) {
            Log.d("log: database", e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private String readJsonDataFromFile() throws IOException {
        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();

        try {
            String jsonDataString = null;
            inputStream = mResources.openRawResource(R.raw.data);
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream, "UTF-8"));
            while ((jsonDataString = bufferedReader.readLine()) != null) {
                builder.append(jsonDataString);
            }
        } finally {
            if (inputStream!=null) {
                inputStream.close();
            }
        }
        return new String(builder);
    }
}
