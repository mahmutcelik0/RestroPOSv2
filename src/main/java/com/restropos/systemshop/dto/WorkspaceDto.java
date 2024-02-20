package com.restropos.systemshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceDto {
    private String businessName;
    private String businessDomain;
    private byte[] businessLogo;
}
