package ar.edu.itba.paw.webapp.dto.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordConstraint, String>
{
    @Override
    public void initialize( PasswordConstraint constraintAnnotation ) {

    }

    @Override
    public boolean isValid( String value, ConstraintValidatorContext context ) {
        return value != null && value.matches( "(?=.*[A-Z])[a-zA-Z0-9]{8,20}" );
    }
}
