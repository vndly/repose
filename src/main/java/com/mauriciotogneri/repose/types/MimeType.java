package com.mauriciotogneri.repose.types;

public enum MimeType
{
    JSON("application/json"), //
    PDF("application/pdf"), //
    JPG("image/jpg"), //
    PNG("image/png"), //
    HTML("text/html"), //
    CSS("text/css"), //
    PLAIN("text/plain");

    private final String value;

    MimeType(String value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return value;
    }
}