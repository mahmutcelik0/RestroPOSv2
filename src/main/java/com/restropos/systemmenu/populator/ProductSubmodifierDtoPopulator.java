package com.restropos.systemmenu.populator;

import com.restropos.systemcore.populator.AbstractPopulator;
import com.restropos.systemmenu.dto.ProductSubmodifierDto;
import com.restropos.systemmenu.entity.ProductSubmodifier;
import org.springframework.stereotype.Component;

@Component
public class ProductSubmodifierDtoPopulator extends AbstractPopulator<ProductSubmodifier, ProductSubmodifierDto> {
    @Override
    protected ProductSubmodifierDto populate(ProductSubmodifier productSubmodifier, ProductSubmodifierDto productSubmodifierDto) {
        productSubmodifierDto.setId(productSubmodifier.getId());
        productSubmodifierDto.setPrice(productSubmodifier.getPrice());
        productSubmodifierDto.setProductSubmodifierName(productSubmodifier.getProductSubmodifierName());
        return productSubmodifierDto;
    }

    @Override
    public ProductSubmodifierDto generateTarget() {
        return new ProductSubmodifierDto();
    }
}
