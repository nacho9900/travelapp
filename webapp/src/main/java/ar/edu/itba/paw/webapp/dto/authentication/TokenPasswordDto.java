package ar.edu.itba.paw.webapp.dto.authentication;

import ar.edu.itba.paw.webapp.dto.validators.PasswordConstraint;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class TokenPasswordDto
{
    @NotNull
    private UUID token;
    @NotBlank
    @PasswordConstraint
    private String password;

    public UUID getToken() {
        return token;
    }

    public String getPassword() {
        return password;
    }
}
