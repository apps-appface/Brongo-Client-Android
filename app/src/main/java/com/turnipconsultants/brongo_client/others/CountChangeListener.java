package com.turnipconsultants.brongo_client.others;

import android.util.Log;

import com.turnipconsultants.brongo_client.activities.BrokersMapActivity;

/**
 * Created by mohit on 04-10-2017.
 */

public class CountChangeListener {
    private int count = 0;
    private ChangeListener listener;

    public CountChangeListener() {
        listener= (ChangeListener) new BrokersMapActivity();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
        Log.e("listener", listener + "");
        if (listener != null) listener.onChange();
    }

    public ChangeListener getListener() {
        return listener;
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    public interface ChangeListener {
        void onChange();
    }
}
