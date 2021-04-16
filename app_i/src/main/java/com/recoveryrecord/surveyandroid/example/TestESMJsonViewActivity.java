package com.recoveryrecord.surveyandroid.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.recoveryrecord.surveyandroid.example.sqlite.ESM;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
//import android.support.v4.app.NotificationCompat ;
//import android.support.v7.app.AppCompatActivity ;


public class TestESMJsonViewActivity extends AppCompatActivity {
//    private TextView txtView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esm_json_view);

        TextView output = (TextView) findViewById(R.id.ESMTextView);
        Button btn_to_esm_db = (Button) findViewById(R.id.btn_to_esm_db);
        Button btn_insert_esm_db = (Button) findViewById(R.id.btn_insert_esm_db);
//        String strJson="{ \"Employee\" :[{\"id\":\"101\",\"name\":\"Sonoo Jaiswal\",\"salary\":\"50000\"},{\"id\":\"102\",\"name\":\"Vimal Jaiswal\",\"salary\":\"60000\"}] }";
//        String strJsons="{\"answers\":{\"share_1\":[\"私人訊息給單一好友\",\"私人訊息給多人群組\",\"實體聊天時口述\"],\"share_2\":\"此則新聞含有錯誤資訊，需澄清\",\"base_2\":\"有\",\"base_1\":\"有\",\"read_6\":\"3\",\"read_15\":\"執行中\",\"read_5\":\"3\",\"read_14\":\"工作或學習\",\"read_8\":\"3\",\"read_17\":\"有\",\"read_7\":\"3\",\"read_16\":\"3\",\"read_2\":\"有\",\"read_11\":\"非預期的閱讀活動(ex:我未規劃此時為閱讀時刻)\",\"read_1\":\"了解\",\"read_10\":\"3\",\"read_4\":\"3\",\"read_13\":\"內容吸引力程度\",\"read_3\":\"3\",\"read_12\":\"掃視閱讀(scanning)\",\"read_9\":\"3\"}}";
        String strJsons="{\"answers\":{\"not_read_5\":\"3\",\"base_2\":\"沒有\",\"base_1\":\"有\",\"read_6\":\"3\",\"read_15\":\"執行中\",\"read_5\":\"3\",\"read_14\":\"工作或學習\",\"read_8\":\"3\",\"read_17\":\"有\",\"read_7\":\"3\",\"read_16\":\"3\",\"read_2\":\"有\",\"read_11\":\"非預期的閱讀活動(ex:我未規劃此時為閱讀時刻)\",\"not_read_1\":\"閱讀標題\",\"read_1\":\"了解\",\"read_10\":\"3\",\"not_read_2\":[\"沒精神閱讀\",\"正在做其他事情\"],\"read_4\":\"3\",\"read_13\":\"內容吸引力程度\",\"not_read_3\":\"沒精神閱讀\",\"read_3\":\"3\",\"read_12\":\"掃視閱讀(scanning)\",\"not_read_4\":\"執行中\",\"read_9\":\"3\",\"share_1\":[\"私人訊息給單一好友\",\"私人訊息給多人群組\",\"實體聊天時口述\"],\"share_2\":\"此則新聞含有錯誤資訊，需澄清\"}}";
        final ESM ESM_answer = new ESM();
        String data = "";
        try {
            // Create the root JSONObject from the JSON string.
//            JSONObject jsonRootObject = new JSONObject(strJson);
            //Get the instance of JSONArray that contains JSONObjects
//            JSONArray jsonArray = jsonRootObject.optJSONArray("Employee");
            JSONObject jsonRootObject = new JSONObject(strJsons);
            JSONObject jsonAnswerObject = jsonRootObject.getJSONObject("answers");
            String base_1 = jsonAnswerObject.optString("base_1");
            String base_2 = jsonAnswerObject.optString("base_2");
            String not_read_1 = jsonAnswerObject.optString("not_read_1");
            String not_read_2 = jsonAnswerObject.optString("not_read_2");//multi_select
            String not_read_3 = jsonAnswerObject.optString("not_read_3");
            String not_read_4 = jsonAnswerObject.optString("not_read_4");
            String not_read_5 = jsonAnswerObject.optString("not_read_5");
            String read_1 = jsonAnswerObject.optString("read_1");
            String read_2 = jsonAnswerObject.optString("read_2");
            String read_3 = jsonAnswerObject.optString("read_3");
            String read_4 = jsonAnswerObject.optString("read_4");
            String read_5 = jsonAnswerObject.optString("read_5");
            String read_6 = jsonAnswerObject.optString("read_6");
            String read_7 = jsonAnswerObject.optString("read_7");
            String read_8 = jsonAnswerObject.optString("read_8");
            String read_9 = jsonAnswerObject.optString("read_9");
            String read_10 = jsonAnswerObject.optString("read_10");
            String read_11 = jsonAnswerObject.optString("read_11");
            String read_12 = jsonAnswerObject.optString("read_12");
            String read_13 = jsonAnswerObject.optString("read_13");
            String read_14 = jsonAnswerObject.optString("read_14");
            String read_15 = jsonAnswerObject.optString("read_15");
            String read_16 = jsonAnswerObject.optString("read_16");
            String read_17 = jsonAnswerObject.optString("read_17");
            String not_share_1 = jsonAnswerObject.optString("not_share_1");
            String share_1 = jsonAnswerObject.optString("share_1");//multi_select
            String share_2 = jsonAnswerObject.optString("share_2");
            ESM_answer.setKEY_BASE_1(base_1);
            ESM_answer.setKEY_BASE_2(base_2);
            ESM_answer.setKEY_NOT_READ_1(not_read_1);
            ESM_answer.setKEY_NOT_READ_2(not_read_2);
            ESM_answer.setKEY_NOT_READ_3(not_read_3);
            ESM_answer.setKEY_NOT_READ_4(not_read_4);
            ESM_answer.setKEY_NOT_READ_5(not_read_5);
            ESM_answer.setKEY_READ_1(read_1);
            ESM_answer.setKEY_READ_2(read_2);
            ESM_answer.setKEY_READ_3(read_3);
            ESM_answer.setKEY_READ_4(read_4);
            ESM_answer.setKEY_READ_5(read_5);
            ESM_answer.setKEY_READ_6(read_6);
            ESM_answer.setKEY_READ_7(read_7);
            ESM_answer.setKEY_READ_8(read_8);
            ESM_answer.setKEY_READ_9(read_9);
            ESM_answer.setKEY_READ_10(read_10);
            ESM_answer.setKEY_READ_11(read_11);
            ESM_answer.setKEY_READ_12(read_12);
            ESM_answer.setKEY_READ_13(read_13);
            ESM_answer.setKEY_READ_14(read_14);
            ESM_answer.setKEY_READ_15(read_15);
            ESM_answer.setKEY_READ_16(read_16);
            ESM_answer.setKEY_READ_17(read_17);
            ESM_answer.setKEY_NOT_SHARE_1(not_share_1);
            ESM_answer.setKEY_SHARE_1(share_1);
            ESM_answer.setKEY_SHARE_2(share_2);

            JSONArray jsonAnswerShareOneArray = jsonAnswerObject.optJSONArray("share_1");
            JSONArray jsonAnswerNotReadSecondArray = jsonAnswerObject.optJSONArray("not_read_2");

//            if(base_1==""){
//                data += "base_1:null\n";
//            } else {
//                data += "base_1: " + base_1+"\n";
//            }
//            if(base_2==""){
//                data += "base_2:null\n";
//            } else {
//                data += "base_2: " + base_2+"\n";
//            }
            data += "base_1: " + ESM_answer.getKEY_BASE_1()+"\n";
            data += "base_2: " + base_2+"\n";
            data += "not_read_1: " + not_read_1+"\n";
//            data += "not_read_2: " + not_read_2+"\n";
            if (jsonAnswerNotReadSecondArray!=null){
                data += "not_read_2: ";
                for(int index_not_read=0; index_not_read < jsonAnswerNotReadSecondArray.length(); index_not_read++){
                    String tmp = jsonAnswerNotReadSecondArray.optString(index_not_read);
                    data += index_not_read+1 + ". " + tmp +" ";
                }
                data += "\n";
            } else {
                data += "not_read_2:\n";
            }
            data += "not_read_3: " + not_read_3+"\n";
            data += "not_read_4: " + not_read_4+"\n";
            data += "not_read_5: " + not_read_5+"\n";
            data += "read_1: " + read_1+"\n";
            data += "read_2: " + read_2+"\n";
            data += "read_3: " + read_3+"\n";
            data += "read_4: " + read_4+"\n";
            data += "read_5: " + read_5+"\n";
            data += "read_6: " + read_6+"\n";
            data += "read_7: " + read_7+"\n";
            data += "read_8: " + read_8 +"\n";
            data += "read_9: " + read_9 +"\n";
            data += "read_10: " + read_10+"\n";
            data += "read_11: " + read_11+"\n";
            data += "read_12: " + read_12+"\n";
            data += "read_13: " + read_13+"\n";
            data += "read_14: " + read_14+"\n";
            data += "read_15: " + read_15+"\n";
            data += "read_16: " + read_16+"\n";
            data += "read_17: " + read_17+"\n";
            data += "not_share_1: " + not_share_1+"\n";
//            data += "share_1: " + share_1+"\n";
            if(jsonAnswerShareOneArray!=null){
                data += "share_1: ";
                for(int index_share=0; index_share < jsonAnswerShareOneArray.length(); index_share++){
                    String tmp = jsonAnswerShareOneArray.optString(index_share);
                    data += index_share+1 + ". " + tmp +" ";
                }
                data += "\n";
            } else {
                data += "share_1:\n";
            }
            data += "share_2: " + share_2+"\n";

            output.setText(data);
        } catch (JSONException e) {e.printStackTrace();}

        btn_to_esm_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TestESMJsonViewActivity.this, TestESMDbViewActivity.class);
                startActivity(intent);
            }
        });
        btn_insert_esm_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(TestESMJsonViewActivity.this, TestESMDbViewActivity.class);
//                startActivity(intent);
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
                String time_now = formatter.format(date);
                TestESMDbHelper dbHandler = new TestESMDbHelper(TestESMJsonViewActivity.this);
                dbHandler.insertESMDetails(ESM_answer, time_now);
                Toast.makeText(getApplicationContext(), "Details Inserted Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TestESMJsonViewActivity.this, TestBasicActivity.class);
        startActivity(intent);
    }

}
