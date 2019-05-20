package ar.edu.itba.paw.webapp.form;

import org.springframework.web.multipart.MultipartFile;

public class EditTripForm {

    //more attributes will be added in future iterations

    private MultipartFile imageUpload;

    public MultipartFile getImageUpload() {
        return imageUpload;
    }

    public void setImageUpload(MultipartFile imageUpload) {
        this.imageUpload = imageUpload;
    }
}
