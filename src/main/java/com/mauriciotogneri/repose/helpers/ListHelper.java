package com.mauriciotogneri.repose.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class ListHelper
{
    private ListHelper()
    {
    }

    public static <T> T random(List<T> list)
    {
        return list.get(RandomHelper.nextInt(list.size()));
    }

    public static <T> List<T> split(Optional<String> list, Factory<T> factory)
    {
        List<T> result = new ArrayList<>();

        if (list.isPresent())
        {
            String[] parts = list.get().split(",");

            for (String part : parts)
            {
                result.add(factory.create(part));
            }
        }

        return result;
    }

    public static <T> String join(List<T> list)
    {
        StringBuilder builder = new StringBuilder();

        for (T element : list)
        {
            if (builder.length() != 0)
            {
                builder.append(", ");
            }

            builder.append(element.toString());
        }

        return builder.toString();
    }

    public interface Factory<T>
    {
        T create(String input);
    }
}