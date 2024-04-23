package com.restropos.systemorder.populator;

import com.restropos.systemcore.populator.AbstractPopulator;
import com.restropos.systemmenu.dto.ProductSelectedModifierDto;
import com.restropos.systemmenu.entity.ProductModifier;
import org.springframework.stereotype.Component;

@Component
public class ProductSelectedModifierDtoPopulator extends AbstractPopulator<ProductModifier,ProductSelectedModifierDto> {
    @Override
    protected ProductSelectedModifierDto populate(ProductModifier productModifier, ProductSelectedModifierDto productSelectedModifierDto) {
        return null;
    }

    @Override
    public ProductSelectedModifierDto generateTarget() {
        return null;
    }
}
