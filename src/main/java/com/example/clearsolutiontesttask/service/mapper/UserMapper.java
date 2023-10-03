package com.example.clearsolutiontesttask.service.mapper;

import com.example.clearsolutiontesttask.dto.request.UserRequestDto;
import com.example.clearsolutiontesttask.dto.response.UserResponseDto;
import com.example.clearsolutiontesttask.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements DtoMapper<UserRequestDto, UserResponseDto, User> {
    @Override
    public User mapToModel(UserRequestDto dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setBirthDate(dto.getBirthDate());
        user.setAddress(dto.getAddress());
        user.setPhoneNumber(dto.getPhoneNumber());
        return user;
    }

    @Override
    public UserResponseDto mapToDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setFirstName(user.getFirstName());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setBirthDate(user.getBirthDate());
        userResponseDto.setAddress(user.getAddress());
        userResponseDto.setPhoneNumber(user.getPhoneNumber());
        return userResponseDto;
    }
}
