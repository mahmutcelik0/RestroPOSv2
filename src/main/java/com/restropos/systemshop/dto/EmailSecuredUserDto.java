package com.restropos.systemshop.dto;

import com.restropos.systemcore.constants.CustomResponseMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class EmailSecuredUserDto {
    @NotEmpty(message = CustomResponseMessage.EMAIL_REQUIRED)
    @Email(message = CustomResponseMessage.CONTENT_MUST_BE_EMAIL)
    private String email;
    @NotEmpty(message = CustomResponseMessage.PASSWORD_REQUIRED)
    @Size(min = 8,message = CustomResponseMessage.PASSWORD_SIZE)
    private String password;
    private String role;

    public EmailSecuredUserDto(String email) {
        this.email = email;
    }
}
