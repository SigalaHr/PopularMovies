package com.example.android.myapplication;

import android.content.Context;

import java.text.DateFormat;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateHelper {
    private static Date getDate(String date, String form) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(form);

        return simpleDateFormat.parse(date);
    }

    public static String getLocalDate(Context cont, String date, String form)
            throws ParseException {
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(cont);

        return dateFormat.format(getDate(date, form));
    }
}
