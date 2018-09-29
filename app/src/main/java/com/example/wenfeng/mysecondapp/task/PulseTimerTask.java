package com.example.wenfeng.mysecondapp.task;


import com.example.wenfeng.mysecondapp.CheckInService;
import com.example.wenfeng.mysecondapp.log.MyLogManager;
import com.example.wenfeng.mysecondapp.log.MyLogger;
import com.example.wenfeng.mysecondapp.strategy.IResetStrategy;
import com.example.wenfeng.mysecondapp.utility.DateUtility;

import java.util.Date;
import java.util.Timer;

public class PulseTimerTask extends RepeatedTimerTask {
    private final static MyLogger log = MyLogManager.getLogger();

    public PulseTimerTask(Timer timer, IResetStrategy strategy) {
        super(timer, strategy, PulseTimerTask.class.getSimpleName());
    }

    @Override
    protected void executeTask() {
        log.info(CheckInService.LOG_TAG, String.format("ExecuteTask() on %s", new Date()));
    }

    @Override
    protected void resetTimer() {
        mDate = mStrategy.calcDate();
    }
}
