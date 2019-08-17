package com.example.delimes.flux;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * Created by User on 20.11.2017.
 */

public class BootReceiver extends BroadcastReceiver {
    Context mContext;
    private final String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";

    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context.getApplicationContext();
        String action = intent.getAction();
        /*
        if (action.equalsIgnoreCase(BOOT_ACTION)) {
            // в общем виде
            //для Activity
            Intent activivtyIntent = new Intent(context, MainActivity.class);
            activivtyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(activivtyIntent);
        }
        */
        Log.d("myLogs", "onReceive: " + action);
        if (action.equalsIgnoreCase(BOOT_ACTION)) {
            Log.d("myLogs", "onReceive:  context.startService(new Intent(context, UpdateReminders.class");
            mContext.startService(new Intent(context, UpdateReminders.class));

            /*
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_DATE_CHANGED);
            mContext.registerReceiver(new DateChangedReceiver(), filter);
            */
        }
    }
}