package com.recoveryrecord.surveyandroid.viewholder;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.recoveryrecord.surveyandroid.AnswerProvider;
import com.recoveryrecord.surveyandroid.R;
import com.recoveryrecord.surveyandroid.SubmitSurveyHandler;
import com.recoveryrecord.surveyandroid.question.QuestionsWrapper.SubmitData;

public class SubmitViewHolder extends RecyclerView.ViewHolder {

    private Button submitButton;

    public SubmitViewHolder(@NonNull View itemView) {
        super(itemView);
        submitButton = itemView.findViewById(R.id.submit_button);
    }

    public void bind(final SubmitData submitData, final AnswerProvider answerProvider, final SubmitSurveyHandler submitSurveyHandler) {
        submitButton.setText(submitData.buttonTitle);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitSurveyHandler.submit(submitData.url, answerProvider.allAnswersJson());
                ((Activity)v.getContext()).finish();
//                ((Activity)context).finish();
//                v.getContext().
//                Intent returnIntent = new Intent();
//                returnIntent.setClass(v.getContext(), com.recoveryrecord.surveyandroid.example.MainActivity.class);
//                v.getContext().startActivity(returnIntent);
            }
        });

    }
}
