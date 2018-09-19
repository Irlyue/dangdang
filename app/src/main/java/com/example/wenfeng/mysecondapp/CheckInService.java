package com.example.wenfeng.mysecondapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.wenfeng.mysecondapp.strategy.DefaultStrategy;
import com.example.wenfeng.mysecondapp.strategy.ICheckInStrategy;
import com.example.wenfeng.mysecondapp.utility.DateUtility;
import com.example.wenfeng.mysecondapp.task.ResetDingDingTask;
import com.example.wenfeng.mysecondapp.task.StartDingDingTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CheckInService extends Service {

    public final static String LOG_TAG = CheckInService.class.getSimpleName();
    public final static String E_CHECK_IN_TIME = "0";
    public final static String E_RESET_TIME = "1";
    public final static String SP = "shared preferences";
    public final static int TIMER_DELAY_SECONDS = 10;

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
        Log.i(LOG_TAG, "onStartCommand()");
        super.onStartCommand(intent, flags, startId);
        getDataFromIntent(intent);
        startTimers();
        Log.i(LOG_TAG, "onStartCommand() done");
        return START_STICKY;
    }

    private void getDataFromIntent(Intent intent){
        Log.i(LOG_TAG, "Getting data from intent...");
        List<ICheckInStrategy> strategies = new ArrayList<>();
        Date resetTime, checkInTime;
        if(intent.getStringExtra(E_CHECK_IN_TIME) != null) {
            saveCurrentData(intent);
            checkInTime = DateUtility.strToDate(intent.getStringExtra(E_CHECK_IN_TIME));
            resetTime = DateUtility.strToDate(intent.getStringExtra(E_RESET_TIME));
            Log.i(LOG_TAG, "Data available, saved to shared preference.");
        }else{
            SharedPreferences sharedPreferences = getSharedPreferences(SP, Context.MODE_PRIVATE);
            checkInTime = DateUtility.strToDate(sharedPreferences.getString(E_CHECK_IN_TIME, "08:00:00"));
            resetTime = DateUtility.strToDate(sharedPreferences.getString(E_RESET_TIME, "23:30:00"));
            Log.i(LOG_TAG, "Data unavailable, restored from shared preference.");
        }
        Log.i(LOG_TAG, String.format("Check in time(%s), reset time(%s)", checkInTime, resetTime));
        strategies.add(new DefaultStrategy(checkInTime));
        StartDingDingTask startDingDingTask = new StartDingDingTask(this, strategies);
        mTimerTasks.add(startDingDingTask);
        mTimerTasks.add(new ResetDingDingTask(startDingDingTask, resetTime));
    }

    private void saveCurrentData(Intent intent){
        SharedPreferences sharedPreferences= getSharedPreferences(SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(E_CHECK_IN_TIME, intent.getStringExtra(E_CHECK_IN_TIME));
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
        Log.i(LOG_TAG, "Starting timers...");
        for(TimerTask task: mTimerTasks)
            mTimer.schedule(task, TIMER_DELAY_SECONDS * 1000, TIMER_DELAY_SECONDS * 1000);
    }

    private void stopTimers(){
        Log.i(LOG_TAG, "Stopping timers...");
        mTimer.cancel();
        mTimer = null;
    }
}
