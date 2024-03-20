package com.restropos.systemmenu.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSubmodifierDto {
    private Long id;
    private String productSubmodifierName;
    private Double price;
}
