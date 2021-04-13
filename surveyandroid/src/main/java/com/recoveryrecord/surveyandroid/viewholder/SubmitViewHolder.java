package com.recoveryrecord.surveyandroid.viewholder;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
import java.util.Objects;

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
                final String time_now = formatter.format(date);
                // [START add_ada_lovelace]
                final Timestamp current = Timestamp.now();
                // Create a new user with a first and last name
//                Map<String, Object> esm = new HashMap<>();
//                esm.put("submit_time", time_now);
//                esm.put("submit_timestamp", current);
//                esm.put("result",  answerProvider.allAnswersJson());
                String device_id = Settings.Secure.getString(((Activity)v.getContext()).getContentResolver(), Settings.Secure.ANDROID_ID);
                String esm_id = "";
                esm_id = String.valueOf(date);
                if (((Activity)v.getContext()).getIntent().getExtras() != null) {
                    Bundle b = ((Activity)v.getContext()).getIntent().getExtras();
                    esm_id = Objects.requireNonNull(b.getString("esm_id"));
                    Log.d("logesm", String.valueOf(456));
                }
                //new
//                db.collection("test_users")
//                        .document(device_id)
//                        .collection("esms")
//                        .document(esm_id)
//                        .set(esm);
                //update
                final DocumentReference rbRef = db.collection("test_users").document(device_id).collection("esms").document(esm_id);
                rbRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                rbRef.update("submit_time", time_now,
                                        "submit_timestamp", current,
                                        "result", answerProvider.allAnswersJson())//another field
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("log: firebase share", "DocumentSnapshot successfully updated!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("log: firebase share", "Error updating document", e);
                                            }
                                        });
                            } else {
                                Log.d("log: firebase share", "No such document");
                            }
                        } else {
                            Log.d("log: firebase share", "get failed with ", task.getException());
                        }
                    }
                });
                Toast.makeText((Activity)v.getContext(), "Post Inserted Successfully", Toast.LENGTH_SHORT).show();
                ((Activity)v.getContext()).finish();

            }
        });

    }
}
