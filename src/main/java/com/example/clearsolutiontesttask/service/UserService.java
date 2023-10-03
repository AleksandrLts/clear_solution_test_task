package com.example.clearsolutiontesttask.service;

import com.example.clearsolutiontesttask.model.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User add(User user);

    User update(User user);

    void delete(Long id);

    List<User> getUsersByBirthDate(LocalDate from, LocalDate to);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findById(Long id);
}
