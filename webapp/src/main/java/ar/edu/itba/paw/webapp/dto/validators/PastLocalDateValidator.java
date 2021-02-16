package ar.edu.itba.paw.webapp.dto.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class PastLocalDateValidator implements ConstraintValidator<Past, LocalDate>
{

    @Override
    public void initialize( Past constraintAnnotation ) {

    }

    @Override
    public boolean isValid( LocalDate value, ConstraintValidatorContext context ) {
        return value == null || value.isBefore( LocalDate.now() );
    }
}
