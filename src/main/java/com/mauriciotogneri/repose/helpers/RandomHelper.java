package com.mauriciotogneri.repose.helpers;

import java.util.Random;

public final class RandomHelper
{
    private RandomHelper()
    {
    }

    // 3 out of 5 => [ 0 1 2 ] 3 4
    public static boolean chances(int valid, int outOf)
    {
        return (new Random().nextInt(outOf) < valid);
    }

    public static boolean chance(int outOf)
    {
        return (new Random().nextInt(outOf) == 0);
    }

    public static int get(int min, int max)
    {
        return new Random().nextInt((max - min) + 1) + min;
    }

    public static int nextInt(int max)
    {
        return new Random().nextInt(max);
    }

    public static double nextDouble()
    {
        return NumberHelper.round(new Random().nextDouble(), 2);
    }

    public static double nextSignedDouble()
    {
        return NumberHelper.round(nextDouble() * sign(), 2);
    }

    public static double nextDouble(double max)
    {
        return NumberHelper.round(new Random().nextDouble() * max, 2);
    }

    public static double nextSignedDouble(double max)
    {
        return NumberHelper.round(nextDouble(max) * sign(), 2);
    }

    public static int sign()
    {
        return (new Random().nextBoolean() ? 1 : -1);
    }

    public static boolean nextBoolean()
    {
        return new Random().nextBoolean();
    }
}