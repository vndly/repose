package com.mauriciotogneri.repose.kernel;

import com.mauriciotogneri.repose.helpers.JsonHelper;
import com.mauriciotogneri.repose.helpers.StringHelper;
import com.mauriciotogneri.repose.types.Header;
import com.mauriciotogneri.repose.types.MimeType;
import com.mauriciotogneri.repose.types.StatusCode;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class Response
{
    private final StatusCode statusCode;
    private final Map<Header, Object> headers;
    private final String content;

    private Response(StatusCode statusCode, Map<Header, Object> headers, String content)
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

    public static Response jsonContent(String content)
    {
        Builder builder = new Builder(StatusCode.OK, MimeType.JSON);
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

    public static Response empty(StatusCode statusCode)
    {
        Builder builder = new Builder(statusCode, MimeType.PLAIN);

        return builder.build();
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("HTTP/1.1 ").append(statusCode.code).append(" ").append(statusCode.value).append("\r\n");

        Set<Entry<Header, Object>> entries = headers.entrySet();

        for (Entry<Header, Object> entry : entries)
        {
            builder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
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
        private final Map<Header, Object> headers;
        private String content;

        public Builder(StatusCode statusCode, MimeType mimeType)
        {
            this.statusCode = statusCode;
            this.headers = new HashMap<>();

            addHeader(Header.CONTENT_TYPE, mimeType + "; charset=utf-8");
        }

        public void setContent(String content)
        {
            this.content = content;

            addHeader(Header.CONTENT_LENGTH, content.getBytes().length);
        }

        public void setJsonContent(Object json)
        {
            setContent(JsonHelper.getJson(json));
        }

        public void addHeader(Header key, Object value)
        {
            this.headers.put(key, value);
        }

        public Response build()
        {
            return new Response(statusCode, headers, content);
        }
    }
}