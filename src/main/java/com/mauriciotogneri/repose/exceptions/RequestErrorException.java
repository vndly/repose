package com.mauriciotogneri.repose.exceptions;

import com.mauriciotogneri.repose.types.StatusCode;

public class RequestErrorException extends Exception
{
    private final StatusCode statusCode;
    private final String message;

    public RequestErrorException(StatusCode statusCode, String message, Throwable cause)
    {
        super(message, cause);

        this.statusCode = statusCode;
        this.message = message;
    }

    public StatusCode statusCode()
    {
        return statusCode;
    }

    public String message()
    {
        return message;
    }
}