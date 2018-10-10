package com.example.wenfeng.mysecondapp;

import android.app.Service;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.wenfeng.mysecondapp.log.MyLogManager;
import com.example.wenfeng.mysecondapp.log.MyLogger;
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

    private final static MyLogger log = MyLogManager.getLogger();

    public final static String LOG_TAG = CheckInService.class.getSimpleName();
    public final static String E_RANGE_LEFT = "2";
    public final static String E_RANGE_RIGHT = "3";
    public final static String SP = "shared preferences";

    private TimerTask mStartDingDing;
    private Timer mTimer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public CheckInService(){
        mTimer = new Timer();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        log.info(LOG_TAG, "onStartCommand()");
        super.onStartCommand(intent, flags, startId);
        getDataFromIntent(intent);
        startTimers();
        log.info(LOG_TAG, "onStartCommand() done");
        return START_STICKY;
    }

    private void getDataFromIntent(Intent intent){
        log.info(LOG_TAG, "Getting data from intent...");
        Date left, right;
        if(intent.getStringExtra(E_RANGE_LEFT) != null) {
            saveCurrentData(intent);
            left = DateUtility.strToDate(intent.getStringExtra(E_RANGE_LEFT));
            right = DateUtility.strToDate(intent.getStringExtra(E_RANGE_RIGHT));
            log.info(LOG_TAG, "Data available, saved to shared preference.");
        }else{
            SharedPreferences sharedPreferences = getSharedPreferences(SP, Context.MODE_PRIVATE);
            left = DateUtility.strToDate(sharedPreferences.getString(E_RANGE_LEFT, "08:00:00"));
            right = DateUtility.strToDate(sharedPreferences.getString(E_RANGE_RIGHT, "08:20:00"));
            log.info(LOG_TAG, "Data unavailable, restored from shared preference.");
        }
        log.info(LOG_TAG, String.format("Left: %s, right: %s", left, right));
        mStartDingDing = new StartDingDingTask(new RandomDateStrategy(left, right), this);
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
        log.info(LOG_TAG, String.format("%s onDestroy()", getClass().getSimpleName()));
        super.onDestroy();
        Intent broadcastIntent = new Intent("RestartTimer");
        sendBroadcast(broadcastIntent);
        stopTimers();
    }

    private void startTimers(){
        log.info(LOG_TAG, "Starting timer...");
        mTimer.schedule(mStartDingDing, 1000, 1000);
    }

    private void stopTimers(){
        log.info(LOG_TAG, "Stopping timers...");
    }
}
