/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.recoveryrecord.surveyandroid.example;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

/**
 * TestActivityRecognitionLogView outputs log data to the screen.
 */
public class TestActivityRecognitionLogFragment extends Fragment {

    private TestActivityRecognitionLogView mTestActivityRecognitionLogView;
    private ScrollView mScrollView;

    public TestActivityRecognitionLogFragment() {}

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View inflateViews() {
        mScrollView = new ScrollView(getActivity());
        ViewGroup.LayoutParams scrollParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mScrollView.setLayoutParams(scrollParams);

        mTestActivityRecognitionLogView = new TestActivityRecognitionLogView(getActivity());
        ViewGroup.LayoutParams logParams = new ViewGroup.LayoutParams(scrollParams);
        logParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mTestActivityRecognitionLogView.setLayoutParams(logParams);
        mTestActivityRecognitionLogView.setClickable(true);
        mTestActivityRecognitionLogView.setFocusable(true);
        mTestActivityRecognitionLogView.setTypeface(Typeface.MONOSPACE);

        // Want to set padding as 16 dips, setPadding takes pixels.  Hooray math!
        int paddingDips = 16;
        double scale = getResources().getDisplayMetrics().density;
        int paddingPixels = (int) ((paddingDips * (scale)) + .5);
        mTestActivityRecognitionLogView.setPadding(paddingPixels, paddingPixels, paddingPixels, paddingPixels);
        mTestActivityRecognitionLogView.setCompoundDrawablePadding(paddingPixels);

        mTestActivityRecognitionLogView.setGravity(Gravity.BOTTOM);

        mTestActivityRecognitionLogView.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Medium);

        mScrollView.addView(mTestActivityRecognitionLogView);
        return mScrollView;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View result = inflateViews();

        mTestActivityRecognitionLogView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        return result;
    }

    public TestActivityRecognitionLogView getLogView() {
        return mTestActivityRecognitionLogView;
    }
}