package com.restropos.systemcore.dto;

import com.restropos.systemcore.constants.CustomResponseMessage;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnableToken {
    @NotEmpty(message = CustomResponseMessage.TOKEN_CODE_REQUIRED)
    @Size(min = 6,max = 6, message = CustomResponseMessage.TOKEN_SIZE)
    private String tokenCode;
    @NotEmpty(message = CustomResponseMessage.ACCOUNT_INFORMATION_REQUIRED)
    private String accountInformation;

}
