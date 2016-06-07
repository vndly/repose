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

    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
    public static String read(String path) throws IOException
    {
        System.out.println(1/0);
        ClassLoader classLoader = ResourceHelper.class.getClassLoader();
        File file = new File(classLoader.getResource(path).getFile());
        FileInputStream inputStream = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
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