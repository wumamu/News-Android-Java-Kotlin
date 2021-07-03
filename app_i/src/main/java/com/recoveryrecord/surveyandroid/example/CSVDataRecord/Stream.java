package com.recoveryrecord.surveyandroid.example.CSVDataRecord;

import java.util.List;
import java.util.Queue;

public interface Stream<T extends DataRecord> extends Queue<T> {
    /**
     * Defining stream types
     */
    public enum StreamType{
        FROM_DEVICE, FROM_USER, FROM_QUESTION
    }

    /**
     * Fetch the current value of the stream
     *
     * @return the value of the newest DataRecord (T) in the stream
     */
    public T getCurrentValue();

    /**
     * Fetch the previous value of the stream - the older current value
     *
     * @return the value of the DataRecord right after the newest DataRecord
     */
    public T getPreviousValue();

    /**
     * Fetch a list of DataRecord types that this stream
     * uses as inputs to create a new stream
     *
     * @return the list of DataRecord types
     */
    public List<Class<? extends DataRecord>> dependsOnDataRecordType();

    /**
     * Get the type of stream - from_device, from_user, from_question
     * @return the type of stream
     *         {@link labelingStudy.nctu.minukucore.stream.Stream.StreamType}
     */
    public StreamType getType();
}
