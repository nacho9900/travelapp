package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Size;

public class UserUpdateBioForm {

    @Size(max = 500)
    private String Biography;

    public String getBiography() {
        return Biography;
    }

    public void setBiography(String biography) {
        Biography = biography;
    }
}
