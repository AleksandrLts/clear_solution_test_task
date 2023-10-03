package com.example.clearsolutiontesttask.service.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.example.clearsolutiontesttask.dto.request.UserRequestDto;
import com.example.clearsolutiontesttask.dto.response.UserResponseDto;
import com.example.clearsolutiontesttask.model.User;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserMapperTest {

    private UserMapper userMapper;
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final LocalDate BIRTH_DATE = LocalDate.of(1990, 5, 15);
    private static final String ADDRESS = "123 Main St";
    private static final String PHONE_NUMBER = "+1234567890";
    private static final Long ID = 1L;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    void testMapToModel() {
        UserRequestDto userRequestDto = createUserRequestDto();

        User user = userMapper.mapToModel(userRequestDto);

        assertNotNull(user);
        assertEquals(FIRST_NAME, user.getFirstName());
        assertEquals(LAST_NAME, user.getLastName());
        assertEquals(BIRTH_DATE, user.getBirthDate());
        assertEquals(ADDRESS, user.getAddress());
        assertEquals(PHONE_NUMBER, user.getPhoneNumber());
    }

    @Test
    void testMapToDto() {
        User user = createUser();

        UserResponseDto userResponseDto = userMapper.mapToDto(user);

        assertNotNull(userResponseDto);
        assertEquals(user.getId(), userResponseDto.getId());
        assertEquals(FIRST_NAME, userResponseDto.getFirstName());
        assertEquals(LAST_NAME, userResponseDto.getLastName());
        assertEquals(BIRTH_DATE, userResponseDto.getBirthDate());
        assertEquals(ADDRESS, userResponseDto.getAddress());
        assertEquals(PHONE_NUMBER, userResponseDto.getPhoneNumber());
    }

    private UserRequestDto createUserRequestDto() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setFirstName(FIRST_NAME);
        userRequestDto.setLastName(LAST_NAME);
        userRequestDto.setBirthDate(BIRTH_DATE);
        userRequestDto.setAddress(ADDRESS);
        userRequestDto.setPhoneNumber(PHONE_NUMBER);
        return userRequestDto;
    }

    private User createUser() {
        User user = new User();
        user.setId(ID);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setBirthDate(BIRTH_DATE);
        user.setAddress(ADDRESS);
        user.setPhoneNumber(PHONE_NUMBER);
        return user;
    }
}