package com.restropos.systemshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerDto {
    private String firstName;
    private String lastName;
    private byte[] profilePhoto;
    private String phoneNumber;
}
