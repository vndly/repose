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

    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
    public static String read(String path) throws IOException
    {
        ClassLoader classLoader = ResourceHelper.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(path);

        return new Scanner(inputStream, "UTF-8").useDelimiter("\\A").next();
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