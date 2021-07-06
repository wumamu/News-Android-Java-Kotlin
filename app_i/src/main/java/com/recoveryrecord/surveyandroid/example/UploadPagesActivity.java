package com.recoveryrecord.surveyandroid.example;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.recoveryrecord.surveyandroid.example.DbHelper.DiaryDbHelper;
import com.recoveryrecord.surveyandroid.example.DbHelper.ESMDbHelper;
import com.recoveryrecord.surveyandroid.example.DbHelper.PushNewsDbHelper;
import com.recoveryrecord.surveyandroid.example.DbHelper.ReadingBehaviorDbHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import static com.recoveryrecord.surveyandroid.example.Constants.*;

public class UploadPagesActivity extends AppCompatActivity {
//    Boolean set_once = false;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    String device_id = "";

    private Button mybutton;
    private TextView uptime, intro;

    @SuppressLint({"HardwareIds", "SetTextI18n"})
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("資料上傳頁面");
        setContentView(R.layout.activity_upload);
        device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        @SuppressLint("SimpleDateFormat")
        final SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//        intro = findViewById(R.id.intro);
        uptime = findViewById(R.id.uptextview);
        mybutton = findViewById(R.id.upbutton);


        final Date date = new Date();
        date.setTime(sharedPrefs.getLong(UPLOAD_TIME, 0)*1000);

//        String[] mysplit = formatter.format(date).split(" ");
//        intro.setText("此上傳機制為確保App資料完整性，您將上傳 新聞閱讀記錄、以及問卷填答記錄（ESM, Diary)。");
        uptime.setText("此上傳機制為確保App資料完整性，您將上傳 新聞閱讀記錄、以及問卷填答記錄（ESM, Diary)。\n\n上次上傳時間\n" + formatter.format(date) + "\n(請每日至少上傳一次)");

        mybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_push_news();
                upload_reading_behavior();
                upload_esm();
                upload_diary();
                Toast.makeText(getApplicationContext(), "上傳資料完成", Toast.LENGTH_SHORT).show();

                Long new_time = Timestamp.now().getSeconds();
                Date new_date = new Date();
                new_date.setTime(new_time*1000);
                uptime.setText("此上傳機制為確保App資料完整性，您將上傳 新聞閱讀記錄、以及問卷填答記錄（ESM, Diary)。\n\n上次上傳時間\n" + formatter.format(new_date));

                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putLong(UPLOAD_TIME, new_time);
                editor.apply();
            }
        });






    }

    private void upload_diary() {
        DiaryDbHelper dbHandler = new DiaryDbHelper(getApplicationContext());
        Cursor cursor = dbHandler.getALL();
        // Move to first row
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                // cursor.getString(cursor.getColumnIndex("news_id"))
                Map<String, Object> diary = new HashMap<>();
                diary.put(PUSH_DIARY_DOC_ID, cursor.getString(cursor.getColumnIndex("doc_id")));
                diary.put(PUSH_DIARY_DEVICE_ID, cursor.getString(cursor.getColumnIndex("device_id")));
                diary.put(PUSH_DIARY_USER_ID, cursor.getString(cursor.getColumnIndex("user_id")));

                diary.put(PUSH_DIARY_SCHEDULE_SOURCE, cursor.getString(cursor.getColumnIndex("diary_schedule_source")));
                diary.put(PUSH_DIARY_SAMPLE_TIME, new Timestamp(cursor.getLong(cursor.getColumnIndex("diary_sample_time")), 0));//
                diary.put(PUSH_DIARY_OPTION, new ArrayList<String>(Arrays.asList(cursor.getString(cursor.getColumnIndex("esm_sample")).split("#"))));

                diary.put(PUSH_DIARY_NOTI_TIME, new Timestamp(cursor.getLong(cursor.getColumnIndex("noti_timestamp")), 0));
                diary.put(PUSH_DIARY_RECEIEVE_TIME, new Timestamp(cursor.getLong(cursor.getColumnIndex("receieve_timestamp")), 0));
                diary.put(PUSH_DIARY_OPEN_TIME, new Timestamp(cursor.getLong(cursor.getColumnIndex("open_timestamp")), 0));
                diary.put(PUSH_DIARY_CLOSE_TIME, new Timestamp(cursor.getLong(cursor.getColumnIndex("close_timestamp")), 0));
                diary.put(PUSH_DIARY_SUBMIT_TIME, new Timestamp(cursor.getLong(cursor.getColumnIndex("submit_timestamp")), 0));//
                diary.put(PUSH_DIARY_REMOVE_TIME, new Timestamp(cursor.getLong(cursor.getColumnIndex("remove_timestamp")), 0));
                diary.put(PUSH_DIARY_REMOVE_TYPE, cursor.getString(cursor.getColumnIndex("remove_type")));
                diary.put(PUSH_DIARY_RESULT, cursor.getString(cursor.getColumnIndex("result")));
                diary.put(PUSH_DIARY_INOPPORTUNE_TARGET, new ArrayList<String>(Arrays.asList(cursor.getString(cursor.getColumnIndex("inopportune_result")).split("#"))));
                diary.put(PUSH_DIARY_OPPORTUNE_TARGET, new ArrayList<String>(Arrays.asList(cursor.getString(cursor.getColumnIndex("opportune_result")).split("#"))));

                db.collection(PUSH_DIARY_COLLECTION+"_sql")
                        .document(cursor.getString(cursor.getColumnIndex("doc_id")))
                        .set(diary);
                cursor.moveToNext();
            }
            cursor.close();
        }
        dbHandler.UpdateAll();
//        dbHandler.deleteDb();
    }

    private void upload_esm() {
        ESMDbHelper dbHandler = new ESMDbHelper(getApplicationContext());
        Cursor cursor = dbHandler.getALL();
        // Move to first row
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                // cursor.getString(cursor.getColumnIndex("news_id"))
                Map<String, Object> esm = new HashMap<>();
                esm.put(PUSH_ESM_DOC_ID, cursor.getString(cursor.getColumnIndex("doc_id")));
                esm.put(PUSH_ESM_DEVICE_ID, cursor.getString(cursor.getColumnIndex("device_id")));
                esm.put(PUSH_ESM_USER_ID, cursor.getString(cursor.getColumnIndex("user_id")));

                esm.put(PUSH_ESM_TYPE, cursor.getInt(cursor.getColumnIndex("esm_type")));
                esm.put(PUSH_ESM_NOTI_ARRAY, new ArrayList<String>(Arrays.asList(cursor.getString(cursor.getColumnIndex("noti_sample")).split("#"))));
                esm.put(PUSH_ESM_READ_ARRAY, new ArrayList<String>(Arrays.asList(cursor.getString(cursor.getColumnIndex("self_read_sample")).split("#"))));
                //new ArrayList<String>(Arrays.asList(cursor.getString(cursor.getColumnIndex("self_read_sample")).split("#")))
                esm.put(PUSH_ESM_SCHEDULE_ID, cursor.getString(cursor.getColumnIndex("esm_schedule_id")));
                esm.put(PUSH_ESM_SCHEDULE_SOURCE, cursor.getString(cursor.getColumnIndex("esm_schedule_source")));
                esm.put(PUSH_ESM_SAMPLE_TIME, new Timestamp(cursor.getLong(cursor.getColumnIndex("esm_sample_time")), 0));//
                esm.put(PUSH_ESM_SAMPLE, cursor.getInt(cursor.getColumnIndex("sample")));
                esm.put(PUSH_ESM_SAMPLE_ID, cursor.getString(cursor.getColumnIndex("select_diary_id")));


                esm.put(PUSH_ESM_NOTI_TIME, new Timestamp(cursor.getLong(cursor.getColumnIndex("noti_timestamp")), 0));
                esm.put(PUSH_ESM_RECEIEVE_TIME, new Timestamp(cursor.getLong(cursor.getColumnIndex("receieve_timestamp")), 0));
                esm.put(PUSH_ESM_OPEN_TIME, new Timestamp(cursor.getLong(cursor.getColumnIndex("open_timestamp")), 0));
                esm.put(PUSH_ESM_CLOSE_TIME, new Timestamp(cursor.getLong(cursor.getColumnIndex("close_timestamp")), 0));
                esm.put(PUSH_ESM_SUBMIT_TIME, new Timestamp(cursor.getLong(cursor.getColumnIndex("submit_timestamp")), 0));//
                esm.put(PUSH_ESM_REMOVE_TIME, new Timestamp(cursor.getLong(cursor.getColumnIndex("remove_timestamp")), 0));
                esm.put(PUSH_ESM_REMOVE_TYPE, cursor.getString(cursor.getColumnIndex("remove_type")));

                esm.put(PUSH_ESM_RESULT, cursor.getString(cursor.getColumnIndex("result")));
                esm.put(PUSH_ESM_TARGET_NEWS_ID, cursor.getString(cursor.getColumnIndex("noti_read_news_id")));//receieve 1 click 0
                esm.put(PUSH_ESM_TARGET_TITLE, cursor.getString(cursor.getColumnIndex("noti_read_title")));//receieve 1 click 0
                esm.put(PUSH_ESM_TARGET_IN_TIME, cursor.getString(cursor.getColumnIndex("noti_read_in_time")));//receieve 1 click 0
                esm.put(PUSH_ESM_TARGET_SITUATION, cursor.getString(cursor.getColumnIndex("noti_read_situation")));//receieve 1 click 0
                esm.put(PUSH_ESM_TARGET_PLACE, cursor.getString(cursor.getColumnIndex("noti_read_place")));//receieve 1 click 0

                db.collection(PUSH_ESM_COLLECTION+"_sql")
                        .document(cursor.getString(cursor.getColumnIndex("doc_id")))
                        .set(esm);
                cursor.moveToNext();
            }
            cursor.close();
        }
        dbHandler.UpdateAll();
//        dbHandler.deleteDb();
    }

    private void upload_reading_behavior() {
        ReadingBehaviorDbHelper dbHandler = new ReadingBehaviorDbHelper(getApplicationContext());
        Cursor cursor = dbHandler.getALL();
        // Move to first row
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                // cursor.getString(cursor.getColumnIndex("news_id"))
                Map<String, Object> rb = new HashMap<>();
                rb.put(READING_BEHAVIOR_DOC_ID, cursor.getString(cursor.getColumnIndex("doc_id")));
                rb.put(READING_BEHAVIOR_DEVICE_ID, cursor.getString(cursor.getColumnIndex("device_id")));
                rb.put(READING_BEHAVIOR_USER_ID, cursor.getString(cursor.getColumnIndex("user_id")));
                rb.put(READING_BEHAVIOR_SAMPLE_CHECK_ID, cursor.getString(cursor.getColumnIndex("select_esm_id")));
                rb.put(READING_BEHAVIOR_TRIGGER_BY, cursor.getString(cursor.getColumnIndex("trigger_by")));
                rb.put(READING_BEHAVIOR_NEWS_ID, cursor.getString(cursor.getColumnIndex("news_id")));
                rb.put(READING_BEHAVIOR_TITLE, cursor.getString(cursor.getColumnIndex("title")));
                rb.put(READING_BEHAVIOR_MEDIA, cursor.getString(cursor.getColumnIndex("media")));
                rb.put(READING_BEHAVIOR_HAS_IMAGE, cursor.getInt(cursor.getColumnIndex("has_img")));
                rb.put(READING_BEHAVIOR_PUBDATE, new Timestamp(cursor.getLong(cursor.getColumnIndex("pubdate")), 0));
                rb.put(READING_BEHAVIOR_ROW_SPACING, cursor.getInt(cursor.getColumnIndex("row_spacing")));
                rb.put(READING_BEHAVIOR_BYTE_PER_LINE, cursor.getInt(cursor.getColumnIndex("byte_per_line")));
                rb.put(READING_BEHAVIOR_FONT_SIZE, cursor.getInt(cursor.getColumnIndex("font_size")));
                rb.put(READING_BEHAVIOR_CONTENT_LENGTH, cursor.getInt(cursor.getColumnIndex("content_length")));
                rb.put(READING_BEHAVIOR_DISPLAY_WIDTH, cursor.getFloat(cursor.getColumnIndex("display_width")));
                rb.put(READING_BEHAVIOR_DISPLAY_HEIGHT, cursor.getFloat(cursor.getColumnIndex("display_height")));
                rb.put(READING_BEHAVIOR_IN_TIME, new Timestamp(cursor.getLong(cursor.getColumnIndex("in_timestamp")), 0));;
                rb.put(READING_BEHAVIOR_OUT_TIME, new Timestamp(cursor.getLong(cursor.getColumnIndex("out_timestamp")), 0));;
                rb.put(READING_BEHAVIOR_TIME_ON_PAGE, cursor.getLong(cursor.getColumnIndex("time_on_page")));
                rb.put(READING_BEHAVIOR_PAUSE_COUNT, cursor.getInt(cursor.getColumnIndex("pause_on_page")));
                rb.put(READING_BEHAVIOR_VIEWPORT_NUM, cursor.getInt(cursor.getColumnIndex("view_port_num")));
                rb.put(READING_BEHAVIOR_VIEWPORT_RECORD, new ArrayList<String>(Arrays.asList(cursor.getString(cursor.getColumnIndex("view_port_record")).split("#"))));//
                rb.put(READING_BEHAVIOR_FLING_NUM, cursor.getInt(cursor.getColumnIndex("fling_num")));
                rb.put(READING_BEHAVIOR_FLING_RECORD, new ArrayList<String>(Arrays.asList(cursor.getString(cursor.getColumnIndex("fling_record")).split("#"))));//
                rb.put(READING_BEHAVIOR_DRAG_NUM, cursor.getInt(cursor.getColumnIndex("drag_num")));
                rb.put(READING_BEHAVIOR_DRAG_RECORD, new ArrayList<String>(Arrays.asList(cursor.getString(cursor.getColumnIndex("drag_counter")).split("#"))));//
                rb.put(READING_BEHAVIOR_SHARE, new ArrayList<String>(Arrays.asList(cursor.getString(cursor.getColumnIndex("share")).split("#"))));//
                rb.put(READING_BEHAVIOR_TIME_SERIES, new ArrayList<String>(Arrays.asList(cursor.getString(cursor.getColumnIndex("time_series")).split("#"))));//

                db.collection(READING_BEHAVIOR_COLLECTION+"_sql")
                        .document(cursor.getString(cursor.getColumnIndex("doc_id")))
                        .set(rb);
                cursor.moveToNext();
            }
            cursor.close();
        }
        dbHandler.UpdateAll();
//        dbHandler.deleteDb();
    }

    private void upload_push_news() {
        PushNewsDbHelper dbHandler = new PushNewsDbHelper(getApplicationContext());
        Cursor cursor = dbHandler.getALL();
        // Move to first row
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                // cursor.getString(cursor.getColumnIndex("news_id"))
                Map<String, Object> pn = new HashMap<>();
                pn.put(PUSH_NEWS_DOC_ID, cursor.getString(cursor.getColumnIndex("doc_id")));
                pn.put(PUSH_NEWS_DEVICE_ID, cursor.getString(cursor.getColumnIndex("device_id")));
                pn.put(PUSH_NEWS_USER_ID, cursor.getString(cursor.getColumnIndex("user_id")));

                pn.put(PUSH_NEWS_ID, cursor.getString(cursor.getColumnIndex("news_id")));
                pn.put(PUSH_NEWS_TITLE, cursor.getString(cursor.getColumnIndex("title")));
                pn.put(PUSH_NEWS_MEDIA, cursor.getString(cursor.getColumnIndex("media")));
                pn.put(PUSH_NEWS_PUBDATE, new Timestamp(cursor.getLong(cursor.getColumnIndex("pubdate")), 0));//

                pn.put(PUSH_NEWS_NOTI_TIME, new Timestamp(cursor.getLong(cursor.getColumnIndex("noti_timestamp")), 0));
                pn.put(PUSH_NEWS_RECEIEVE_TIME, new Timestamp(cursor.getLong(cursor.getColumnIndex("receieve_timestamp")), 0));
                pn.put(PUSH_NEWS_OPEN_TIME, new Timestamp(cursor.getLong(cursor.getColumnIndex("open_timestamp")), 0));
                pn.put(PUSH_NEWS_REMOVE_TIME, new Timestamp(cursor.getLong(cursor.getColumnIndex("remove_timestamp")), 0));
                pn.put(PUSH_NEWS_REMOVE_TYPE, cursor.getString(cursor.getColumnIndex("remove_type")));

                pn.put(PUSH_NEWS_TYPE, cursor.getString(cursor.getColumnIndex("type")));
                pn.put(PUSH_NEWS_CLICK, cursor.getString(cursor.getColumnIndex("click")));//receieve 1 click 0

                db.collection(PUSH_NEWS_COLLECTION+"_sql")
                        .document(cursor.getString(cursor.getColumnIndex("doc_id")))
                        .set(pn);
                cursor.moveToNext();
            }
            cursor.close();
        }
        dbHandler.UpdateAll();
//        dbHandler.deleteDb();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
