package com.example.wenfeng.mysecondapp.strategy;

import java.util.Date;

public class RandomCheckInStrategy implements ICheckInStrategy {
    @Override
    public boolean isTimeWithinRange(Date date) {
        return false;
    }
}
