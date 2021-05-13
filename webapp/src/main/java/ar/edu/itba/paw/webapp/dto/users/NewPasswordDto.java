package ar.edu.itba.paw.webapp.dto.users;

import ar.edu.itba.paw.webapp.dto.validators.PasswordConstraint;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

public class NewPasswordDto
{
    @NotBlank
    @Size( max = 100 )
    @PasswordConstraint
    private String passwordNew;
    @NotBlank
    @Size( max = 100 )
    @PasswordConstraint
    private String passwordCurrent;

    public String getPasswordNew() {
        return passwordNew;
    }

    public String getPasswordCurrent() {
        return passwordCurrent;
    }
}
