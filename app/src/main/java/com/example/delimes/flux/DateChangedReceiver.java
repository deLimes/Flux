package com.example.delimes.flux;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class DateChangedReceiver extends BroadcastReceiver {

    public MainActivity mainActivity;
    public Context mContext;

    public DateChangedReceiver(Context context) {
        this.mainActivity = (MainActivity)context;
    }

    public DateChangedReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_DATE_CHANGED)) {
            Log.d("123", "onReceive: DateChangedReceiver");
            mContext = context.getApplicationContext();

            MainActivity.calendar.clear();
            MainActivity.calendar.setTimeInMillis(System.currentTimeMillis());
            int year = MainActivity.calendar.get(Calendar.YEAR);
            int month = MainActivity.calendar.get(Calendar.MONTH);
            int day = MainActivity.calendar.get(Calendar.DAY_OF_MONTH);
            MainActivity.calendar.clear();
            MainActivity.calendar.set(year, month, day);
            MainActivity.currDate = new Date(MainActivity.calendar.getTimeInMillis());

            if (MainActivity.numberYearPicker != null) {
                MainActivity.numberYearPicker.setValue(year);
            }

            mContext.stopService(new Intent(mContext, UpdateReminders.class));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mContext.startForegroundService(new Intent(mContext, UpdateReminders.class));
            }else {
                mContext.startService(new Intent(mContext, UpdateReminders.class));
            }
            //Log.d("123", "onReceive: DateChangedReceiver");
        }
    }

}