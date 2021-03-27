package com.recoveryrecord.surveyandroid.viewholder;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.recoveryrecord.surveyandroid.AnswerProvider;
import com.recoveryrecord.surveyandroid.R;
import com.recoveryrecord.surveyandroid.SubmitSurveyHandler;
import com.recoveryrecord.surveyandroid.question.QuestionsWrapper.SubmitData;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SubmitViewHolder extends RecyclerView.ViewHolder {

    private Button submitButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public SubmitViewHolder(@NonNull View itemView) {
        super(itemView);
        submitButton = itemView.findViewById(R.id.submit_button);
    }

    public void bind(final SubmitData submitData, final AnswerProvider answerProvider, final SubmitSurveyHandler submitSurveyHandler) {
        submitButton.setText(submitData.buttonTitle);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                submitSurveyHandler.submit(submitData.url, answerProvider.allAnswersJson());
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
                String time_now = formatter.format(date);
                // [START add_ada_lovelace]
                // Create a new user with a first and last name
                Map<String, Object> esm = new HashMap<>();
                esm.put("submit_time", time_now);
                esm.put("result",  answerProvider.allAnswersJson());
                LocalDate l_date = LocalDate.now();
                String device_id = Settings.Secure.getString(((Activity)v.getContext()).getContentResolver(), Settings.Secure.ANDROID_ID);
                db.collection("test_users")
                        .document(device_id)
                        .collection("esms")
                        .document(time_now)
                        .set(esm);
                Toast.makeText((Activity)v.getContext(), "Post Inserted Successfully", Toast.LENGTH_SHORT).show();
                ((Activity)v.getContext()).finish();

            }
        });

    }
}
