package com.restropos.systemmenu.populator;

import com.restropos.systemcore.populator.AbstractPopulator;
import com.restropos.systemmenu.dto.ProductDto;
import com.restropos.systemmenu.entity.Product;
import com.restropos.systemshop.populator.ImageDtoPopulator;
import com.restropos.systemshop.populator.WorkspaceDtoPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductDtoPopulator extends AbstractPopulator<Product, ProductDto> {
    @Autowired
    private WorkspaceDtoPopulator workspaceDtoPopulator;

    @Autowired
    private ImageDtoPopulator imageDtoPopulator;

    @Autowired
    private ProductModifierDtoPopulator productModifierDtoPopulator;

    @Override
    protected ProductDto populate(Product product, ProductDto productDto) {
        return ProductDto.builder()
                .productName(product.getProductName())
                .workspace(workspaceDtoPopulator.populate(product.getWorkspace()))
                .productModifiers(productModifierDtoPopulator.populateAll(product.getProductModifiers())) //değişecek
                .productDescription(product.getProductDescription())
                .image(imageDtoPopulator.populate(product.getImage()))
                .price(product.getPrice())
                .categoryTitle(product.getCategory().getCategoryTitle())
                .build();
    }

    @Override
    public ProductDto generateTarget() {
        return new ProductDto();
    }
}
