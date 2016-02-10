package com.mauriciotogneri.repose.exceptions;

import com.mauriciotogneri.repose.types.StatusCode;

public final class UnauthorizedException extends RequestErrorException
{
    @SuppressWarnings("ThrowableInstanceNeverThrown")
    public static final UnauthorizedException INVALID_CREDENTIALS = new UnauthorizedException("Invalid credentials");

    @SuppressWarnings("ThrowableInstanceNeverThrown")
    public static final UnauthorizedException INVALID_AUTHENTICATION = new UnauthorizedException("The supplied authentication is invalid");

    public UnauthorizedException(String message)
    {
        this(message, null);
    }

    public UnauthorizedException(String message, Throwable cause)
    {
        super(StatusCode.UNAUTHORIZED, message, cause);
    }
}