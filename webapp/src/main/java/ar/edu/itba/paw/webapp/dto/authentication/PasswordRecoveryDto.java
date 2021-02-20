package ar.edu.itba.paw.webapp.dto.authentication;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class PasswordRecoveryDto
{
    @NotBlank
    @Email
    private String email;

    public String getEmail() {
        return email;
    }
}
