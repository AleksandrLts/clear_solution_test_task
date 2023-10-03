package com.example.clearsolutiontesttask.controller;

import com.example.clearsolutiontesttask.dto.request.UserPatchRequestDto;
import com.example.clearsolutiontesttask.dto.request.UserRequestDto;
import com.example.clearsolutiontesttask.dto.response.UserResponseDto;
import com.example.clearsolutiontesttask.model.User;
import com.example.clearsolutiontesttask.service.UserService;
import com.example.clearsolutiontesttask.service.mapper.DtoMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final DtoMapper<UserRequestDto, UserResponseDto, User> userDtoMapper;

    @PostMapping
    public UserResponseDto add(@RequestBody @Valid UserRequestDto userRequestDto) {
        return userDtoMapper.mapToDto(userService
                .add(userDtoMapper.mapToModel(userRequestDto)));
    }

    @PatchMapping("/{id}")
    public UserResponseDto patch(@PathVariable Long id,
                                      @RequestBody @Valid
                                      UserPatchRequestDto userPatchRequestDto)
            throws IOException {
        User existingUser = userService.findById(id).orElseThrow(
                () -> new NoSuchElementException("There is no exist users by id: " + id)
        );
        User updatedUser = applyPatchToUser(userPatchRequestDto, existingUser);
        return userDtoMapper.mapToDto(userService.update(updatedUser));
    }

    @PutMapping("/{id}")
    public UserResponseDto update(@PathVariable Long id,
                                      @RequestBody @Valid UserRequestDto userRequestDto) {
        User userToUpdate = userDtoMapper.mapToModel(userRequestDto);
        userToUpdate.setId(id);
        return userDtoMapper.mapToDto(userService.update(userToUpdate));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @GetMapping
    public List<UserResponseDto> getUsersByBirthDateRange(@RequestParam LocalDate from,
                                                         @RequestParam LocalDate to) {
        return userService.getUsersByBirthDate(from, to)
                .stream()
                .map(userDtoMapper::mapToDto)
                .toList();
    }

    private User applyPatchToUser(UserPatchRequestDto userPatchRequestDto, User oldUser)
            throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        JsonNode originalObjNode = objectMapper.valueToTree(oldUser);
        JsonNode patchNode = objectMapper.valueToTree(userPatchRequestDto);
        JsonNode mergedNode = mergeJsonNodes(originalObjNode, patchNode);
        return objectMapper.treeToValue(mergedNode, User.class);
    }

    private JsonNode mergeJsonNodes(JsonNode original, JsonNode patch) {
        ObjectNode mergedNode = original.deepCopy();
        Iterator<String> fieldNames = patch.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode newValue = patch.get(fieldName);
            if (!newValue.isNull()) {
                mergedNode.set(fieldName, newValue);
            }
        }
        return mergedNode;
    }
}
