package com.restropos.systemshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmailSecuredUserDto {
    private String email;
    private String password;

    public EmailSecuredUserDto(String email) {
        this.email = email;
    }
}
