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

    public static synchronized String getDate(DateTime dateTime)
    {
        return defaultFormat.print(dateTime);
    }

    public static synchronized DateTime getDate(String value)
    {
        return defaultFormat.parseDateTime(value);
    }
}