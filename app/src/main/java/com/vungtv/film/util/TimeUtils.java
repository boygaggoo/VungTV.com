package com.vungtv.film.util;

import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimeZone;


public class TimeUtils {
    private static StringBuilder formatBuilder = new StringBuilder();
    private static Formatter formatter = new Formatter(formatBuilder, Locale.getDefault());

    /**
     * Formats the specified milliseconds to a human readable format
     * in the form of (Hours : Minutes : Seconds).  If the specified
     * milliseconds is less than 0 the resulting format will be
     * "--:--" to represent an unknown time
     *
     * @param milliseconds The time in milliseconds to format
     * @return The human readable time
     */
    public static String formatTime(long milliseconds) {
        if (milliseconds < 0) {
            return "--:--";
        }

        long seconds = (milliseconds % DateUtils.MINUTE_IN_MILLIS) / DateUtils.SECOND_IN_MILLIS;
        long minutes = (milliseconds % DateUtils.HOUR_IN_MILLIS) / DateUtils.MINUTE_IN_MILLIS;
        long hours = (milliseconds % DateUtils.DAY_IN_MILLIS) / DateUtils.HOUR_IN_MILLIS;

        StringBuilder formatBuilder = new StringBuilder();
        formatBuilder.setLength(0);
        Formatter formatter = new Formatter(formatBuilder, Locale.getDefault());
        if (hours > 0) {
            return formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        }

        return formatter.format("%02d:%02d", minutes, seconds).toString();
    }

    /**
     * Get time now with GMT +7;
     * @return
     */
    public static long getCurrentTimeMillis() {
        return Calendar.getInstance(TimeZone.getDefault()).getTimeInMillis();
    }

    /**
     * Get time now with GMT +7;
     * @return
     */
    public static String getCurrentDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(Calendar.getInstance(TimeZone.getDefault()).getTime());
    }

    /**
     * Formats the specified milliseconds to a human readable format
     * in the form of (Hours : Minutes : Seconds).  If the specified
     * milliseconds is less than 0 the resulting format will be
     * "--:--" to represent an unknown time
     *
     * @param milliseconds The time in milliseconds to format
     * @return The human readable time
     */
    public static String formatMs(long milliseconds) {
        if (milliseconds < 0) {
            return "--:--";
        }

        long seconds = (milliseconds % DateUtils.MINUTE_IN_MILLIS) / DateUtils.SECOND_IN_MILLIS;
        long minutes = (milliseconds % DateUtils.HOUR_IN_MILLIS) / DateUtils.MINUTE_IN_MILLIS;
        long hours = (milliseconds % DateUtils.DAY_IN_MILLIS) / DateUtils.HOUR_IN_MILLIS;

        formatBuilder.setLength(0);
        if (hours > 0) {
            return formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        }

        return formatter.format("%02d:%02d", minutes, seconds).toString();
    }

    public static String convertTimeStampToDate(long timeStamp){
        DateFormat objFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Calendar objCalendar =
                Calendar.getInstance(TimeZone.getDefault());
        objCalendar.setTimeInMillis(timeStamp * 1000);//edit
        String result = objFormatter.format(objCalendar.getTime());
        objCalendar.clear();
        return result;
    }

    public static String convertTimeStampToDateTime(long timeStamp){
        DateFormat objFormatter = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault());

        Calendar objCalendar =
                Calendar.getInstance(TimeZone.getDefault());
        objCalendar.setTimeInMillis(timeStamp * 1000);
        String result = objFormatter.format(objCalendar.getTime());
        objCalendar.clear();
        return result;
    }
}
