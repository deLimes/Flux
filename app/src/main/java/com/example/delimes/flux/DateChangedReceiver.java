package com.example.delimes.flux;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Date;

public class DateChangedReceiver extends BroadcastReceiver {

    public MainActivity mainActivity;

    public DateChangedReceiver(Context context) {
        this.mainActivity = (MainActivity)context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        MainActivity.calendar.clear();
        MainActivity.calendar.setTimeInMillis(System.currentTimeMillis());
        int year = MainActivity.calendar.get(Calendar.YEAR);
        int month = MainActivity.calendar.get(Calendar.MONTH);
        int day = MainActivity.calendar.get(Calendar.DAY_OF_MONTH);
        MainActivity.calendar.clear();
        MainActivity.calendar.set(year, month, day);
        MainActivity.currDate = new Date(MainActivity.calendar.getTimeInMillis());

        mainActivity.winter.invalidate();
        mainActivity.spring.invalidate();
        mainActivity.summer.invalidate();
        mainActivity.autumn.invalidate();
    }

}