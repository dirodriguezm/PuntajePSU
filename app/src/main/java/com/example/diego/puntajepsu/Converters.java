package com.example.diego.puntajepsu;

import android.arch.persistence.room.TypeConverter;

import java.util.Calendar;

public class Converters {

    @TypeConverter
    public static Calendar fromTimestamp(Long value) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(value);
        return value == null ? null : date;
    }

    @TypeConverter
    public static Long dateToTimestamp(Calendar date) {
        return date == null ? null : date.getTimeInMillis();
    }

}
