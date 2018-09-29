package com.example.wenfeng.mysecondapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.wenfeng.mysecondapp.strategy.FixedDateStrategy;
import com.example.wenfeng.mysecondapp.strategy.FixedPeriodStrategy;
import com.example.wenfeng.mysecondapp.strategy.RandomDateStrategy;
import com.example.wenfeng.mysecondapp.task.PulseTimerTask;
import com.example.wenfeng.mysecondapp.task.RepeatedTimerTask;
import com.example.wenfeng.mysecondapp.utility.DateUtility;
import com.example.wenfeng.mysecondapp.task.StartDingDingTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CheckInService extends Service {

    public final static String LOG_TAG = CheckInService.class.getSimpleName();
    public final static String E_RANGE_LEFT = "2";
    public final static String E_RANGE_RIGHT = "3";
    public final static String SP = "shared preferences";

    private List<RepeatedTimerTask> mTimerTasks;
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
        Date left, right;
        if(intent.getStringExtra(E_RANGE_LEFT) != null) {
            saveCurrentData(intent);
            left = DateUtility.strToDate(intent.getStringExtra(E_RANGE_LEFT));
            right = DateUtility.strToDate(intent.getStringExtra(E_RANGE_RIGHT));
            Log.i(LOG_TAG, "Data available, saved to shared preference.");
        }else{
            SharedPreferences sharedPreferences = getSharedPreferences(SP, Context.MODE_PRIVATE);
            left = DateUtility.strToDate(sharedPreferences.getString(E_RANGE_LEFT, "08:00:00"));
            right = DateUtility.strToDate(sharedPreferences.getString(E_RANGE_RIGHT, "08:20:00"));
            Log.i(LOG_TAG, "Data unavailable, restored from shared preference.");
        }
        Log.i(LOG_TAG, String.format("Left: %s, right: %s", left, right));
        StartDingDingTask startDingDingTask = new StartDingDingTask(mTimer, new RandomDateStrategy(left, right), this);
        mTimerTasks.add(startDingDingTask);
        mTimerTasks.add(new PulseTimerTask(mTimer, new FixedPeriodStrategy(600)));
    }

    private void saveCurrentData(Intent intent){
        SharedPreferences sharedPreferences= getSharedPreferences(SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(E_RANGE_LEFT, intent.getStringExtra(E_RANGE_LEFT));
        editor.putString(E_RANGE_RIGHT, intent.getStringExtra(E_RANGE_RIGHT));
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
        for(RepeatedTimerTask task: mTimerTasks) {
            Log.i(LOG_TAG, String.format("Scheduling %s on %s", task.getTaskName(), task.getDate()));
            mTimer.schedule(task, task.getDate());
        }
    }

    private void stopTimers(){
        Log.i(LOG_TAG, "Stopping timers...");
        mTimer.cancel();
        mTimer = null;
    }
}
