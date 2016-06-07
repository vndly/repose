package com.mauriciotogneri.repose.helpers;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public final class DateHelper
{
    private static final DateTimeFormatter defaultFormat = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private DateHelper()
    {
    }

    public static synchronized String date(DateTime dateTime)
    {
        return defaultFormat.print(dateTime);
    }

    public static synchronized DateTime date(String value)
    {
        return defaultFormat.parseDateTime(value);
    }
}