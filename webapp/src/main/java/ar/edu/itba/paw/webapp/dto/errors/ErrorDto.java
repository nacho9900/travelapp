package ar.edu.itba.paw.webapp.dto.errors;

import javax.validation.ConstraintViolation;

public class ErrorDto
{
    private String message;
    private String field;

    public ErrorDto() {
        //For Jackson
    }

    public ErrorDto(String message, String field) {
        this.message = message;
        this.field = field;
    }

    public ErrorDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage( String message ) {
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField( String field ) {
        this.field = field;
    }

    public static <T> ErrorDto fromConstraintViolation( ConstraintViolation<T> violation ) {
        return new ErrorDto(violation.getMessage(), violation.getPropertyPath().toString());
    }
}
