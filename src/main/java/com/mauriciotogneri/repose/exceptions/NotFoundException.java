package com.mauriciotogneri.repose.exceptions;

import com.mauriciotogneri.repose.types.StatusCode;

public final class NotFoundException extends RequestErrorException
{
    @SuppressWarnings("ThrowableInstanceNeverThrown")
    public static final NotFoundException DEFAULT = new NotFoundException("The requested resource could not be found");

    public NotFoundException(String message)
    {
        this(message, null);
    }

    public NotFoundException(String message, Throwable cause)
    {
        super(StatusCode.NOT_FOUND, message, cause);
    }
}