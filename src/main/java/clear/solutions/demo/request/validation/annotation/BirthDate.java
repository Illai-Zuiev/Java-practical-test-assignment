package clear.solutions.demo.request.validation.annotation;

import clear.solutions.demo.request.validation.validator.BirthDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BirthDateValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BirthDate {
    String message() default "Age must be bigger than %s";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
