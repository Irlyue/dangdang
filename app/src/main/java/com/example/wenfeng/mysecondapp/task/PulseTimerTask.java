package com.example.wenfeng.mysecondapp.task;

import android.util.Log;

import com.example.wenfeng.mysecondapp.CheckInService;
import com.example.wenfeng.mysecondapp.strategy.IResetStrategy;
import com.example.wenfeng.mysecondapp.utility.DateUtility;

import java.util.Date;
import java.util.Timer;

public class PulseTimerTask extends RepeatedTimerTask {
    public PulseTimerTask(Timer timer, IResetStrategy strategy) {
        super(timer, strategy, PulseTimerTask.class.getSimpleName());
    }

    @Override
    protected void executeTask() {
        Log.i(CheckInService.LOG_TAG, String.format("ExecuteTask() on %s", new Date()));
    }

    @Override
    protected void resetTimer() {
        mDate = mStrategy.calcDate();
    }
}
