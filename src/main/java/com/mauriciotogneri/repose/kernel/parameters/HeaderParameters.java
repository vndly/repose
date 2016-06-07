package com.mauriciotogneri.repose.kernel.parameters;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

public final class HeaderParameters extends MapParameters
{
    public HeaderParameters(HttpServletRequest request)
    {
        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements())
        {
            String headerName = headerNames.nextElement();

            put(headerName, request.getHeader(headerName));
        }
    }
}