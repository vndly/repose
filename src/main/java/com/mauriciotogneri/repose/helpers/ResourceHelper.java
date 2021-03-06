package com.mauriciotogneri.repose.helpers;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public final class ResourceHelper
{
    private ResourceHelper()
    {
    }

    public static String read(InputStream inputStream) throws IOException
    {
        return new Scanner(inputStream, "UTF-8").useDelimiter("\\A").next();
    }

    public static String read(String path) throws IOException
    {
        return read(inputStream(path));
    }

    public static String read(String pattern, Object... parameters) throws IOException
    {
        return read(String.format(pattern, parameters));
    }

    public static InputStream inputStream(String path)
    {
        ClassLoader classLoader = ResourceHelper.class.getClassLoader();

        return classLoader.getResourceAsStream(path);
    }

    public static boolean close(Closeable resource)
    {
        boolean result = false;

        if (resource != null)
        {
            try
            {
                resource.close();
                result = true;
            }
            catch (Exception e)
            {
                // exception swallowed on purpose
            }
        }

        return result;
    }
}