package com.delimes.flux;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by User on 19.11.2017.
 */

public class Receiver extends BroadcastReceiver {

    public Receiver() {
    }
    final String LOG_TAG = "myLogs";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(LOG_TAG, "onReceive: Receiver");
        Log.d(LOG_TAG, "action = " + intent.getAction());
        Log.d(LOG_TAG, "extra = " + intent.getStringExtra("extra"));

        MainActivity.sendNotif(context, intent);

    }

}
