package com.restropos.systemshop.dto;

import com.restropos.systemcore.constants.CustomResponseMessage;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BasicUserDto extends EmailSecuredUserDto{
    @NotEmpty(message = CustomResponseMessage.DEVICE_NAME_REQUIRED)
    private String deviceName;
}
