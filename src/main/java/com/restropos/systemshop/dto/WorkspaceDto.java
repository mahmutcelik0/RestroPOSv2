package com.restropos.systemshop.dto;

import com.restropos.systemcore.constants.CustomResponseMessage;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceDto {
    @NotEmpty(message = CustomResponseMessage.BUSINESS_NAME_REQUIRED)
    private String businessName;
    @NotEmpty(message = CustomResponseMessage.BUSINESS_DOMAIN_REQUIRED)
    private String businessDomain;
    private byte[] businessLogo;
}
