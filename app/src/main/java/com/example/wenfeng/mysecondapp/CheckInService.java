package com.example.wenfeng.mysecondapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CheckInService extends Service {

    public final static String LOG_TAG = "CheckInService";
    public final static String E_CHECK_IN_TIMER = "check in time";
    public final static int TIMER_DELAY_SECONDS = 10;
    public final static String E_RESET_TIME = "reset time";
    public final static String SP = "shared preferences";

    private List<TimerTask> mTimerTasks;
    private Timer mTimer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public CheckInService(){
        mTimerTasks = new ArrayList<>();
        mTimer = new Timer();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getDataFromIntent(intent);
        super.onStartCommand(intent, flags, startId);
        startTimers();
        return START_STICKY;
    }

    private void getDataFromIntent(Intent intent){
        List<ICheckInStrategy> strategies = new ArrayList<>();
        Date resetTime, checkInTime;
        if(intent.getStringExtra(E_CHECK_IN_TIMER) != null) {
            saveCurrentData(intent);
            checkInTime = DateUtility.strToDate(intent.getStringExtra(E_CHECK_IN_TIMER));
            resetTime = DateUtility.strToDate(intent.getStringExtra(E_RESET_TIME));
        }else{
            SharedPreferences sharedPreferences = getSharedPreferences(SP, Context.MODE_PRIVATE);
            checkInTime = DateUtility.strToDate(sharedPreferences.getString(E_CHECK_IN_TIMER, "08:00:00"));
            resetTime = DateUtility.strToDate(sharedPreferences.getString(E_RESET_TIME, "23:30:00"));
        }
        strategies.add(new DefaultStrategy(checkInTime));
        StartDingDingTask startDingDingTask = new StartDingDingTask(this, strategies);
        mTimerTasks.add(startDingDingTask);
        mTimerTasks.add(new ResetDingDingTask(startDingDingTask, resetTime));
    }

    private void saveCurrentData(Intent intent){
        SharedPreferences sharedPreferences= getSharedPreferences(SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(E_CHECK_IN_TIMER, intent.getStringExtra(E_CHECK_IN_TIMER));
        editor.putString(E_RESET_TIME, intent.getStringExtra(E_RESET_TIME));
        editor.apply();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy()");
        Intent broadcastIntent = new Intent("RestartTimer");
        sendBroadcast(broadcastIntent);
        stopTimers();
    }

    private void startTimers(){
        for(TimerTask task: mTimerTasks)
            mTimer.schedule(task, TIMER_DELAY_SECONDS * 1000, TIMER_DELAY_SECONDS * 1000);
    }

    private void stopTimers(){
        mTimer.cancel();
        mTimer = null;
    }
}
