package ar.edu.itba.paw.webapp.dto.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StringEnumValidator implements ConstraintValidator<StringEnumConstraint, String>
{
    private Set<String> ENUM_NAMES;

    public static Set<String> getNamesSet(Class<? extends Enum<?>> e) {
        Enum<?>[] enums = e.getEnumConstants();
        String[] names = new String[enums.length];
        for (int i = 0; i < enums.length; i++) {
            names[i] = enums[i].name();
        }
        Set<String> mySet = new HashSet<String>( Arrays.asList( names));
        return mySet;
    }

    @Override
    public void initialize(StringEnumConstraint stringEnumConstraint) {
        Class<? extends Enum<?>> enumSelected = stringEnumConstraint.enumClass();
        ENUM_NAMES = getNamesSet( enumSelected);
    }

    @Override
    public boolean isValid( String value, ConstraintValidatorContext context ) {
        if ( value == null ) {
            return true;
        } else {
            return ENUM_NAMES.contains( value);
        }
    }
}
