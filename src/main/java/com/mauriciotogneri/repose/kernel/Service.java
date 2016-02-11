package com.mauriciotogneri.repose.kernel;

import com.mauriciotogneri.repose.exceptions.NotFoundException;
import com.mauriciotogneri.repose.exceptions.RequestErrorException;
import com.mauriciotogneri.repose.helpers.ResourceHelper;
import com.mauriciotogneri.repose.kernel.endpoints.EndPoint;
import com.mauriciotogneri.repose.kernel.endpoints.EndPointDirectory;
import com.mauriciotogneri.repose.types.StatusCode;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Service extends Thread
{
    private final String host;
    private final int port;
    private volatile boolean running = false;
    private final boolean onlyLocal;
    private final ExecutorService clientPool;
    private final EndPointDirectory endPointDirectory = new EndPointDirectory();

    public Service(String host, int port, int numberOfThreads, boolean onlyLocal)
    {
        this.host = host;
        this.port = port;
        this.onlyLocal = onlyLocal;
        this.clientPool = Executors.newFixedThreadPool(numberOfThreads);
    }

    protected void addEndPoint(EndPoint endPoint)
    {
        endPointDirectory.add(endPoint);
    }

    public EndPoint getEndPoint(String path) throws NotFoundException
    {
        return endPointDirectory.get(path);
    }

    @Override
    public void run()
    {
        ServerSocket serverSocket = null;

        try
        {
            if (onlyLocal)
            {
                serverSocket = new ServerSocket(port, 0, InetAddress.getByName("localhost"));
            }
            else
            {
                serverSocket = new ServerSocket(port);
            }

            onInitialized(host, endPointDirectory);

            running = true;

            while (running)
            {
                Socket clientSocket = serverSocket.accept();

                try
                {
                    clientPool.submit(new Task(clientSocket, this));
                }
                catch (Exception e)
                {
                    onError(e);

                    ResourceHelper.close(clientSocket);
                }
            }
        }
        catch (Exception e)
        {
            onError(e);
        }
        finally
        {
            ResourceHelper.close(serverSocket);
            onFinish();
        }
    }

    @SuppressWarnings("UnusedParameters")
    public Response onException(Exception exception)
    {
        if (exception instanceof RequestErrorException)
        {
            return Response.empty(((RequestErrorException) exception).getStatusCode());
        }
        else
        {
            return Response.empty(StatusCode.INTERNAL_SERVER_ERROR);
        }
    }

    public abstract void onInitialized(String host, Iterable<EndPoint> endPoints);

    public abstract void onError(Exception e);

    public abstract void onRequest(Request request);

    public abstract void onResponse(Response response, int time);

    public abstract void onFinish();

    public void finish()
    {
        running = false;
    }
}