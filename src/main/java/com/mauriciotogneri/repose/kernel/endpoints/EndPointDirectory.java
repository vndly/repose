package com.mauriciotogneri.repose.kernel.endpoints;

import com.mauriciotogneri.repose.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class EndPointDirectory implements Iterable<EndPoint>
{
    private final List<EndPoint> endPoints = new ArrayList<>();

    public void add(EndPoint endPoint)
    {
        if (!endPoints.contains(endPoint))
        {
            endPoints.add(endPoint);
        }
        else
        {
            System.err.println("End point with same path already exist: " + endPoint.getPath());
            System.exit(0);
        }
    }

    public EndPoint get(String path) throws NotFoundException
    {
        for (EndPoint endPoint : endPoints)
        {
            if (endPoint.matches(path))
            {
                return endPoint;
            }
        }

        throw NotFoundException.DEFAULT;
    }

    @Override
    public Iterator<EndPoint> iterator()
    {
        return endPoints.iterator();
    }
}