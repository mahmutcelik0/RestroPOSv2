package com.restropos.systemshop.dto;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SystemUserDto extends EmailSecuredUserDto{
    private String firstName;
    private String lastName;
}
