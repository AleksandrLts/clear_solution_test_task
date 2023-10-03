package com.example.clearsolutiontesttask.repository;

import com.example.clearsolutiontesttask.model.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> getUserByBirthDateBetween(LocalDate from, LocalDate to);

    Optional<User> getUserByPhoneNumber(String phoneNumber);

    Optional<User> getUserById(Long id);
}
