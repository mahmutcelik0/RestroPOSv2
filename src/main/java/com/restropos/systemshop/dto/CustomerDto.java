package com.restropos.systemshop.dto;

import com.restropos.systemcore.constants.CustomResponseMessage;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerDto {
    @NotEmpty(message = CustomResponseMessage.FIRST_NAME_REQUIRED)
    private String firstName;
    @NotEmpty(message = CustomResponseMessage.LAST_NAME_REQUIRED)
    private String lastName;
    private byte[] profilePhoto;
    @NotEmpty(message = CustomResponseMessage.PHONE_NUMBER_REQUIRED)
    private String phoneNumber;
}
