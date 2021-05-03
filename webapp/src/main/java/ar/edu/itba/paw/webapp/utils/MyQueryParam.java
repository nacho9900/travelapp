package ar.edu.itba.paw.webapp.utils;

public class MyQueryParam
{
    private final String name;
    private final Object value;

    public MyQueryParam( String name, Object value ) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
