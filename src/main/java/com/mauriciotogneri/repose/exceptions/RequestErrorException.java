package com.mauriciotogneri.repose.exceptions;

import com.mauriciotogneri.repose.kernel.Response;
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

    public Response getResponse()
    {
        ErrorResponse errorResponse = new ErrorResponse(statusCode.code, message);

        return Response.json(statusCode, errorResponse);
    }

    public static class ErrorResponse
    {
        private final int code;
        private final String message;

        public ErrorResponse(int code, String message)
        {
            this.code = code;
            this.message = message;
        }
    }
}