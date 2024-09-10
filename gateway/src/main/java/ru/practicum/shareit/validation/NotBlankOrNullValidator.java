package ru.practicum.shareit.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotBlankOrNullValidator implements ConstraintValidator<NotBlankOrNull, String> {
    @Override
    public void initialize(NotBlankOrNull constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String field, ConstraintValidatorContext constraintValidatorContext) {
        if (field == null) return true;
        return !field.isBlank();
    }
}
