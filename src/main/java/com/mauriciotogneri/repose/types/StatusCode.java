package com.mauriciotogneri.repose.types;

public enum StatusCode
{
    OK(200, "OK"), //
    CREATED(201, "Created"), //
    NO_CONTENT(204, "No Content"), //

    // client codes
    BAD_REQUEST(400, "Bad Request"), //
    UNAUTHORIZED(401, "Unauthorized"), //
    FORBIDDEN(403, "Forbidden"), //
    NOT_FOUND(404, "Not Found"), //
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"), //
    NON_ACCEPTABLE(406, "Not acceptable"), //

    // server codes
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"), //
    SERVICE_UNAVAILABLE(503, "Service Unavailable");

    public final int code;
    public final String value;

    StatusCode(int code, String value)
    {
        this.code = code;
        this.value = value;
    }
}