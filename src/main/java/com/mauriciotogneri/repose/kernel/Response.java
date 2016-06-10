package com.mauriciotogneri.repose.kernel;

import com.mauriciotogneri.repose.helpers.JsonHelper;
import com.mauriciotogneri.repose.helpers.StringHelper;
import com.mauriciotogneri.repose.types.HeaderType;
import com.mauriciotogneri.repose.types.MimeType;
import com.mauriciotogneri.repose.types.StatusCode;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

public final class Response
{
    private final StatusCode statusCode;
    private final Map<HeaderType, Object> headers;
    private final String content;

    private Response(StatusCode statusCode, Map<HeaderType, Object> headers, String content)
    {
        this.statusCode = statusCode;
        this.content = content;
        this.headers = headers;
    }

    public static Response json(StatusCode statusCode, Object object)
    {
        Builder builder = new Builder(statusCode, MimeType.JSON);
        builder.setJsonContent(object);

        return builder.build();
    }

    public static Response json(Object object)
    {
        Builder builder = new Builder(StatusCode.OK, MimeType.JSON);
        builder.setJsonContent(object);

        return builder.build();
    }

    public static Response json(Object object, List<Header> headers)
    {
        Builder builder = new Builder(StatusCode.OK, MimeType.JSON);
        builder.setJsonContent(object);

        for (Header header : headers)
        {
            builder.addHeader(header.type, header.value);
        }

        return builder.build();
    }

    public static Response jsonContent(String content)
    {
        Builder builder = new Builder(StatusCode.OK, MimeType.JSON);
        builder.setContent(content);

        return builder.build();
    }

    public static Response plain(String content)
    {
        Builder builder = new Builder(StatusCode.OK, MimeType.PLAIN);
        builder.setContent(content);

        return builder.build();
    }

    public static Response html(String content)
    {
        Builder builder = new Builder(StatusCode.OK, MimeType.HTML);
        builder.setContent(content);

        return builder.build();
    }

    public static Response css(String content)
    {
        Builder builder = new Builder(StatusCode.OK, MimeType.CSS);
        builder.setContent(content);

        return builder.build();
    }

    public static Response binary(String content, MimeType mimeType)
    {
        Builder builder = new Builder(StatusCode.OK, mimeType);
        builder.setContent(content);

        return builder.build();
    }

    public static Response empty(StatusCode statusCode)
    {
        Builder builder = new Builder(statusCode, MimeType.PLAIN);

        return builder.build();
    }

    public boolean isBinary()
    {
        for (Entry<HeaderType, Object> entry : headers.entrySet())
        {
            if (entry.getKey() == HeaderType.CONTENT_TYPE)
            {
                String value = entry.getValue().toString();

                if (value.startsWith(MimeType.JPG.toString()) || value.startsWith(MimeType.PNG.toString()) || value.startsWith(MimeType.PDF.toString()))
                {
                    return true;
                }
            }
        }

        return false;
    }

    public void fillServletResponse(HttpServletResponse servletResponse)
    {
        servletResponse.setStatus(statusCode.code);

        for (Entry<HeaderType, Object> entry : headers.entrySet())
        {
            servletResponse.setHeader(entry.getKey().toString(), entry.getValue().toString());
        }

        if (!StringHelper.isEmpty(content))
        {
            try
            {
                PrintWriter writer = servletResponse.getWriter();
                writer.write(String.format("%s\n", content));
                writer.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("HTTP/1.1 %d %s\r\n", statusCode.code, statusCode.value));

        for (Entry<HeaderType, Object> entry : headers.entrySet())
        {
            builder.append(String.format("%s: %s\r\n", entry.getKey(), entry.getValue()));
        }

        builder.append("\r\n");

        if (!StringHelper.isEmpty(content))
        {
            builder.append(content);
        }

        return builder.toString();
    }

    public static class Builder
    {
        private final StatusCode statusCode;
        private final Map<HeaderType, Object> headers;
        private String content;

        public Builder(StatusCode statusCode, MimeType mimeType)
        {
            this.statusCode = statusCode;
            this.headers = new HashMap<>();

            addHeader(HeaderType.CONTENT_TYPE, String.format("%s; charset=utf-8", mimeType));
        }

        public void setContent(String content)
        {
            this.content = content;

            addHeader(HeaderType.CONTENT_LENGTH, content.getBytes().length);
        }

        public void setJsonContent(Object json)
        {
            setContent(JsonHelper.json(json));
        }

        public void addHeader(HeaderType key, Object value)
        {
            this.headers.put(key, value);
        }

        public Response build()
        {
            return new Response(statusCode, headers, content);
        }
    }
}