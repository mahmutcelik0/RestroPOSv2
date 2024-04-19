package com.restropos.systemorder.dto;

import com.restropos.systemmenu.dto.ProductDto;
import com.restropos.systemmenu.dto.ProductModifierDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductDto {
    private String id;
    private ProductDto product;
    private Long quantity;
    private ProductModifierDto productModifiers;
    private Long calculatedPrice;
}
