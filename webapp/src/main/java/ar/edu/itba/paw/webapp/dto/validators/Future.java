package ar.edu.itba.paw.webapp.dto.validators;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint( validatedBy = {FutureLocalDateValidator.class, })
public @interface Future
{
    String message() default "date must be in the future";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
