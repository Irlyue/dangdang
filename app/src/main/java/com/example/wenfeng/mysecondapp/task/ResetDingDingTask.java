package com.example.wenfeng.mysecondapp.task;

import android.util.Log;

import com.example.wenfeng.mysecondapp.CheckInService;
import com.example.wenfeng.mysecondapp.utility.DateUtility;

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
            Log.i(CheckInService.LOG_TAG, "Reset task");
            mStartDingDingTask.setTodayChecked(false);
        }
    }

    private boolean isTimeToReset(){
        Date now = new Date();
        Date resetDate = DateUtility.setDayAndReturn(now, mResetDate);
        return now.after(resetDate);
    }
}
