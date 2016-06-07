package com.mauriciotogneri.repose.kernel.endpoints;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class EndPointDirectory
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
            System.err.println("End point with same path already exist: " + endPoint.path());
            System.exit(0);
        }
    }

    public Optional<EndPoint> get(String path)
    {
        for (EndPoint endPoint : endPoints)
        {
            if (endPoint.matches(path))
            {
                return Optional.of(endPoint);
            }
        }

        return Optional.empty();
    }
}