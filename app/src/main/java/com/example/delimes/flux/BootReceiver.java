package com.example.delimes.flux;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
        mContext = context;
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
            Log.d("myLogs", "onReceive:  context.startService(new Intent(context, UpdateReminders.cla");
            context.startService(new Intent(context, UpdateReminders.class));
        }
    }
}