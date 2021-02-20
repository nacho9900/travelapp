package ar.edu.itba.paw.webapp.dto.errors;

import ar.edu.itba.paw.webapp.dto.serializers.CollectionSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ErrorsDto
{
    @JsonSerialize( using = CollectionSerializer.class )
    private final Set<ErrorDto> errors = new HashSet<>();

    public ErrorsDto() {
        //For Jackson
    }

    public Set<ErrorDto> getErrors() {
        return errors;
    }

    public ErrorsDto addError(String message, String field) {
        this.errors.add( new ErrorDto(message, field) );
        return this;
    }

    public ErrorsDto addError(String message) {
        this.errors.add( new ErrorDto(message) );
        return this;
    }

    public boolean isEmpty() {
        return this.errors.isEmpty();
    }

    public <T> ErrorsDto addConstraintsViolations( Set<ConstraintViolation<T>> violations ) {
        this.errors.addAll( violations.stream()
                                      .map( ErrorDto::fromConstraintViolation )
                                      .collect( Collectors.toSet() ) );

        return this;
    }

    public static <T> ErrorsDto fromConstraintsViolations( Set<ConstraintViolation<T>> violations ) {
        ErrorsDto errorsDto = new ErrorsDto();
        errorsDto.errors.addAll( violations.stream()
                                           .map( ErrorDto::fromConstraintViolation )
                                           .collect( Collectors.toSet() ) );
        return errorsDto;
    }
}
