package com.waltonbd.smartscale.database;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeConverter {

    public static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    @TypeConverter
    public static Date toDate(String stringDate) {
        try {
            return DATE_FORMAT.parse(stringDate);
        } catch (ParseException e) {
            return null;
        }
    }

    @TypeConverter
    public static String toTimestamp(Date date) {
        return DATE_FORMAT.format(date);
    }
}
