package com.mauriciotogneri.repose.helpers;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public final class ResourceHelper
{
    private ResourceHelper()
    {
    }

    public static String read(String path) throws IOException
    {
        File file = new File(path);
        FileInputStream inputStream = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        //noinspection ResultOfMethodCallIgnored
        inputStream.read(data);

        close(inputStream);

        return new String(data, "UTF-8");
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