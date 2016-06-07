package com.mauriciotogneri.repose.kernel.endpoints;

import com.mauriciotogneri.repose.annotations.ContentType;
import com.mauriciotogneri.repose.exceptions.BadRequestException;
import com.mauriciotogneri.repose.exceptions.MethodNotAllowedException;
import com.mauriciotogneri.repose.exceptions.NonAcceptableException;
import com.mauriciotogneri.repose.kernel.Path;
import com.mauriciotogneri.repose.kernel.Request;
import com.mauriciotogneri.repose.kernel.Response;
import com.mauriciotogneri.repose.types.Header;
import com.mauriciotogneri.repose.types.Method;
import com.mauriciotogneri.repose.types.MimeType;
import com.mauriciotogneri.repose.types.StatusCode;

public class EndPoint
{
    private final Path path;

    public EndPoint()
    {
        com.mauriciotogneri.repose.annotations.Path path = getClass().getAnnotation(com.mauriciotogneri.repose.annotations.Path.class);

        this.path = new Path(path.value());
    }

    public Path path()
    {
        return path;
    }

    public boolean matches(String path)
    {
        return this.path.matches(path);
    }

    public Response response(Request request) throws Exception
    {
        try
        {
            Method method = request.method();

            switch (method)
            {
                case GET:
                    validateMethod(method, request);
                    return get(request);

                case POST:
                    validateMethod(method, request);
                    return post(request);

                case PUT:
                    validateMethod(method, request);
                    return put(request);

                case DELETE:
                    validateMethod(method, request);
                    return delete(request);

                case PATCH:
                    validateMethod(method, request);
                    return patch(request);

                case HEAD:
                    validateMethod(method, request);
                    return head(request);

                case TRACE:
                    validateMethod(method, request);
                    return trace(request);

                case OPTIONS:
                    validateMethod(method, request);
                    return options(request);

                case CONNECT:
                    validateMethod(method, request);
                    return connect(request);
            }

            throw MethodNotAllowedException.DEFAULT;
        }
        catch (Exception e)
        {
            Response response = onException(e);

            if (response != null)
            {
                return response;
            }
            else
            {
                throw e;
            }
        }
    }

    private void validateMethod(Method methodName, Request request) throws Exception
    {
        try
        {
            java.lang.reflect.Method method = getClass().getMethod(methodName.toString().toLowerCase(), Request.class);
            ContentType contentType = method.getAnnotation(ContentType.class);

            if (contentType != null)
            {
                for (MimeType mimeType : contentType.value())
                {
                    assertContentType(request, mimeType);
                }
            }
        }
        catch (NonAcceptableException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new BadRequestException("Invalid method", e);
        }
    }

    protected void assertContentType(Request request, MimeType mimeType) throws Exception
    {
        assertHeader(request, Header.CONTENT_TYPE, mimeType.toString(), new NonAcceptableException("Resource representation is only available with these Content-Types: " + mimeType.toString()));
    }

    protected void assertHeader(Request request, Header key, String value, Exception exception) throws Exception
    {
        String header = request.header(key);

        if ((header != null) && (!header.contains(value)))
        {
            throw exception;
        }
    }

    @SuppressWarnings("UnusedParameters")
    public synchronized Response onException(Exception exception) throws Exception
    {
        return null;
    }

    @SuppressWarnings("UnusedParameters")
    public synchronized Response get(Request request) throws Exception
    {
        return Response.empty(StatusCode.METHOD_NOT_ALLOWED);
    }

    @SuppressWarnings("UnusedParameters")
    public synchronized Response post(Request request) throws Exception
    {
        return Response.empty(StatusCode.METHOD_NOT_ALLOWED);
    }

    @SuppressWarnings("UnusedParameters")
    public synchronized Response put(Request request) throws Exception
    {
        return Response.empty(StatusCode.METHOD_NOT_ALLOWED);
    }

    @SuppressWarnings("UnusedParameters")
    public synchronized Response delete(Request request) throws Exception
    {
        return Response.empty(StatusCode.METHOD_NOT_ALLOWED);
    }

    @SuppressWarnings("UnusedParameters")
    public synchronized Response patch(Request request) throws Exception
    {
        return Response.empty(StatusCode.METHOD_NOT_ALLOWED);
    }

    @SuppressWarnings("UnusedParameters")
    public synchronized Response head(Request request) throws Exception
    {
        return Response.empty(StatusCode.METHOD_NOT_ALLOWED);
    }

    @SuppressWarnings("UnusedParameters")
    public synchronized Response trace(Request request) throws Exception
    {
        return Response.empty(StatusCode.METHOD_NOT_ALLOWED);
    }

    @SuppressWarnings("UnusedParameters")
    public synchronized Response options(Request request) throws Exception
    {
        return Response.empty(StatusCode.METHOD_NOT_ALLOWED);
    }

    @SuppressWarnings("UnusedParameters")
    public synchronized Response connect(Request request) throws Exception
    {
        return Response.empty(StatusCode.METHOD_NOT_ALLOWED);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || !(o instanceof EndPoint))
        {
            return false;
        }

        EndPoint endPoint = (EndPoint) o;

        return path.equals(endPoint.path);

    }

    @Override
    public int hashCode()
    {
        return path.hashCode();
    }
}