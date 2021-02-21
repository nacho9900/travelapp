package ar.edu.itba.paw.webapp.dto.authentication;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class VerificationDto
{
    @NotNull
    private UUID token;

    public UUID getToken() {
        return token;
    }
}
