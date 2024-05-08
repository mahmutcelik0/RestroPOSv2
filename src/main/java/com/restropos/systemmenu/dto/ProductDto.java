package com.restropos.systemmenu.dto;

import com.restropos.systemshop.dto.ImageDto;
import com.restropos.systemshop.dto.WorkspaceDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private String productName;
    private WorkspaceDto workspace;
    private List<ProductModifierDto> productModifiers;
    private String productDescription;
    private ImageDto image;
    private Double price;
    private String categoryTitle;
    private Integer totalReviewCount;
    private Double meanOfProductStar;
}
