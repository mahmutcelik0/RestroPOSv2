package com.restropos.systemshop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmailSecuredUserDto {
    @NotEmpty(message = "Email can't be null")
    @Email(message = "Content must be in email format")
    private String email;
    @NotEmpty(message = "Password can't be null")
    @Size(min = 8,message = "Password must be min 8 length")
    private String password;

    public EmailSecuredUserDto(String email) {
        this.email = email;
    }
}
