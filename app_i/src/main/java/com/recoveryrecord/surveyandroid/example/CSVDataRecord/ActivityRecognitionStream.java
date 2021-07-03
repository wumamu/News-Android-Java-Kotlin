package com.recoveryrecord.surveyandroid.example.CSVDataRecord;

import java.util.ArrayList;
import java.util.List;

public class ActivityRecognitionStream extends AbstractStreamFromDevice<ActivityRecognitionDataRecord> {
    public ActivityRecognitionStream(int maxSize) {
            super(maxSize);
            }

    @Override
    public List<Class<? extends DataRecord>> dependsOnDataRecordType() {
            return new ArrayList<>();
            }
}
