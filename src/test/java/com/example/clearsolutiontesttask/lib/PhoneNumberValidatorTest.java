package com.example.clearsolutiontesttask.lib;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import com.example.clearsolutiontesttask.model.User;
import com.example.clearsolutiontesttask.service.impl.UserServiceImpl;
import java.util.Optional;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PhoneNumberValidatorTest {
    @Mock
    private UserServiceImpl userService;
    @Mock
    private ConstraintValidatorContext context;
    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;

    private PhoneNumberValidator validator;

    private static final String VALID_PHONE_NUMBER = "+380987654321";
    private static final String INVALID_PHONE_NUMBER = "+380563453";
    private static final String EXISTING_PHONE_NUMBER = "+380123456789";
    private static final String NULL_PHONE_NUMBER = null;

    @BeforeEach
    void setUp() {
        validator = new PhoneNumberValidator(userService);
    }

    @Test
    void testValidPhoneNumber() {
        when(userService.findByPhoneNumber(VALID_PHONE_NUMBER)).thenReturn(Optional.empty());

        boolean isValid = validator.isValid(VALID_PHONE_NUMBER, context);

        assertTrue(isValid);
        verify(userService, times(1)).findByPhoneNumber(VALID_PHONE_NUMBER);
    }

    @Test
    void testPhoneNumberAlreadyExists() {
        when(userService.findByPhoneNumber(EXISTING_PHONE_NUMBER)).thenReturn(Optional.of(new User()));
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);

        boolean isValid = validator.isValid(EXISTING_PHONE_NUMBER, context);

        assertFalse(isValid);
        verify(userService, times(1)).findByPhoneNumber(EXISTING_PHONE_NUMBER);
        verify(context, times(1)).buildConstraintViolationWithTemplate("This phone number already exists");
        verify(builder, times(1)).addConstraintViolation();
    }

    @Test
    void testNullPhoneNumber() {
        boolean isValid = validator.isValid(NULL_PHONE_NUMBER, context);

        assertTrue(isValid);
        verify(userService, never()).findByPhoneNumber(any());
    }

    @Test
    void testInvalidPhoneNumber() {
        when(userService.findByPhoneNumber(INVALID_PHONE_NUMBER)).thenReturn(Optional.empty());

        boolean isValid = validator.isValid(INVALID_PHONE_NUMBER, context);

        assertFalse(isValid);
        verify(userService, times(1)).findByPhoneNumber(INVALID_PHONE_NUMBER);
    }
}