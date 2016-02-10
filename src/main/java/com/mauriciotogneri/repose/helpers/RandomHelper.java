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

    public static int get(int min, int max)
    {
        return new Random().nextInt((max - min) + 1) + min;
    }
}