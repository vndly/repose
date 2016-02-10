package com.mauriciotogneri.repose.kernel;

import com.mauriciotogneri.repose.exceptions.BadRequestException;
import com.mauriciotogneri.repose.kernel.parameters.PathParameters;

import java.util.ArrayList;
import java.util.List;

public final class Path
{
    private final String originalPath;
    private final String regexp;
    private final List<PathPart> parts = new ArrayList<>();

    public Path(String originalPath)
    {
        this.originalPath = originalPath;

        StringBuilder builder = new StringBuilder();
        String[] parts = originalPath.split("/");

        for (int i = 1; i < parts.length; i++)
        {
            String part = parts[i];
            builder.append("\\/");

            if ((part.startsWith("{")) && (part.endsWith("}")))
            {
                this.parts.add(new VariablePart(part.substring(1, part.length() - 1)));
                builder.append("[^\\/]+");
            }
            else
            {
                this.parts.add(new FixedPart());
                builder.append(part);
            }
        }

        builder.append("\\/?");

        this.regexp = builder.toString();
    }

    public PathParameters getPathParameters(String path) throws BadRequestException
    {
        try
        {
            PathParameters parameters = new PathParameters();

            String[] sections = path.split("/");

            for (int i = 0; i < parts.size(); i++)
            {
                PathPart part = parts.get(i);
                String value = sections[i + 1];

                if (part instanceof VariablePart)
                {
                    VariablePart variablePart = (VariablePart) part;
                    parameters.put(variablePart.name, value);
                }
            }

            return parameters;
        }
        catch (Exception e)
        {
            throw new BadRequestException("Invalid path parameters", e);
        }
    }

    public boolean matches(String path)
    {
        return path.matches(regexp);
    }

    @Override
    public String toString()
    {
        return originalPath;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass()))
        {
            return false;
        }

        Path path = (Path) o;

        return originalPath.equals(path.originalPath);
    }

    @Override
    public int hashCode()
    {
        return originalPath.hashCode();
    }

    public interface PathPart
    {
    }

    public static class FixedPart implements PathPart
    {
    }

    public static class VariablePart implements PathPart
    {
        public final String name;

        private VariablePart(String name)
        {
            this.name = name;
        }
    }
}