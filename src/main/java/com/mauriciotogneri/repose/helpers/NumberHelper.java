package com.mauriciotogneri.repose.helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class NumberHelper
{
    private NumberHelper()
    {
    }

    public static int randomInt(int places)
    {
        return RandomHelper.nextInt(places);
    }

    public static double randomAsDecimal(int places)
    {
        return round(RandomHelper.nextDouble() * Math.pow(10, places), 2);
    }

    public static double randomPercentage()
    {
        return round(RandomHelper.nextDouble() * RandomHelper.sign(), 2);
    }

    public static double round(double value, int places)
    {
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);

        return bigDecimal.doubleValue();
    }
}