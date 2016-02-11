package com.mauriciotogneri.repose.kernel;

import com.mauriciotogneri.repose.helpers.ResourceHelper;
import com.mauriciotogneri.repose.kernel.endpoints.EndPoint;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

final class Task implements Runnable
{
    private final Socket socket;
    private final Service service;

    public Task(Socket socket, Service service)
    {
        this.socket = socket;
        this.service = service;
    }

    @Override
    public void run()
    {
        long startTime = System.nanoTime();

        try
        {
            Request request = new Request(socket);

            service.onRequest(request);

            EndPoint endPoint = service.getEndPoint(request.getPath());

            request.loadPathParameters(endPoint.getPath());

            Response response = endPoint.getResponse(request);

            send(response, socket, startTime);
        }
        catch (Exception e)
        {
            returnError(service.onException(e), socket, e, startTime);
        }
        finally
        {
            ResourceHelper.close(socket);
        }
    }

    private void returnError(Response response, Socket socket, Exception e, long startTime)
    {
        service.onError(e);

        try
        {
            send(response, socket, startTime);
        }
        catch (IOException ex)
        {
            service.onError(ex);
        }
    }

    private void send(Response response, Socket socket, long startTime) throws IOException
    {
        OutputStreamWriter writer = null;

        try
        {
            writer = new OutputStreamWriter(socket.getOutputStream());
            writer.write(response.toString());
            writer.flush();

            int requestTime = getRequestTime(startTime);
            service.onResponse(response, requestTime);
        }
        finally
        {
            ResourceHelper.close(writer);
        }
    }

    private int getRequestTime(long startTime)
    {
        return (int) TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
    }
}