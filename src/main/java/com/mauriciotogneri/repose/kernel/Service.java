package com.mauriciotogneri.repose.kernel;

import com.mauriciotogneri.repose.kernel.endpoints.EndPoint;
import com.mauriciotogneri.repose.kernel.endpoints.EndPointDirectory;

import java.util.Optional;

public class Service
{
    private final ServerConfig config;
    private final EndPointDirectory endPointDirectory = new EndPointDirectory();

    public Service(ServerConfig config)
    {
        this.config = config;
    }

    public String host()
    {
        return config.host();
    }

    protected void addEndPoint(EndPoint endPoint)
    {
        endPointDirectory.add(endPoint);
    }

    public Optional<EndPoint> endPoint(String path)
    {
        return endPointDirectory.get(path);
    }

    @Override
    public String toString()
    {
        return String.format("%s => %s", getClass().getSimpleName(), host());
    }
}