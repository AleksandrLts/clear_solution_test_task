package com.example.clearsolutiontesttask.dto.request;

import com.example.clearsolutiontesttask.lib.ValidBirthDate;
import com.example.clearsolutiontesttask.lib.ValidPhoneNumber;
import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequestDto {
    @NotBlank(message = "First name can't be null or blank")
    private String firstName;
    @NotBlank(message = "Last name can't be null or blank")
    private String lastName;
    @NotNull(message = "Birth date can't be null")
    @ValidBirthDate
    private LocalDate birthDate;
    private String address;
    @ValidPhoneNumber
    private String phoneNumber;
}
