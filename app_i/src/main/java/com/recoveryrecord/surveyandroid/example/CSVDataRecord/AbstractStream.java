package com.recoveryrecord.surveyandroid.example.CSVDataRecord;

import android.util.Log;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class AbstractStream<T extends DataRecord>
        extends LinkedList<T>
        implements Stream<T> {
    protected int maxSize;

    public AbstractStream(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(T object) {
        if (this.size() == maxSize) {
            this.removeFirst();
        }
        return super.add(object);
    }

    @Override
    public void add(int location, T object) {
        if (this.size() == maxSize) {
            this.removeFirst();
        }
        super.add(location, object);
    }

    @Override
    public boolean addAll(Collection<? extends T> objects) {
        final int neededSize = size() + objects.size();
        final int overflowSize = neededSize - maxSize;
        if (overflowSize > 0) {
            removeRange(0, overflowSize);
        }
        return super.addAll(objects);
    }

    @Override
    public boolean addAll(int location, Collection<? extends T> objects) {
        Log.d("AbstractStream", "Add all called on location :" + location);
        return super.addAll(location, objects);
    }

    @Override
    public void addFirst(T object) {
        Log.e("AbstractStream", "Cannot add to the starting of the queue in abstract stream.");
        throw new UnsupportedOperationException(
                "Cannot add to the starting of the queue in abstract stream.");
    }

    @Override
    public void addLast(T object) {
        this.add(object);
    }

    @Override
    public T getCurrentValue() {
        return (this.size() >= 2 ? this.getLast() : null);
    }

    @Override
    public T getPreviousValue() {
        return this.size() > 1 ? this.get(this.size() - 2): null;
    }

    @Override
    public List<Class<? extends DataRecord>> dependsOnDataRecordType() {
        return null;
    }

    @Override
    public StreamType getType() {
        return null;
    }
}
