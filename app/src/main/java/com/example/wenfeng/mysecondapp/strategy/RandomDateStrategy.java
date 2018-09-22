package com.example.wenfeng.mysecondapp.strategy;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class RandomDateStrategy implements IResetStrategy {
    private Date mLeft;
    private Date mRight;

    public RandomDateStrategy(Date left, Date right){
        mLeft = left;
        mRight = right;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Date calcDate() {
        if(mLeft.after(mRight))
            return mLeft;
        ThreadLocalRandom generator = ThreadLocalRandom.current();
        return new Date(generator.nextLong(mLeft.getTime(), mRight.getTime()));
    }
}
