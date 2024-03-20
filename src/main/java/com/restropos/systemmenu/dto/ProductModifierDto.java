package com.restropos.systemmenu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductModifierDto {
    private Long id;
    private String productModifierName;
    private List<ProductSubmodifierDto> productSubmodifierSet;
    private Boolean isRequired;
    private String choice;
}
