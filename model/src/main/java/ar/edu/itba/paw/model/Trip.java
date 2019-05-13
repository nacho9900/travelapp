package ar.edu.itba.paw.model;

import java.util.Calendar;

public class Trip {


    private final long id;
    private long startPlaceId;
    private String name;
    private String description;
    private Calendar startDate;
    private Calendar endDate;

    public Trip(long id, long startPlaceId, String name, String description, Calendar startDate, Calendar endDate) {
        this.id = id;
        this.startPlaceId = startPlaceId;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public long getStartPlaceId() {
        return startPlaceId;
    }

    public void setStartPlaceId(long startPlaceId) {
        this.startPlaceId = startPlaceId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }
}
