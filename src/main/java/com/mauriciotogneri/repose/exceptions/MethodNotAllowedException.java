package com.mauriciotogneri.repose.exceptions;

import com.mauriciotogneri.repose.types.StatusCode;

public final class MethodNotAllowedException extends RequestErrorException
{
    @SuppressWarnings("ThrowableInstanceNeverThrown")
    public static final MethodNotAllowedException DEFAULT = new MethodNotAllowedException("Method not allowed");

    public MethodNotAllowedException(String message)
    {
        this(message, null);
    }

    public MethodNotAllowedException(String message, Throwable cause)
    {
        super(StatusCode.METHOD_NOT_ALLOWED, message, cause);
    }
}