package com.restropos.systemshop.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceDto {
    @NotEmpty(message = "Business name can't be null")
    private String businessName;
    @NotEmpty(message = "Business domain can't be null")
    private String businessDomain;
    private byte[] businessLogo;
}
