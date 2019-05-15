package ar.edu.itba.paw.model;

public class Activity {

    private final long id;
    private final String name;
    private final String category;
    private final long placeId;


    public Activity(long id, String name, String category, long placeId) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.placeId = placeId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public long getPlaceId() {
        return placeId;
    }
}
