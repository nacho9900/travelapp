package ar.edu.itba.paw.webapp.dto.trips;

import org.hibernate.validator.constraints.NotBlank;

public class FileDto
{
    @NotBlank
    private String filename;
    @NotBlank
    private String fileBase64;

    public String getFilename() {
        return filename;
    }

    public String getFileBase64() {
        return fileBase64;
    }
}
