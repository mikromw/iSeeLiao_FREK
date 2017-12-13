package com.milfrost.frek.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by vincent on 23/08/17.
 */

public class DateParser {

    public static long getTimeInMillis(String dateTime){
        return getParsedCalendar(dateTime).getTimeInMillis();
    }
    public static long getTimeInMillis(String format,String dateTime){
        return getParsedCalendar(format,dateTime).getTimeInMillis();
    }
    public static String getParsedFormat(String format,String dateTime){
        return new SimpleDateFormat(format).format(getParsedCalendar(dateTime).getTime());
    }
    public static String getParsedFormatFromMillis(String format,long millis){
        return new SimpleDateFormat(format).format(getCalendarFromMillis(millis).getTime());
    }
    public static String getParsedFormatFromCalendar(String format,Calendar calendar){
        return new SimpleDateFormat(format).format(calendar.getTime());
    }
    public static Calendar getCalendarFromMillis(long millis){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar;
    }
    public static String getCurrentTimeInString(){
        SimpleDateFormat sdf  = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date(System.currentTimeMillis()));
    }
    public static Calendar getParsedCalendarWithoutTime(String dateTime){
        DateFormat formatter;
        if(dateTime.length()>10){
            dateTime = dateTime.substring(0,10);
        }
        formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = formatter.parse(dateTime);
        }catch (Exception e){}

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
    public static Calendar getParsedCalendar(String dateTime){
        DateFormat formatter;
        formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = formatter.parse(dateTime);
        }catch (Exception e){}

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
    public static Calendar getParsedCalendar(String pattern,String dateTime){
        DateFormat formatter;
        formatter = new SimpleDateFormat(pattern, Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = formatter.parse(dateTime);
        }catch (Exception e){}

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static String convertFormat(String sourcePattern, String destPattern, String dateTime){
        return new SimpleDateFormat(destPattern).format(getParsedCalendar(sourcePattern,dateTime).getTime());
    }

    public static int getDayDifference(int dayA, int dayB){
        int result = dayA-dayB;
        if(result<0)
            result+=7;
        return result;
    }
}
