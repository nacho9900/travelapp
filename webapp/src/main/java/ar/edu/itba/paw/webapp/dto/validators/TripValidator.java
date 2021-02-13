package ar.edu.itba.paw.webapp.dto.validators;


import ar.edu.itba.paw.webapp.dto.trips.TripDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TripValidator implements ConstraintValidator<TripConstraint, TripDto>
{
    @Override
    public void initialize( TripConstraint constraintAnnotation ) {

    }

    @Override
    public boolean isValid( TripDto value, ConstraintValidatorContext context ) {
        if ( value.getStartDate() != null && value.getEndDate() != null && value.getStartDate().after(
                value.getEndDate() ) ) {
            return false;
        }

        return true;
    }
}
