package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Size;

public class TripCommentForm {
    @Size(max = 500, min = 1)
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
