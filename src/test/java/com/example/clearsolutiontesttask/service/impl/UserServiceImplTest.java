package com.example.clearsolutiontesttask.service.impl;




import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.example.clearsolutiontesttask.model.User;
import com.example.clearsolutiontesttask.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private List<User> users = new ArrayList<>();
    private static final String FIRST_NAME_1 = "Bob";
    private static final String LAST_NAME_1 = "Bobson";
    private static final LocalDate BIRTH_DATE_1 = LocalDate.of(1995, 5, 10);
    private static final String ADDRESS_1 = "Address1";
    private static final String PHONE_NUMBER_1 = "+380670000000";

    private static final String FIRST_NAME_2 = "Alice";
    private static final String LAST_NAME_2 = "Alison";
    private static final LocalDate BIRTH_DATE_2 = LocalDate.of(1993, 8, 15);
    private static final String ADDRESS_2 = "Address1";
    private static final String PHONE_NUMBER_2 = "+380950000000";

    @BeforeEach
    void setUp() {
        User firstUser = createUser(FIRST_NAME_1, LAST_NAME_1, BIRTH_DATE_1, ADDRESS_1, PHONE_NUMBER_1);
        User secondUser = createUser(FIRST_NAME_2, LAST_NAME_2, BIRTH_DATE_2, ADDRESS_2, PHONE_NUMBER_2);
        users = List.of(firstUser, secondUser);
    }

    @Test
    void testGetUsersByBirthDate() {
        LocalDate from = LocalDate.of(1990, 1, 1);
        LocalDate to = LocalDate.of(2000, 12, 31);

        when(userRepository.getUserByBirthDateBetween(from, to)).thenReturn(users);

        List<User> result = userService.getUsersByBirthDate(from, to);

        assertEquals(2, result.size());
        assertEquals("Bob", result.get(0).getFirstName());
        assertEquals("Alison", result.get(1).getLastName());

        verify(userRepository, times(1)).getUserByBirthDateBetween(from, to);
    }

    @Test
    void testInvalidDateRange() {
        LocalDate from = LocalDate.of(2000, 1, 1);
        LocalDate to = LocalDate.of(1990, 12, 31);

        assertThrows(RuntimeException.class, () -> userService.getUsersByBirthDate(from, to));

        verify(userRepository, never()).getUserByBirthDateBetween(from, to);
    }

    private User createUser(String firstName, String lastName, LocalDate birthDate, String address, String phoneNumber) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setBirthDate(birthDate);
        user.setAddress(address);
        user.setPhoneNumber(phoneNumber);
        return user;
    }
}