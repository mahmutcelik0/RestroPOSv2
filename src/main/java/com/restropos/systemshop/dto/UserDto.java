package com.restropos.systemshop.dto;

import com.restropos.systemcore.constants.CustomResponseMessage;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto extends SystemUserDto{
    @NotEmpty(message = CustomResponseMessage.DEVICE_NAME_REQUIRED)
    private String deviceName;
    @NotEmpty(message = CustomResponseMessage.ROLE_DOES_NOT_EXIST)
    private String role;
}
