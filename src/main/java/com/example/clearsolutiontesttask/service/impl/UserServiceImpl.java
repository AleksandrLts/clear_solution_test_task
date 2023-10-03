package com.example.clearsolutiontesttask.service.impl;

import com.example.clearsolutiontesttask.exception.DateRangeException;
import com.example.clearsolutiontesttask.service.UserService;
import com.example.clearsolutiontesttask.model.User;
import com.example.clearsolutiontesttask.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User add(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getUsersByBirthDate(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new DateRangeException("Invalid range of birth dates");
        }
        return userRepository.getUserByBirthDateBetween(from, to);
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return userRepository.getUserByPhoneNumber(phoneNumber);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.getUserById(id);
    }
}
