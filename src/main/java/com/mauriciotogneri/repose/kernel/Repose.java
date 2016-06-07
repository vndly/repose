package com.mauriciotogneri.repose.kernel;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

import java.util.List;

public abstract class Repose
{
    private final ServerConfig config;
    private final List<Service> services;

    public Repose(ServerConfig config, List<Service> services)
    {
        this.config = config;
        this.services = services;
    }

    public void start() throws Exception
    {
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(config.port);
        connector.setHost("localhost");
        server.setConnectors(new Connector[] {connector});

        onStart(services);

        server.setHandler(new Dispatcher(this, services));
        server.start();
        server.join();
    }

    public abstract void onStart(List<Service> services);

    public abstract void onRequest(Request request);

    public abstract void onResponse(Response response, int time);

    public abstract Response onException(Exception exception);
}