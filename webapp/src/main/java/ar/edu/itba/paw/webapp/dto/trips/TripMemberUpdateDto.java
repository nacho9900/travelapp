package ar.edu.itba.paw.webapp.dto.trips;

import ar.edu.itba.paw.model.TripMemberRole;
import ar.edu.itba.paw.webapp.dto.validators.StringEnumConstraint;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class TripMemberUpdateDto
{
    @NotNull
    private Long id;
    @NotBlank
    @StringEnumConstraint( enumClass = TripMemberRole.class )
    private String role;

    public Long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }
}
