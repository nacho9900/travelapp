package ar.edu.itba.paw.model;

public class Activity {

    private final long id;
    private final String name;
    private final long categoryId;


    public Activity(long id, String name, long categoryId) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getCategoryId() {
        return categoryId;
    }
}
