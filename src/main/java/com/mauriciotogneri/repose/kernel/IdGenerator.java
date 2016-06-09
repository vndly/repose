package com.mauriciotogneri.repose.kernel;

public class IdGenerator
{
    private int nextId = 1;

    public int nextInt()
    {
        return nextId++;
    }

    public String nextString()
    {
        return String.valueOf(nextInt());
    }
}