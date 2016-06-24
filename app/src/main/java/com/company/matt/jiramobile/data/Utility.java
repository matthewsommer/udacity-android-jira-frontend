package com.company.matt.jiramobile.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utility {
    public static long normalizeDate(String dateStr) {
        final String iso8601DatePattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ";
        final DateFormat iso8601DateFormat = new SimpleDateFormat(iso8601DatePattern);
        final TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
        iso8601DateFormat.setTimeZone(utcTimeZone);
        Date date = null;

        try {
            date = iso8601DateFormat.parse(dateStr);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return date.getTime();
    }
}