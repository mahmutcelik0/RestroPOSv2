package com.restropos.systemorder.populator;

import com.restropos.systemcore.populator.AbstractPopulator;
import com.restropos.systemmenu.dto.ProductSelectedModifierDto;
import com.restropos.systemmenu.populator.ProductDtoPopulator;
import com.restropos.systemorder.dto.OrderProductDto;
import com.restropos.systemorder.entity.OrderProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderProductDtoPopulator extends AbstractPopulator<OrderProduct,OrderProductDto> {
    @Autowired
    private ProductDtoPopulator productDtoPopulator;
    @Autowired
    private ProductSelectedModifierDtoPopulator productSelectedModifierDtoPopulator;

    @Override
    protected OrderProductDto populate(OrderProduct orderProduct, OrderProductDto orderProductDto) {
        orderProductDto.setProduct(productDtoPopulator.populate(orderProduct.getProduct()));
        orderProductDto.setQuantity(orderProduct.getQuantity());
        orderProductDto.setCalculatedPrice(orderProduct.getCalculatedPrice());
        ProductSelectedModifierDto productSelectedModifierDto = new ProductSelectedModifierDto();
        orderProductDto.setProductSelectedModifiers(productSelectedModifierDtoPopulator.populateAll(orderProduct.getProductModifiers().stream().toList()));
//        orderProductDto.getProductSelectedModifiers()
        return orderProductDto;
    }

    @Override
    public OrderProductDto generateTarget() {
        return new OrderProductDto();
    }
}
