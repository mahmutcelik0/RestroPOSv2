package com.restropos.systemshop.dto;

import com.restropos.systemcore.constants.CustomResponseMessage;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class SystemUserDto extends EmailSecuredUserDto{
    @NotEmpty(message = CustomResponseMessage.FIRST_NAME_REQUIRED)
    private String firstName;
    @NotEmpty(message = CustomResponseMessage.LAST_NAME_REQUIRED)
    private String lastName;
    private WorkspaceDto workspaceDto;
    @NotEmpty(message = CustomResponseMessage.ROLE_DOES_NOT_EXIST)
    private String role;
}
