package com.mauriciotogneri.repose.kernel;

import com.mauriciotogneri.repose.exceptions.BadRequestException;
import com.mauriciotogneri.repose.exceptions.MethodNotAllowedException;
import com.mauriciotogneri.repose.helpers.JsonHelper;
import com.mauriciotogneri.repose.kernel.parameters.HeaderParameters;
import com.mauriciotogneri.repose.kernel.parameters.PathParameters;
import com.mauriciotogneri.repose.kernel.parameters.UrlParameters;
import com.mauriciotogneri.repose.types.Header;
import com.mauriciotogneri.repose.types.Method;

import java.lang.reflect.Field;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

public final class Request
{
    private final Method method;
    private final String path;
    private final UrlParameters urlParameters;
    private final PathParameters pathParameters;
    private final HeaderParameters headerParameters;
    private final String body;

    public Request(PathParameters pathParameters, HttpServletRequest servletRequest) throws Exception
    {
        this.method = method(servletRequest.getMethod());
        this.path = path(servletRequest);
        this.urlParameters = new UrlParameters(servletRequest);
        this.pathParameters = pathParameters;
        this.headerParameters = new HeaderParameters(servletRequest);
        this.body = body(servletRequest);
    }

    public Method method()
    {
        return method;
    }

    public <T> T urlParameters(Class<T> clazz) throws BadRequestException
    {
        return objectFromJson(clazz, urlParameters.json(), "Invalid url parameters");
    }

    public <T> T pathParameters(Class<T> clazz) throws BadRequestException
    {
        return objectFromJson(clazz, pathParameters.json(), "Invalid path parameters");
    }

    @SuppressWarnings("unchecked")
    public <T> T header(String key)
    {
        return (T) headerParameters.get(key);
    }

    public <T> T header(Header key)
    {
        return header(key.toString());
    }

    public <T> T body(Class<T> clazz) throws BadRequestException
    {
        return objectFromJson(clazz, body, "Invalid body");
    }

    private <T> T objectFromJson(Class<T> clazz, String json, String message) throws BadRequestException
    {
        try
        {
            T object = JsonHelper.object(json, clazz);
            Field[] fields = clazz.getFields();

            for (Field field : fields)
            {
                Object value = field.get(object);

                validateParameter(field, value);
            }

            return object;
        }
        catch (BadRequestException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new BadRequestException(message, e);
        }
    }

    private void validateParameter(Field field, Object value) throws BadRequestException
    {
        if ((!field.getType().equals(Optional.class)) && (value == null))
        {
            throw new BadRequestException(String.format("Missing parameter '%s'", field.getName()));
        }
    }

    private String path(HttpServletRequest request)
    {
        String requestURL = request.getRequestURI();
        String queryString = request.getQueryString();

        if (queryString == null)
        {
            return String.format("%s %s %s", request.getMethod(), requestURL, request.getProtocol());
        }
        else
        {
            return String.format("%s %s?%s, %s", request.getMethod(), requestURL, queryString, request.getProtocol());
        }
    }

    private Method method(String input) throws MethodNotAllowedException
    {
        try
        {
            return Enum.valueOf(Method.class, input);
        }
        catch (Exception e)
        {
            throw MethodNotAllowedException.DEFAULT;
        }
    }

    private String body(HttpServletRequest request) throws Exception
    {
        Scanner scanner = new Scanner(request.getInputStream(), "UTF-8").useDelimiter("\\A");

        return scanner.hasNext() ? scanner.next() : "";
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("%s\n", path));

        for (Entry<String, Object> header : headerParameters.entrySet())
        {
            builder.append(String.format("%s: %s\n", header.getKey(), header.getValue()));
        }

        if (!body.isEmpty())
        {
            builder.append("\n").append(body);
        }

        return builder.toString();
    }
}