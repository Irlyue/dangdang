package com.example.wenfeng.mysecondapp.utility;

import java.util.Calendar;
import java.util.Date;

public class DateUtility {
    public static Date strToDate(String hms){
        int h, m, s;
        String[] times = hms.split(":");
        h = Integer.parseInt(times[0]);
        m = Integer.parseInt(times[1]);
        s = Integer.parseInt(times[2]);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, h);
        cal.set(Calendar.MINUTE, m);
        cal.set(Calendar.SECOND, s);
        return cal.getTime();
    }

    /**
     * say day=2009-10-20 10:00:00
     *    date=2012-09-01 08:00:00
     * then setDayAndReturn(day, date) returns 2009-10-20 08:00:00
     */
    public static Date setDayAndReturn(Date day, Date date){
        Calendar dayWanted = Calendar.getInstance();
        dayWanted.setTime(day);

        Calendar timeWanted = Calendar.getInstance();
        timeWanted.setTime(date);

        timeWanted.set(Calendar.YEAR, dayWanted.get(Calendar.YEAR));
        timeWanted.set(Calendar.MONTH, dayWanted.get(Calendar.MONTH));
        timeWanted.set(Calendar.DAY_OF_MONTH, dayWanted.get(Calendar.DAY_OF_MONTH));
        return timeWanted.getTime();
    }

    public static Date advanceDate(Date date, int hours, int minutes, int seconds){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.HOUR_OF_DAY, hours);
        calendar.add(Calendar.MINUTE, minutes);
        calendar.add(Calendar.SECOND, seconds);

        return calendar.getTime();
    }

    public static Date advanceDate(Date date, int days){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.DAY_OF_MONTH, days);

        return calendar.getTime();
    }
}
