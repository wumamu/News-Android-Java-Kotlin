package com.recoveryrecord.surveyandroid.example;

import com.recoveryrecord.surveyandroid.example.model.MediaModel;

import java.util.ArrayList;

public interface OnCustomerListChangedListener {
    void onNoteListChanged(ArrayList<MediaModel> mediaModels);
}