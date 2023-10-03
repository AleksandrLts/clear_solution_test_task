package com.example.clearsolutiontesttask.lib;

import com.example.clearsolutiontesttask.model.User;
import com.example.clearsolutiontesttask.service.UserService;
import java.util.Optional;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {
    private static final String PHONE_NUMBER_PATTERN = "\\+380\\d{9}";
    private final UserService userService;

    @Override
    public boolean isValid(String phoneNumber,
                           ConstraintValidatorContext context) {
        if (phoneNumber == null) {
            return true;
        }
        Optional<User> userOptional = userService.findByPhoneNumber(phoneNumber);
        if (userOptional.isPresent()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("This phone number already exists")
                    .addConstraintViolation();
            return false;
        }
        return phoneNumber.matches(PHONE_NUMBER_PATTERN);
    }
}
