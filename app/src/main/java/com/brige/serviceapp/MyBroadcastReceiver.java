package com.brige.serviceapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startIntent = new Intent(context, MyService.class);
        context.stopService(startIntent);

        /**
         * Here you can carry out any implementation you desire.
         * Networking, Service manipulation etc...
         */
    }
}
