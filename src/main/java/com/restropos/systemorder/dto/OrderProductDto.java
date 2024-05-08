package com.restropos.systemorder.dto;

import com.restropos.systemmenu.dto.ProductDto;
import com.restropos.systemmenu.dto.ProductSelectedModifierDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductDto {
    private Long id;
    private ProductDto product;
    private Long quantity;
    private List<ProductSelectedModifierDto> productSelectedModifiers;
    private Long calculatedPrice;
    private Integer orderProductReviewStar;
}
