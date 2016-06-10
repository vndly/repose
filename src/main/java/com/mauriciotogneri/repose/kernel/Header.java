package com.mauriciotogneri.repose.kernel;

import com.mauriciotogneri.repose.types.HeaderType;

public class Header
{
    public final HeaderType type;

    public final String value;

    public Header(HeaderType type, String value)
    {
        this.type = type;
        this.value = value;
    }
}