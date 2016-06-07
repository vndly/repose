package com.mauriciotogneri.repose.kernel;

import java.io.IOException;

public final class ServerConfig
{
    public final String hostName;
    public final int port;

    public ServerConfig(String hostName, String port) throws IOException
    {
        this.hostName = hostName;
        this.port = Integer.parseInt(port);
    }

    public String host()
    {
        if (port != 0)
        {
            return String.format("%s:%s", hostName, port);
        }
        else
        {
            return hostName;
        }
    }
}