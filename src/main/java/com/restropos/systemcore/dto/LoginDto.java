package com.restropos.systemcore.dto;

import com.restropos.systemcore.constants.CustomResponseMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    @NotEmpty(message = CustomResponseMessage.EMAIL_REQUIRED)
    @Email(message = CustomResponseMessage.CONTENT_MUST_BE_EMAIL)
    private String email;
    @NotEmpty(message = CustomResponseMessage.PASSWORD_REQUIRED)
    private String password;
}
