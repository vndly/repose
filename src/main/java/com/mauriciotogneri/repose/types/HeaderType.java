package com.mauriciotogneri.repose.types;

public enum HeaderType
{
    // request
    ACCEPT("Accept"), //
    ACCEPT_CHARSET("Accept-Charset"), //
    ACCEPT_ENCODING("Accept-Encoding"), //
    ACCEPT_LANGUAGE("Accept-Language"), //
    COOKIE("Cookie"), //

    // response
    SET_COOKIE("Set-Cookie"), //
    LINK("Link"), //

    // both
    CONTENT_LENGTH("Content-Length"), //
    CONTENT_TYPE("Content-Type");

    private final String value;

    HeaderType(String value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return value;
    }
}