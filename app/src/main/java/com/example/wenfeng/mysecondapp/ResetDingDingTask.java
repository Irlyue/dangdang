package com.example.wenfeng.mysecondapp;

import android.util.Log;

import java.util.Date;
import java.util.TimerTask;

public class ResetDingDingTask extends TimerTask {
    private StartDingDingTask mStartDingDingTask;
    private Date mResetDate;
    public ResetDingDingTask(StartDingDingTask startDingDingTask, Date resetDate){
        mStartDingDingTask = startDingDingTask;
        mResetDate = resetDate;
    }
    @Override
    public void run() {
        if(isTimeToReset()){
            Log.i(CheckInService.LOG_TAG, "Time to reset..");
            mStartDingDingTask.setTodayChecked(false);
        }else{
            Log.i(CheckInService.LOG_TAG, "Wait for tomorrow..");
        }
    }

    private boolean isTimeToReset(){
        Date now = new Date();
        Date resetDate = DateUtility.setDayAndReturn(now, mResetDate);
        Log.i(CheckInService.LOG_TAG, String.format("Reset date: %s, now: %s", resetDate, now));
        return now.after(resetDate);
    }
}
