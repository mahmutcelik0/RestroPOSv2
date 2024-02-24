package com.restropos.systemshop.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerDto {
    @NotEmpty(message = "First name can't be null")
    private String firstName;
    @NotEmpty(message = "Last name can't be null")
    private String lastName;
    private byte[] profilePhoto;
    @NotEmpty(message = "Phone number can't be null")
    private String phoneNumber;
}
