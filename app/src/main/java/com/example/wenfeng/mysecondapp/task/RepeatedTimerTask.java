package com.example.wenfeng.mysecondapp.task;

import android.util.Log;

import com.example.wenfeng.mysecondapp.CheckInService;
import com.example.wenfeng.mysecondapp.strategy.IResetStrategy;
import com.example.wenfeng.mysecondapp.utility.DateUtility;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public abstract class RepeatedTimerTask extends TimerTask{
    protected Timer mTimer;
    protected Date mDate;
    protected IResetStrategy mStrategy;
    private String mTaskName;

    public RepeatedTimerTask(Timer timer, IResetStrategy strategy, String taskName){
        mTimer = timer;
        mStrategy = strategy;
        mTaskName = taskName;
        mDate = DateUtility.setDayAndReturn(new Date(), mStrategy.calcDate());
    }

    @Override
    public void run() {
        executeTask();
        resetTimer();
        startNewTask();
    }

    public String getTaskName(){
        return this.mTaskName;
    }

    public Date getDate(){
        return this.mDate;
    }

    protected abstract void executeTask();

    protected void resetTimer(){
        Date tomorrow = DateUtility.advanceDate(new Date(), 1);
        mDate = DateUtility.setDayAndReturn(tomorrow, mStrategy.calcDate());
    }

    protected void startNewTask(){
        Log.i(CheckInService.LOG_TAG, String.format("Scheduling new task %s on %s", getTaskName(), getDate()));
        mTimer.schedule(new NewTask(), mDate);
    }

    private class NewTask extends TimerTask{

        @Override
        public void run() {
            RepeatedTimerTask.this.run();
        }
    }
}
