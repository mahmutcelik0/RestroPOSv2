package com.restropos.systemshop.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SystemUserDto extends EmailSecuredUserDto{
    @NotEmpty(message = "First name can't be null")
    private String firstName;
    @NotEmpty(message = "Last name can't be null")
    private String lastName;
}
