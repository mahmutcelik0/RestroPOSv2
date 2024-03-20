package com.restropos.systemmenu.populator;

import com.restropos.systemcore.populator.AbstractPopulator;
import com.restropos.systemmenu.dto.ProductModifierDto;
import com.restropos.systemmenu.entity.ProductModifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductModifierDtoPopulator extends AbstractPopulator<ProductModifier, ProductModifierDto> {
    @Autowired
    private ProductSubmodifierDtoPopulator productSubmodifierDtoPopulator;

    @Override
    protected ProductModifierDto populate(ProductModifier productModifier, ProductModifierDto productModifierDto) {
        productModifierDto.setId(productModifier.getId());
        productModifierDto.setProductModifierName(productModifier.getProductModifierName());
        productModifierDto.setIsRequired(productModifier.isRequired());
        productModifierDto.setChoice(String.valueOf(productModifier.getChoice()));
        productModifierDto.setProductSubmodifierSet(productSubmodifierDtoPopulator.populateAll(productModifier.getProductSubmodifierSet().stream().toList()));

        return productModifierDto;
    }

    @Override
    public ProductModifierDto generateTarget() {
        return new ProductModifierDto();
    }
}
