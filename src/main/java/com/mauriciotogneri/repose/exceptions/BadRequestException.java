package com.mauriciotogneri.repose.exceptions;

import com.mauriciotogneri.repose.types.StatusCode;

public final class BadRequestException extends RequestErrorException
{
    @SuppressWarnings("ThrowableInstanceNeverThrown")
    public static final BadRequestException DEFAULT = new BadRequestException("Bad request");

    public BadRequestException(String message)
    {
        this(message, null);
    }

    public BadRequestException(String message, Throwable cause)
    {
        super(StatusCode.BAD_REQUEST, message, cause);
    }
}