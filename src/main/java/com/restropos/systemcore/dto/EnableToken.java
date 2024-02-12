package com.restropos.systemcore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnableToken {
    private String tokenCode;
    private String accountInformation;

}
