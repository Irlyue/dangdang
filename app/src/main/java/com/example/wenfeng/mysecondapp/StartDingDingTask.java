package com.example.wenfeng.mysecondapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import java.util.List;
import java.util.TimerTask;

public class StartDingDingTask extends TimerTask {
    public final static int WAIT_SECONDS = 30;
    public final static String DING_DING_PACKAGE_NAME = "com.alibaba.android.rimet";
    private Service mService;
    private CheckInUtility mCheckIn;


    private boolean mTodayChecked;

    public StartDingDingTask(Service service, List<ICheckInStrategy> strategies){
        mTodayChecked = false;
        mService = service;
        mCheckIn = new CheckInUtility(strategies);
    }

    @Override
    public void run() {
        if(isTodayChecked())
            return;
        if(mCheckIn.isTimeToCheckIn()) {
            wakeUpScreen();
            Log.i(CheckInService.LOG_TAG, "Starting ding ding...");
            Intent launchIntent = mService.getPackageManager().getLaunchIntentForPackage(DING_DING_PACKAGE_NAME);
            mService.startActivity(launchIntent);
            setTodayChecked(true);
            waitForSeconds(WAIT_SECONDS);
            goToHomeScreen();
            waitForSeconds(5);
            killDingDing();
        }
    }

    private void wakeUpScreen(){
        Log.i(CheckInService.LOG_TAG, "Waking up screen...");
        PowerManager pm = (PowerManager) mService.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
        wakeLock.acquire(50000);
    }

    private void waitForSeconds(int seconds){
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void goToHomeScreen(){
        Log.i(CheckInService.LOG_TAG, "Going to home...");
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mService.startActivity(startMain);
    }

    private void killDingDing(){
        Log.i(CheckInService.LOG_TAG, "Killing ding ding...");
        ActivityManager am = (ActivityManager) mService.getSystemService(Activity.ACTIVITY_SERVICE);
        am.killBackgroundProcesses(DING_DING_PACKAGE_NAME);
    }

    public boolean isTodayChecked() {
        return mTodayChecked;
    }

    public void setTodayChecked(boolean todayChecked) {
        this.mTodayChecked = todayChecked;
    }
}
