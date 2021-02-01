package ar.edu.itba.paw.webapp.dto.validators;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<EmailConstraint, String>
{
    @Override
    public void initialize( EmailConstraint constraintAnnotation ) {
    }

    @Override
    public boolean isValid( String value, ConstraintValidatorContext context ) {
        boolean isValidEMail = true;

        try {
            InternetAddress email = new InternetAddress( value );
        }
        catch ( AddressException ex ) {
            isValidEMail = false;
        }

        return isValidEMail;
    }
}
