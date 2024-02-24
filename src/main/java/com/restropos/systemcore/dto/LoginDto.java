package com.restropos.systemcore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    @NotEmpty(message = "Email can't be null")
    @Email(message = "Content is not email")
    private String email;
    @NotEmpty(message = "Password can't be null")
    private String password;
}
