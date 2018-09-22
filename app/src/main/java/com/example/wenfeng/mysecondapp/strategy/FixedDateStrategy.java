package com.example.wenfeng.mysecondapp.strategy;

import java.util.Date;

public class FixedDateStrategy implements IResetStrategy {
    private Date mDate;

    public FixedDateStrategy(Date date){
        mDate = date;
    }

    @Override
    public Date calcDate() {
        return mDate;
    }
}
