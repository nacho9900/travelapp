package ar.edu.itba.paw.webapp.dto.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FutureLocalDateValidator implements ConstraintValidator<Future, LocalDate>
{
    @Override
    public void initialize( Future constraintAnnotation ) {

    }

    @Override
    public boolean isValid( LocalDate value, ConstraintValidatorContext context ) {
        return value == null || value.isAfter( LocalDate.now() );
    }
}
