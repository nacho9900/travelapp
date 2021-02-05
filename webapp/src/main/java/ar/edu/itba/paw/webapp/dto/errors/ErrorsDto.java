package ar.edu.itba.paw.webapp.dto.errors;

import ar.edu.itba.paw.webapp.dto.serializers.CollectionSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.validation.ConstraintViolation;
import java.util.Set;
import java.util.stream.Collectors;

public class ErrorsDto
{
    @JsonSerialize(using = CollectionSerializer.class )
    private Set<ErrorDto> errors;

    public ErrorsDto() {
        //For Jackson
    }

    public static <T> ErrorsDto fromConstraintsViolations( Set<ConstraintViolation<T>> violations ) {
        ErrorsDto errorsDto = new ErrorsDto();
        errorsDto.errors = violations.stream()
                                     .map( ErrorDto::fromConstraintViolation )
                                     .collect( Collectors.toSet() );
        return errorsDto;
    }
}
