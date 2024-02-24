package com.restropos.systemcore.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnableToken {
    @NotEmpty(message = "Token code can't be null")
    @Size(min = 6,max = 6, message = "Token size must be 6 character")
    private String tokenCode;
    @NotEmpty(message = "Account information can't be null")
    private String accountInformation;

}
