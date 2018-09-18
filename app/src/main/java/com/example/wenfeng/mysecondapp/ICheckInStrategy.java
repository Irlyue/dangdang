package com.example.wenfeng.mysecondapp;

import java.util.Date;

public interface ICheckInStrategy {
    public boolean isTimeWithinRange(Date date);
}
