package com.waltonbd.smartscale.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtil {

    //*********** Checks if the Date is not Passed ********//
    public static boolean checkIsDatePassed(String givenDate) {

        boolean isPassed = false;

        Date dateGiven, dateSystem;

        Calendar calender = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDate = dateFormat.format(calender.getTime());
        try {
            dateSystem = dateFormat.parse(currentDate);
            dateGiven = dateFormat.parse(givenDate.replaceAll("[a-zA-Z]", " "));

            if (dateSystem.getTime() >= dateGiven.getTime()) {
                isPassed = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("exception", "ParseException= " + e);
        }

        return isPassed;
    }

    //*********** Returns the formatted Timestamp ********//
    public static String getFormattedTimestamp(String mDateFormat) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd MMM, yyyy  hh:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.getDefault());
        Date formattedTimestamp = new Date();
        try {
            formattedTimestamp = inputFormat.parse(mDateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputFormat.format(formattedTimestamp != null ? formattedTimestamp : mDateFormat);
    }

    //*********** Method to get the date in milli seconds from string format ********//
    public static long getDateInMilliSeconds(String givenDateString, String format) {
        String DATE_TIME_FORMAT = format;
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.US);
        long timeInMilliseconds = 1;
        try {
            Date mDate = sdf.parse(givenDateString);
            timeInMilliseconds = mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }

    /**
     * https://stackoverflow.com/a/4292246/5280371
     *
     * @param date
     * @return
     */
    public static String formatToYesterdayOrToday(String date) {
        Date dateTime = new Date();
        try {
            dateTime = new SimpleDateFormat("dd MMM, yyyy HH:mm", Locale.getDefault()).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        assert dateTime != null;
        calendar.setTime(dateTime);
        Calendar today = Calendar.getInstance(TimeZone.getTimeZone("UTC")); // Input dateTime is UTC
        Calendar yesterday = Calendar.getInstance(TimeZone.getTimeZone("UTC")); // Input dateTime is UTC
        yesterday.add(Calendar.DATE, -1);
        DateFormat dateFormatter = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());

        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            return "Today";
        } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            return "Yesterday";
        } else {
            return dateFormatter.format(dateTime);
        }
    }

    //*********** Returns local datetime from UTC ********//
    public static String getFormattedTime(String dateTime) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        Date formattedTimestamp = new Date();
        try {
            formattedTimestamp = inputFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputFormat.format(formattedTimestamp != null ? formattedTimestamp : dateTime);
    }

    //*********** Returns the formatted Timestamp ********//
    public static String getLocalTimeFromUTC(String time) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date formattedTime = new Date();
        try {
            formattedTime = inputFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputFormat.format(formattedTime != null ? formattedTime : time);
    }

    //*********** Return UTC from current time ********//
    public static String getCurrentUTCTime() {
        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat outputFmt = new SimpleDateFormat("HH:mm", Locale.getDefault());
        outputFmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        return outputFmt.format(time);
    }

    //*********** Return Local time ********//
    public static String getCurrentTime() {
        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat outputFmt = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return outputFmt.format(time);
    }

    //*********** Return Local Date ********//
    public static String getCurrentDate() {
        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat outputFmt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return outputFmt.format(time);
    }

    //*********** Return Local DateTime ********//
    public static String getCurrentDateTime() {
        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat outputFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return outputFmt.format(time);
    }

    //*********** Return UTC Date ********//
    public static String getUTCDate() {
        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat outputFmt = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
        outputFmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        return outputFmt.format(time);
    }

    //*********** Return UTC DateTime ********//
    public static String getUTCDateTime() {
        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat outputFmt = new SimpleDateFormat("dd MMM, yyyy HH:mm", Locale.getDefault());
        outputFmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        return outputFmt.format(time);
    }

    //*********** Return Formatted Birthday ********//
    public static String formatBirthday(String pickedDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
        try {
            return outputFormat.format(inputFormat.parse(pickedDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return pickedDate;
    }

    //*********** Return Date from Birthday ********//
    public static Date formatDate(String birthday) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
        try {
            return dateFormat.parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static String createdDateTimeToString(String inputDate, String outputPattern) {
        if (inputDate == null) return null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.getDefault());
        try {
            return outputFormat.format(inputFormat.parse(inputDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return inputDate;
    }

    public static String updatedDateTimeToString(String inputDate, String outputPattern) {
        if (inputDate == null) return null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.getDefault());
        try {
            return outputFormat.format(inputFormat.parse(inputDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return inputDate;
    }
}
