package com.restropos.systemshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SystemUserDtoResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private WorkspaceDto workspaceDto;
    private String role;
    private String email;
}
