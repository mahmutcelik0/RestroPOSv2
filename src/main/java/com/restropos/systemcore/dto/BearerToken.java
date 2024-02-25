package com.restropos.systemcore.dto;

import com.restropos.systemcore.constants.CustomResponseMessage;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@Builder
public class BearerToken{
    @NotEmpty(message = CustomResponseMessage.ACCESS_TOKEN_REQUIRED)
    private String accessToken;
    @NotEmpty(message = CustomResponseMessage.TOKEN_TYPE_REQUIRED)
    private String tokenType;
}
