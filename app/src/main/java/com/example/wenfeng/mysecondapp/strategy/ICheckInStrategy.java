package com.example.wenfeng.mysecondapp.strategy;

import java.util.Date;

public interface ICheckInStrategy {
    public boolean isTimeWithinRange(Date date);
}
