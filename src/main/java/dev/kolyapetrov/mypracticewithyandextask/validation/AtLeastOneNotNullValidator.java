package dev.kolyapetrov.mypracticewithyandextask.validation;

import dev.kolyapetrov.mypracticewithyandextask.entity.Citizen;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class AtLeastOneNotNullValidator
        implements ConstraintValidator<AtLeastOneNotNull, Citizen> {
    @Override
    public boolean isValid(Citizen citizen,
                           ConstraintValidatorContext context) {
        try {
            Field field = citizen.getClass().getDeclaredField("citizen_id");
            field.setAccessible(true);
            if (field.get(citizen) != null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Citizen_id must be not in Json")
                        .addConstraintViolation();
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Field[] fields = citizen.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.get(citizen) != null) {
                    return true;
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}
