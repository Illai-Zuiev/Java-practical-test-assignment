package clear.solutions.demo.request.validation.validator;

import clear.solutions.demo.request.validation.annotation.BirthDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.Optional;

public class BirthDateValidator implements ConstraintValidator<BirthDate, LocalDate> {

    @Value("${user.age.restriction}")
    private int ageRestriction;

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return Optional.ofNullable(value)
                .map(date -> {
                    formatMessage(context);
                    return date.plusYears(ageRestriction).isBefore(LocalDate.now());
                })
                .orElse(false);
    }

    private void formatMessage(ConstraintValidatorContext context) {
        String msg = context.getDefaultConstraintMessageTemplate();
        String formattedMsg = String.format(msg, this.ageRestriction);
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(formattedMsg).addConstraintViolation();
    }
}