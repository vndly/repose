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
        long startTime = System.nanoTime();

        try
        {
            for (Service service : services)
            {
                Optional<EndPoint> endPoint = service.endPoint(target);

                if (endPoint.isPresent())
                {
                    Response endPointResponse = handle(endPoint.get(), target, request);
                    fillResponse(endPointResponse, response, startTime);
                    baseRequest.setHandled(true);
                    return;
                }
            }

            throw NotFoundException.DEFAULT;
        }
        catch (Exception e)
        {
            Response exceptionResponse = repose.onException(e);
            fillResponse(exceptionResponse, response, startTime);
        }
    }

    private Response handle(EndPoint endPoint, String target, HttpServletRequest servletRequest) throws Exception
    {
        Request request = new Request(endPoint.path().pathParameters(target), servletRequest);

        repose.onRequest(request);

        return endPoint.response(request);
    }

    private void fillResponse(Response response, HttpServletResponse servletResponse, long startTime)
    {
        repose.onResponse(response, requestTime(startTime));
        response.fillServletResponse(servletResponse);
    }

    private int requestTime(long startTime)
    {
        return (int) TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
    }
}