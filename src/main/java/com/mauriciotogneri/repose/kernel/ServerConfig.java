package com.mauriciotogneri.repose.kernel;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class ServerConfig
{
    public final String hostName;
    public final int port;

    public ServerConfig(String configPath) throws IOException
    {
        Properties properties = new Properties();
        properties.load(new FileInputStream(configPath));

        this.hostName = properties.getProperty("host");
        this.port = Integer.parseInt(properties.getProperty("port"));
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