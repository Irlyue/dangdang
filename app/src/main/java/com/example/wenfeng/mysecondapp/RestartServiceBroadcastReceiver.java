package com.example.wenfeng.mysecondapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RestartServiceBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Service", "Service Stops! Oooooooooooooppppssssss!!!!");
        context.startService(new Intent(context, CheckInService.class));
    }
}
