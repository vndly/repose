package com.mauriciotogneri.repose.kernel;

import com.mauriciotogneri.repose.exceptions.NotFoundException;
import com.mauriciotogneri.repose.kernel.endpoints.EndPoint;

import org.eclipse.jetty.server.handler.AbstractHandler;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Dispatcher extends AbstractHandler
{
    private final Repose repose;
    private final List<Service> services;

    public Dispatcher(Repose repose, List<Service> services)
    {
        this.repose = repose;
        this.services = services;
    }

    @Override
    public void handle(String target, org.eclipse.jetty.server.Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            for (Service service : services)
            {
                Optional<EndPoint> endPoint = service.endPoint(target);

                if (endPoint.isPresent())
                {
                    Response endPointResponse = handle(endPoint.get(), target, request);
                    endPointResponse.fillServletResponse(response);
                    break;
                }
            }

            throw NotFoundException.DEFAULT;
        }
        catch (Exception e)
        {
            repose.onException(e).fillServletResponse(response);
        }
    }

    private Response handle(EndPoint endPoint, String target, HttpServletRequest servletRequest) throws Exception
    {
        long startTime = System.nanoTime();

        Request request = new Request(endPoint.path().pathParameters(target), servletRequest);

        repose.onRequest(request);

        Response response = endPoint.response(request);

        repose.onResponse(response, requestTime(startTime));

        return response;
    }

    private int requestTime(long startTime)
    {
        return (int) TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
    }
}