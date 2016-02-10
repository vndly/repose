package com.mauriciotogneri.repose.exceptions;

import com.mauriciotogneri.repose.types.StatusCode;

public final class NonAcceptableException extends RequestErrorException
{
    public NonAcceptableException(String message)
    {
        this(message, null);
    }

    public NonAcceptableException(String message, Throwable cause)
    {
        super(StatusCode.NON_ACCEPTABLE, message, cause);
    }
}