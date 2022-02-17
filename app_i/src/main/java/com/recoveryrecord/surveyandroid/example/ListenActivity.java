package com.recoveryrecord.surveyandroid.example;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class ListenActivity extends AppCompatActivity {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView tvhello;
    private TextView pushNews, pushEsm, pushDiary;

    String tmpNews = "";
    String tmpEsm = "";
    String tmpDiary = "";
//    List<String> device_list = Arrays.asList("318f4fea56e7070c", "046827517ac92b09");
    public static Map<String, String> device_map;
    static {
        device_map = new HashMap<>();
        device_map.put("318f4fea56e7070c (pixel 4a)","318f4fea56e7070c");
        device_map.put("046827517ac92b09 (sm a52s)","046827517ac92b09");
    }
    @SuppressLint({"HardwareIds", "SetTextI18n"})
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("監控");
        setContentView(R.layout.activity_listen);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this,
                        R.array.device_array,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
//        spinner.setSelection(2, false);
//        預設選項設為第3項Earth(index為2)
        spinner.setOnItemSelectedListener(spnOnItemSelected);
        tvhello= findViewById(R.id.hello);
        pushNews= findViewById(R.id.pushNewsTextview);
        pushEsm= findViewById(R.id.pushEsmTextview);
        pushDiary= findViewById(R.id.pushDiaryTextview);
        tvhello.setText("選項:"+spinner.getSelectedItem().toString());





    }
    private final AdapterView.OnItemSelectedListener spnOnItemSelected = new AdapterView.OnItemSelectedListener() {
        @SuppressLint("SetTextI18n")
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            String sPos=String.valueOf(pos);
            String sInfo=parent.getItemAtPosition(pos).toString();
//            String sInfo=parent.getSelectedItem().toString();
            tvhello.setText("選項"+sPos+":"+sInfo);
            if(device_map.containsKey(sInfo)){
                crawlDateNewsNotification(device_map.get(sInfo));
                crawlDateEsm(device_map.get(sInfo));
                crawlDateDiary(device_map.get(sInfo));
            }

        }
        public void onNothingSelected(AdapterView<?> parent) {
            //
        }
    };
    private void crawlDateNewsNotification(String device_id) {
        tmpNews = "Notification\n";
//        tmp = tmp.concat("Notification\ndevice: " + device_map.get(device_id));
//        TextView rowTextView = new TextView(ListenActivity.this);
//        rowTextView.setTextColor(Color.parseColor("black"));
//        rowTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
//        rowTextView.setText(tmp);
//        ((LinearLayout) findViewById(R.id.layout_inside)).addView(rowTextView);
        db.collection("push_news")
                .whereEqualTo("device_id", device_id)
//                    .whereLessThan("noti_timestamp", 100000)
//                    .whereGreaterThan("noti_timestamp", 100000)
                .orderBy("receieve_timestamp", Query.Direction.DESCENDING)
                .limit(30)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list) {
                            if(d.getString("type").equals("target add")){
                                tmpNews+=d.getTimestamp("receieve_timestamp").toDate()+"\n";
                            }
                        }
                    } else {
                        tmpNews+="push_news no data\n";
                    }
                    pushNews.setText(tmpNews);
                }).addOnFailureListener(e -> Log.d("lognewsselect", String.valueOf(e)));
    }

    private void crawlDateEsm(String device_id) {
        tmpEsm = "Esm\n";
//        tmp = tmp.concat("Esm\n" + device_map.get(device_id));
//        TextView rowTextView = new TextView(ListenActivity.this);
//        rowTextView.setTextColor(Color.parseColor("black"));
//        rowTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
//        rowTextView.setText(tmp);
//        ((LinearLayout) findViewById(R.id.layout_inside)).addView(rowTextView);
        db.collection("push_esm")
                .whereEqualTo("device_id", device_id)
                .orderBy("receieve_timestamp", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list) {
                            tmpEsm+=d.getTimestamp("receieve_timestamp").toDate()+"\n";
                        }
                    } else {
                        tmpEsm+="no data\n";
                    }
                    pushEsm.setText(tmpEsm);
                }).addOnFailureListener(e -> Log.d("lognewsselect", String.valueOf(e)));
    }

    private void crawlDateDiary(String device_id) {
        tmpDiary = "Diary\n";
//        tmp = tmp.concat("Diary\ndevice: " + device_map.get(device_id));
//        TextView rowTextView = new TextView(ListenActivity.this);
//        rowTextView.setTextColor(Color.parseColor("black"));
//        rowTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
//        rowTextView.setText(tmp);
//        ((LinearLayout) findViewById(R.id.layout_inside)).addView(rowTextView);
        db.collection("push_diary")
                .whereEqualTo("device_id", device_id)
                .orderBy("noti_timestamp", Query.Direction.DESCENDING)
                .limit(5)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list) {
                            tmpDiary+=d.getTimestamp("receieve_timestamp").toDate()+"\n";
                        }
                    } else {
                        tmpDiary+="push_diary no data\n";
                    }
                    pushDiary.setText(tmpDiary);
                }).addOnFailureListener(e -> Log.d("lognewsselect", String.valueOf(e)));
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


