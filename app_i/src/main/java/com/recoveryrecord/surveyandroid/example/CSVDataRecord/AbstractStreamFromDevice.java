package com.recoveryrecord.surveyandroid.example.CSVDataRecord;

import androidx.core.app.NotificationCompat;

public class AbstractStreamFromDevice<T extends DataRecord> extends AbstractStream<T> {
    public AbstractStreamFromDevice(int maxSize) {
        super(maxSize);
    }

}
