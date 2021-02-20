package ar.edu.itba.paw.webapp.dto.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target( {TYPE, ANNOTATION_TYPE} )
@Retention( RUNTIME )
@Constraint( validatedBy = {TripValidator.class} )
public @interface TripConstraint
{

    String message() default "Invalid value on trip fields";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
