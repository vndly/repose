package com.mauriciotogneri.repose.kernel.parameters;

import javax.servlet.http.HttpServletRequest;

public final class UrlParameters extends MapParameters
{
    public UrlParameters(HttpServletRequest request)
    {
        for (Entry<String, String[]> entry : request.getParameterMap().entrySet())
        {
            String name = entry.getKey();
            String[] value = entry.getValue();

            if (value.length > 0)
            {
                put(name, value);
            }
        }
    }
}