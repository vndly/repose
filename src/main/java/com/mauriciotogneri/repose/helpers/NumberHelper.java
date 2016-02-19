package com.mauriciotogneri.repose.helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class NumberHelper
{
    private NumberHelper()
    {
    }

    public static double round(double value, int places)
    {
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);

        return bigDecimal.doubleValue();
    }
}