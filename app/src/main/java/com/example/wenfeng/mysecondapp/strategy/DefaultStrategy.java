package com.example.wenfeng.mysecondapp.strategy;

import android.util.Log;

import com.example.wenfeng.mysecondapp.utility.DateUtility;

import java.util.Calendar;
import java.util.Date;

public class DefaultStrategy implements ICheckInStrategy {
    private final static String DEFAULT_DAYS="1234567";
    private final static int RANGE = 2;
    private Date mLeft;
    private Date mRight;
    private String mDays;


    public DefaultStrategy(Date date){
        this(date, DEFAULT_DAYS);
    }

    private Date getLeft(Date date, int minutes){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    public DefaultStrategy(Date date, String days){
        mRight = date;
        mLeft = getLeft(mRight, -RANGE);
        mDays = days;
    }

    @Override
    public boolean isTimeWithinRange(Date date) {
        Date left = DateUtility.setDayAndReturn(date, mLeft);
        Date right = DateUtility.setDayAndReturn(date, mRight);
        return isDayWanted(date) && date.after(left) && date.before(right);
    }

    private boolean isDayWanted(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) + "";
        return mDays.contains(dayOfWeek);
    }

}
