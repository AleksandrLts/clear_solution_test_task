package com.example.clearsolutiontesttask.dto.request;

import com.example.clearsolutiontesttask.lib.ValidBirthDate;
import com.example.clearsolutiontesttask.lib.ValidPhoneNumber;
import java.time.LocalDate;
import lombok.Data;

@Data
public class UserPatchRequestDto {
    private String firstName;
    private String lastName;
    @ValidBirthDate
    private LocalDate birthDate;
    private String address;
    @ValidPhoneNumber
    private String phoneNumber;
}
