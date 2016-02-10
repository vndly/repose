# Repose

Repose is a simple REST framework for Java:

```java
public class Main extends Service
{
    public static void main(String[] args)
    {
        Main main = new Main("localhost", 7777, 10, true);
        main.addEndPoint(new Echo());
        main.start();
    }
}
```

```java
@Path("/foo/bar/echo")
public class Echo extends EndPoint
{
    @Override
    public synchronized Response get(Request request) throws Exception
    {
        return Response.plain(request.getBody());
    }
}
```