package com.mauriciotogneri.repose.helpers;

public final class EnumHelper
{
    private EnumHelper()
    {
    }

    public static <T> T random(Class<T> clazz)
    {
        T[] values = clazz.getEnumConstants();

        return values[RandomHelper.get(0, values.length - 1)];
    }
}
