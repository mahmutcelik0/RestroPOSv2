package com.restropos.systemcore.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@Builder
public class BearerToken{
    @NotEmpty
    private String accessToken;
    @NotEmpty
    private String tokenType;
}
