package com.example.clearsolutiontesttask.lib;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

@ExtendWith(MockitoExtension.class)
class BirthDateValidatorTest {
    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @InjectMocks
    private BirthDateValidator birthDateValidator;

    @Value("${user.min.age}")
    private int validAge;
    private static final LocalDate NULL_DATE = null;

    @Test
    void testValidBirthDate() {
        LocalDate birthDate = LocalDate.now().minusYears(validAge);

        boolean isValid = birthDateValidator.isValid(birthDate, constraintValidatorContext);

        assertTrue(isValid);
    }

    @Test
    void testInvalidBirthDate() {
        LocalDate birthDate = LocalDate.now().minusYears(validAge).plusDays(1);

        boolean isValid = birthDateValidator.isValid(birthDate, constraintValidatorContext);

        assertFalse(isValid);
    }

    @Test
    void testNullBirthDate() {
        boolean isValid = birthDateValidator.isValid(NULL_DATE, constraintValidatorContext);

        assertTrue(isValid);
    }
}