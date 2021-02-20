package ar.edu.itba.paw.webapp.dto.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class FutureLocalDateTimeValidator implements ConstraintValidator<Future, LocalDateTime>
{
    @Override
    public void initialize( Future constraintAnnotation ) {

    }

    @Override
    public boolean isValid( LocalDateTime value, ConstraintValidatorContext context ) {
        return value == null || value.isAfter( LocalDateTime.now( ZoneOffset.UTC) );
    }
}
