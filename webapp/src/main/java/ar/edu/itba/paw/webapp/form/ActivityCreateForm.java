package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ActivityCreateForm {


    @Size(min = 3, max = 40)
    private String name;

    @Pattern(regexp = "[a-zA-Z]+")
    @Size(min = 3, max = 40)
    private String category;

    @Size(min = 3, max = 100)
    private String placeInput;

    @NotNull
    @Size(min = 8, max = 10)
    private String startDate;

    @NotNull
    @Size(min = 8, max = 10)
    private String endDate;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPlaceInput() {
        return placeInput;
    }

    public void setPlaceInput(String placeInput) {
        this.placeInput = placeInput;
    }
}
