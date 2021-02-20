package ar.edu.itba.paw.webapp.dto.validators;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint( validatedBy = {PastLocalDateValidator.class, PastLocalDateTimeValidator.class} )
public @interface Past
{
    String message() default "date must be in the past";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
