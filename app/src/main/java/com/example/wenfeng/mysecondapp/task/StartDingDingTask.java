package com.example.wenfeng.mysecondapp.task;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.example.wenfeng.mysecondapp.CheckInService;
import com.example.wenfeng.mysecondapp.log.MyLogManager;
import com.example.wenfeng.mysecondapp.log.MyLogger;
import com.example.wenfeng.mysecondapp.strategy.IResetStrategy;
import com.example.wenfeng.mysecondapp.utility.AndroidUtility;

import java.util.Timer;

public class StartDingDingTask extends RepeatedTimerTask {
    private final static MyLogger log = MyLogManager.getLogger();
    public final static int WAIT_SECONDS = 30;
    public final static String DING_DING_PACKAGE_NAME = "com.alibaba.android.rimet";
    private Service mService;


    public StartDingDingTask(Timer timer, IResetStrategy strategy, Service service){
        super(timer, strategy, StartDingDingTask.class.getSimpleName());
        mService = service;
    }

    @Override
    protected void executeTask() {
        log.info(CheckInService.LOG_TAG, String.format("ExecuteTask() on %s", getDate()));
        wakeUpScreen();
        startDingDing();
        AndroidUtility.waitForSeconds(WAIT_SECONDS);
        goToHomeScreen();
        AndroidUtility.waitForSeconds(5);
//        killDingDing();
    }

    private void startDingDing(){
        log.info(CheckInService.LOG_TAG, "Starting ding ding...");
        AndroidUtility.startApplication(mService, DING_DING_PACKAGE_NAME);
    }

    private void wakeUpScreen(){
        log.info(CheckInService.LOG_TAG, "Waking up screen...");
        PowerManager pm = (PowerManager) mService.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
        wakeLock.acquire(50000);
    }

    private void goToHomeScreen(){
        log.info(CheckInService.LOG_TAG, "Going to home...");
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mService.startActivity(startMain);
    }

    private void killDingDing(){
        log.info(CheckInService.LOG_TAG, "Killing ding ding...");
        ActivityManager am = (ActivityManager) mService.getSystemService(Activity.ACTIVITY_SERVICE);
        am.killBackgroundProcesses(DING_DING_PACKAGE_NAME);
    }
}
