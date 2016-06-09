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
        return new Random().nextDouble();
    }

    public static double nextDouble(double max)
    {
        return new Random().nextDouble() * max;
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