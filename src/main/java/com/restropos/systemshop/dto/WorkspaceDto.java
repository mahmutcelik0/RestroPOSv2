package com.restropos.systemshop.dto;

import com.restropos.systemcore.constants.CustomResponseMessage;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "[A-Za-z0-9](?:[A-Za-z0-9\\-]{0,61}[A-Za-z0-9])?",message = CustomResponseMessage.BUSINESS_DOMAIN_PATTERN)
    private String businessDomain;
    private byte[] businessLogo;
}
