package com.recoveryrecord.surveyandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class DefaultSubmitSurveyHandler implements SubmitSurveyHandler {

    private Context mContext;
    private Response.Listener<JSONObject> mResponseListener;
    private Response.ErrorListener mErrorListener;

    public DefaultSubmitSurveyHandler(Context context) {
        mContext = context;
        mResponseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getContext(), "Response: " + response.toString(), Toast.LENGTH_LONG).show();
            }
        };
        mErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "ERROR: " + error, Toast.LENGTH_LONG).show();
            }
        };
    }

    private Context getContext() {
        return mContext;
    }

    public void setResponseListener(Response.Listener<JSONObject> responseListener) {
        mResponseListener = responseListener;
    }

    public void setErrorListener(Response.ErrorListener errorListener) {
        mErrorListener = errorListener;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void submit(String url, String jsonQuestionAnswerData) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        Log.i("log: ESM", jsonQuestionAnswerData);
        addESM(jsonQuestionAnswerData);

//        JSONObject requestBody = null;
//        try {
//            requestBody = new JSONObject(jsonQuestionAnswerData);
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return;
//        }
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody, mResponseListener, mErrorListener);
//        queue.add(jsonObjectRequest);

    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addESM(String jsonQuestionAnswerData) {
//        Date date = new Date(System.currentTimeMillis());
//        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//        String time_now = formatter.format(date);
//        Map<String, Object> esm = new HashMap<>();
//        esm.put("submit_time", time_now);
//        esm.put("result",  jsonQuestionAnswerData);
//        LocalDate l_date = LocalDate.now();
//        String device_id = Settings.Secure.getString(mContext.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//        db.collection("test_users")
//                .document(device_id)
//                .collection("esms")
//                .document(time_now)
//                .set(esm);

    }
}
