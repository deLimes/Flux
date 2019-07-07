package com.example.delimes.flux;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class TimeChangedReceiver extends BroadcastReceiver {

    public MainActivity mainActivity;


    public TimeChangedReceiver() {
    }

    public TimeChangedReceiver(Context context) {
        this.mainActivity = (MainActivity)context;

    }


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("123", "onReceive Tick");
        if (intent.getAction().equals(Intent.ACTION_TIME_TICK) && mainActivity.numberYearPicker != null) {
            mainActivity.onTimeChanged();
        }

    }
}
