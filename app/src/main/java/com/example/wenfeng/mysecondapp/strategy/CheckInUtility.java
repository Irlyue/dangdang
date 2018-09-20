package com.example.wenfeng.mysecondapp.strategy;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CheckInUtility {
    private List<ICheckInStrategy> mStrategies;
    public CheckInUtility(List<ICheckInStrategy> strategies){
        mStrategies = new ArrayList<>(strategies);
    }

    public void addStrategy(ICheckInStrategy strategy){
        mStrategies.add(strategy);
    }

    public boolean isTimeToCheckIn(){
        Date date = new Date();
        for(ICheckInStrategy strategy: mStrategies){
            if(strategy.isTimeWithinRange(date)){
                return true;
            }
        }
        return false;
    }

}
