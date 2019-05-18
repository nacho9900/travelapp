package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class TripCreateForm {

    @Size(min = 5, max = 100)
    private String placeInput;

    @Size(min = 5, max = 100)
    private String name;

    @Size(min = 25, max = 100)
    private String description;

    private String startDate;

    private String endDate;

    public String getPlaceInput() {
        return placeInput;
    }

    public void setPlaceInput(String placeInput) {
        this.placeInput = placeInput;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
