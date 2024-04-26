package com.restropos.systemorder.populator;

import com.restropos.systemcore.populator.AbstractPopulator;
import com.restropos.systemmenu.dto.ProductSelectedModifierDto;
import com.restropos.systemmenu.dto.SelectionDto;
import com.restropos.systemmenu.populator.ProductDtoPopulator;
import com.restropos.systemorder.dto.OrderProductDto;
import com.restropos.systemorder.entity.OrderProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderProductDtoPopulator extends AbstractPopulator<OrderProduct, OrderProductDto> {
    @Autowired
    private ProductDtoPopulator productDtoPopulator;

    @Override
    protected OrderProductDto populate(OrderProduct orderProduct, OrderProductDto orderProductDto) {
        orderProductDto.setId(orderProduct.getId());
        orderProductDto.setProduct(productDtoPopulator.populate(orderProduct.getProduct()));
        orderProductDto.setQuantity(orderProduct.getQuantity());
        orderProductDto.setCalculatedPrice(orderProduct.getCalculatedPrice());
        List<ProductSelectedModifierDto> productSelectedModifierDtos = new ArrayList<>();
        orderProduct.getOrderSelectedModifiers().forEach(e->{
            ProductSelectedModifierDto productSelectedModifierDto = new ProductSelectedModifierDto();
            productSelectedModifierDto.setId(e.getProductModifier().getId());
            productSelectedModifierDto.setName(e.getProductModifier().getProductModifierName());
            List<SelectionDto> selections = e.getProductSubmodifiers().stream().map(x->{
                SelectionDto selectionDto = new SelectionDto();
                selectionDto.setLabel(x.getProductSubmodifierName());
                selectionDto.setValue(Double.valueOf(x.getId()));
                return selectionDto;
            }).toList();
            productSelectedModifierDto.setSelections(selections);
            productSelectedModifierDtos.add(productSelectedModifierDto);
        });

        orderProductDto.setProductSelectedModifiers(productSelectedModifierDtos);
        return orderProductDto;
    }

    @Override
    public OrderProductDto generateTarget() {
        return new OrderProductDto();
    }
}
