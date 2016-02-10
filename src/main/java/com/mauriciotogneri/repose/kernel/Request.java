package com.mauriciotogneri.repose.kernel;

import com.mauriciotogneri.repose.annotations.Range;
import com.mauriciotogneri.repose.annotations.Required;
import com.mauriciotogneri.repose.exceptions.BadRequestException;
import com.mauriciotogneri.repose.exceptions.MethodNotAllowedException;
import com.mauriciotogneri.repose.helpers.JsonHelper;
import com.mauriciotogneri.repose.kernel.parameters.HeaderParameters;
import com.mauriciotogneri.repose.kernel.parameters.PathParameters;
import com.mauriciotogneri.repose.kernel.parameters.UrlParameters;
import com.mauriciotogneri.repose.types.Header;
import com.mauriciotogneri.repose.types.Method;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public final class Request
{
    private final String ip;
    private final Method method;
    private final String path;
    private final UrlParameters urlParameters;
    private final PathParameters pathParameters;
    private final HeaderParameters headerParameters;
    private final String body;
    private final RequestStructure requestStructure;

    public Request(Socket socket) throws BadRequestException, MethodNotAllowedException
    {
        this.requestStructure = new RequestStructure(socket);

        this.ip = socket.getInetAddress().getHostAddress();
        this.method = getMethod(requestStructure.path);
        this.path = getPath(requestStructure.path);
        this.urlParameters = getUrlParameters(requestStructure.path);
        this.pathParameters = new PathParameters();
        this.headerParameters = getHeaderParameters(requestStructure.headers);
        this.body = requestStructure.body;
    }

    public String getIp()
    {
        return ip;
    }

    public Method getMethod()
    {
        return method;
    }

    public String getPath()
    {
        return path;
    }

    public <T> T getUrlParameters(Class<T> clazz) throws BadRequestException
    {
        return getObjectFromJson(clazz, urlParameters.getJson(), "Invalid url parameters");
    }

    public <T> T getPathParameters(Class<T> clazz) throws BadRequestException
    {
        return getObjectFromJson(clazz, pathParameters.getJson(), "Invalid path parameters");
    }

    @SuppressWarnings("unchecked")
    public <T> T getHeader(String key)
    {
        return (T) headerParameters.get(key);
    }

    public <T> T getHeader(Header key)
    {
        return getHeader(key.toString());
    }

    public <T> T getBody(Class<T> clazz) throws BadRequestException
    {
        return getObjectFromJson(clazz, body, "Invalid body");
    }

    private <T> T getObjectFromJson(Class<T> clazz, String json, String message) throws BadRequestException
    {
        try
        {
            T object = JsonHelper.getObject(json, clazz);
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
        Required required = field.getAnnotation(Required.class);

        if (required != null)
        {
            if (value == null)
            {
                throw new BadRequestException("Missing parameter '" + field.getName() + "'");
            }
            else if (required.value() && (value instanceof String) && (value.toString().isEmpty()))
            {
                throw new BadRequestException("Empty parameter '" + field.getName() + "'");
            }
        }

        if (value != null)
        {
            Range range = field.getAnnotation(Range.class);

            if (range != null)
            {
                if (value instanceof Integer)
                {
                    Integer intValue = (Integer) value;

                    if ((intValue < range.min()) || (intValue > range.max()))
                    {
                        throw new BadRequestException("Parameter '" + field.getName() + "' with value " + intValue + " out of range [" + range.min() + ", " + range.max() + "]");
                    }
                }
            }
        }
    }

    public void loadPathParameters(Path path) throws BadRequestException
    {
        pathParameters.putAll(path.getPathParameters(this.path));
    }

    private Method getMethod(String input) throws MethodNotAllowedException
    {
        String methodValue = input.split(" ")[0].trim();

        try
        {
            return Enum.valueOf(Method.class, methodValue);
        }
        catch (Exception e)
        {
            throw MethodNotAllowedException.DEFAULT;
        }
    }

    private String[] getPathRequest(String input) throws BadRequestException
    {
        try
        {
            return input.split(" ")[1].trim().split("\\?");
        }
        catch (Exception e)
        {
            throw new BadRequestException("Invalid path: " + input);
        }
    }

    private String getPath(String input) throws BadRequestException
    {
        try
        {
            String[] pathRequest = getPathRequest(input);

            return pathRequest[0].trim();
        }
        catch (Exception e)
        {
            throw new BadRequestException("Invalid path: " + input);
        }
    }

    private UrlParameters getUrlParameters(String input) throws BadRequestException
    {
        try
        {
            UrlParameters urlParameters = new UrlParameters();
            String[] pathRequest = getPathRequest(input);

            if (pathRequest.length > 1)
            {
                String params = pathRequest[1].trim();
                String[] parts = params.split("&");

                for (String part : parts)
                {
                    String[] parameter = part.split("=");

                    if (parameter.length > 1)
                    {
                        urlParameters.put(parameter[0], parameter[1]);
                    }
                }
            }

            return urlParameters;
        }
        catch (Exception e)
        {
            throw new BadRequestException("Invalid url parameters: " + input);
        }
    }

    private HeaderParameters getHeaderParameters(List<String> headers)
    {
        HeaderParameters headerParameters = new HeaderParameters();

        for (String header : headers)
        {
            String[] parts = header.split(":");
            headerParameters.put(parts[0].trim(), parts[1].trim());
        }

        return headerParameters;
    }

    @Override
    public String toString()
    {
        return requestStructure.toString();
    }

    private static class RequestStructure
    {
        public String path = "";
        public final List<String> headers = new ArrayList<>();
        public String body = "";

        private RequestStructure(Socket socket) throws BadRequestException
        {
            try
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                boolean pathProcessed = false;
                int contentLength = 0;
                String line;

                while (((line = reader.readLine()) != null) && (line.length() > 0))
                {
                    if (!pathProcessed)
                    {
                        path = URLDecoder.decode(line, "UTF-8");
                        pathProcessed = true;
                    }
                    else
                    {
                        headers.add(line);

                        if (line.startsWith(Header.CONTENT_LENGTH.toString()))
                        {
                            contentLength = Integer.parseInt(line.substring(16));
                        }
                    }
                }

                if (contentLength > 0)
                {
                    char[] buffer = new char[contentLength];
                    reader.read(buffer, 0, contentLength);
                    body = new String(buffer);
                }
            }
            catch (Exception e)
            {
                throw new BadRequestException("Invalid request", e);
            }
        }

        @Override
        public String toString()
        {
            StringBuilder builder = new StringBuilder();

            builder.append(path).append("\n");

            for (String header : headers)
            {
                builder.append(header).append("\n");
            }

            if (!body.isEmpty())
            {
                builder.append("\n").append(body);
            }

            return builder.toString();
        }
    }
}