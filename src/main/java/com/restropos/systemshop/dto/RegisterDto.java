package com.restropos.systemshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    private SystemUserDto systemUser;
    private WorkspaceDto workspace;
}
