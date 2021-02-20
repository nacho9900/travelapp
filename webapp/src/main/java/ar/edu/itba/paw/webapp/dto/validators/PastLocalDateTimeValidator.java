package ar.edu.itba.paw.webapp.dto.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class PastLocalDateTimeValidator implements ConstraintValidator<Past, LocalDateTime>
{
    @Override
    public void initialize( Past constraintAnnotation ) {

    }

    @Override
    public boolean isValid( LocalDateTime value, ConstraintValidatorContext context ) {
        return value == null || value.isBefore( LocalDateTime.now( ZoneOffset.UTC) );
    }
}
