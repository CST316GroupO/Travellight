package groupo.travellight.app;

/**
 * Created by Tommy Pham on 3/4/14.
 */

/*
    Events class for the objects from yelp

    Add more elements along the way when needed
 */
public class Events {
    private String name;
    private float rating;

    public Events(String name, float rating)
    {
        super();
        this.name = name;
        this.rating = rating;
    }

    public String getName()
    {
        return name;
    }

    public float getRating()
    {
        return rating;
    }
}
