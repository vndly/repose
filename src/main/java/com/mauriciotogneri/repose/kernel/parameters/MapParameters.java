package com.mauriciotogneri.repose.kernel.parameters;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map.Entry;

public class MapParameters extends HashMap<String, Object>
{
    public String getJson()
    {
        JsonObject jsonObject = new JsonObject();

        for (Entry<String, Object> entry : entrySet())
        {
            String key = entry.getKey();
            Object value = entry.getValue();

            jsonObject.addProperty(key, value.toString());
        }

        return jsonObject.toString();
    }
}