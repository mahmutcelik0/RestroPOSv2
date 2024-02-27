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
public class SystemUserDto extends EmailSecuredUserDto{
    @NotEmpty(message = CustomResponseMessage.FIRST_NAME_REQUIRED)
    private String firstName;
    @NotEmpty(message = CustomResponseMessage.LAST_NAME_REQUIRED)
    private String lastName;
    private WorkspaceDto workspaceDto;
}
