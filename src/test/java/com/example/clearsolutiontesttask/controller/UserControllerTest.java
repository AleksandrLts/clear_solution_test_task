package com.example.clearsolutiontesttask.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.clearsolutiontesttask.dto.request.UserPatchRequestDto;
import com.example.clearsolutiontesttask.dto.request.UserRequestDto;
import com.example.clearsolutiontesttask.dto.response.UserResponseDto;
import com.example.clearsolutiontesttask.model.User;
import com.example.clearsolutiontesttask.service.UserService;
import com.example.clearsolutiontesttask.service.mapper.DtoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private DtoMapper<UserRequestDto, UserResponseDto, User> userDtoMapper;

    private static final Long ID_1 = 1L;
    private static final String FIRST_NAME_1 = "Bob";
    private static final String LAST_NAME_1 = "Bobson";
    private static final LocalDate BIRTH_DATE_1 = LocalDate.of(1995, 5, 10);
    private static final Long ID_2 = 2L;
    private static final String FIRST_NAME_2 = "Alice";
    private static final String LAST_NAME_2 = "Alison";
    private static final LocalDate BIRTH_DATE_2 = LocalDate.of(1991, 5, 10);
    private static final LocalDate FROM_DATE = LocalDate.of(1990, 1, 1);
    private static final LocalDate TO_DATE =  LocalDate.of(2000, 12, 31);
    private static final String UPDATED_NAME = "John";
    private static final String UPDATED_LAST_NAME = "Johnson";


    @Test
    public void testAddUser() throws Exception {
        UserRequestDto requestDto = createUserRequestDto(FIRST_NAME_1, LAST_NAME_1, BIRTH_DATE_1);
        User userToAdd = createUser(ID_1, FIRST_NAME_1, LAST_NAME_1, BIRTH_DATE_1);
        UserResponseDto responseDto = createUserResponseDto(ID_1, FIRST_NAME_1, LAST_NAME_1, BIRTH_DATE_1);

        when(userDtoMapper.mapToModel(requestDto)).thenReturn(userToAdd);
        when(userService.add(userToAdd)).thenReturn(userToAdd);
        when(userDtoMapper.mapToDto(userToAdd)).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value(responseDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(responseDto.getLastName()));
    }

    @Test
    public void testDeleteUser() throws Exception {
        Long userIdToDelete = ID_1;

        doNothing().when(userService).delete(userIdToDelete);

        mockMvc.perform(delete("/users/{id}", userIdToDelete))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetUsersByBirthDateRange() throws Exception {
        LocalDate fromDate = FROM_DATE;
        LocalDate toDate = TO_DATE;

        User user1 = createUser(ID_1, FIRST_NAME_1, LAST_NAME_1, BIRTH_DATE_1);
        User user2 = createUser(ID_2, FIRST_NAME_2, LAST_NAME_2, BIRTH_DATE_2);
        List<User> users = Arrays.asList(user1, user2);

        UserResponseDto responseDto1 = createUserResponseDto(ID_1, FIRST_NAME_1, LAST_NAME_1, BIRTH_DATE_1);
        UserResponseDto responseDto2 = createUserResponseDto(ID_2, FIRST_NAME_2, LAST_NAME_2, BIRTH_DATE_2);

        when(userService.getUsersByBirthDate(fromDate, toDate)).thenReturn(users);
        when(userDtoMapper.mapToDto(user1)).thenReturn(responseDto1);
        when(userDtoMapper.mapToDto(user2)).thenReturn(responseDto2);

        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .param("from", fromDate.toString())
                        .param("to", toDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[1].id").exists());
    }

    @Test
    public void testPatchUser() throws Exception {
        UserPatchRequestDto patchRequestDto = new UserPatchRequestDto();
        patchRequestDto.setFirstName(UPDATED_NAME);

        User existingUser = createUser(ID_1, FIRST_NAME_1, LAST_NAME_1, BIRTH_DATE_1);
        User updatedUser = createUser(ID_1, UPDATED_NAME, LAST_NAME_1, BIRTH_DATE_1);
        UserResponseDto responseDto = createUserResponseDto(ID_1, UPDATED_NAME, LAST_NAME_1, BIRTH_DATE_1);

        when(userService.findById(ID_1)).thenReturn(Optional.of(existingUser));
        when(userDtoMapper.mapToDto(any(User.class))).thenReturn(responseDto);
        when(userService.update(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/{id}", ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(patchRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseDto.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is(responseDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(responseDto.getLastName())))
                .andExpect(jsonPath("$.birthDate", is(responseDto.getBirthDate().toString())))
                .andReturn();
    }

    @Test
    public void testUpdateUser() throws Exception {
        UserRequestDto requestDto = createUserRequestDto(FIRST_NAME_1, UPDATED_LAST_NAME, BIRTH_DATE_1);
        User updatedUser = createUser(ID_1, FIRST_NAME_1, UPDATED_LAST_NAME, BIRTH_DATE_1);
        UserResponseDto responseDto = createUserResponseDto(ID_1, FIRST_NAME_1, UPDATED_LAST_NAME, BIRTH_DATE_1);

        when(userService.update(any(User.class))).thenReturn(updatedUser);
        when(userDtoMapper.mapToModel(requestDto)).thenReturn(updatedUser);
        when(userDtoMapper.mapToDto(updatedUser)).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseDto.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is(responseDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(responseDto.getLastName())))
                .andExpect(jsonPath("$.birthDate", is(responseDto.getBirthDate().toString())));
    }

    private User createUser(Long id, String firstName, String lastName, LocalDate birthDate) {
        User user = new User();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setBirthDate(birthDate);
        return user;
    }

    private UserResponseDto createUserResponseDto(Long id, String firstName, String lastName, LocalDate birthDate) {
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(id);
        responseDto.setFirstName(firstName);
        responseDto.setLastName(lastName);
        responseDto.setBirthDate(birthDate);
        return responseDto;
    }

    private UserRequestDto createUserRequestDto(String firstName, String lastName, LocalDate birthDate) {
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setFirstName(firstName);
        requestDto.setLastName(lastName);
        requestDto.setBirthDate(birthDate);
        return requestDto;
    }

    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}