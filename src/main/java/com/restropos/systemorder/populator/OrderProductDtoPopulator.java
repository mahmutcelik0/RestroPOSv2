package com.restropos.systemorder.populator;

import com.restropos.systemcore.populator.AbstractPopulator;
import com.restropos.systemmenu.dto.ProductSelectedModifierDto;
import com.restropos.systemmenu.dto.SelectionDto;
import com.restropos.systemmenu.populator.ProductDtoPopulator;
import com.restropos.systemorder.dto.OrderProductDto;
import com.restropos.systemorder.entity.OrderProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
        if(!CollectionUtils.isEmpty(orderProduct.getProductModifiers())){
            orderProduct.getProductModifiers().forEach(e -> {
                ProductSelectedModifierDto productSelectedModifierDto = new ProductSelectedModifierDto();
                productSelectedModifierDto.setName(e.getProductModifierName());
                productSelectedModifierDto.setId(e.getId());
                if(!CollectionUtils.isEmpty(orderProduct.getProductSubmodifiers())){
                    List<SelectionDto> selectionDtoList = orderProduct.getProductSubmodifiers().stream().map(x -> {
                        e.getProductSubmodifierSet().stream().forEach(m -> {
                            if(m.getProductSubmodifierName().equalsIgnoreCase(x.getProductSubmodifierName())){

                            }
                        });
                        SelectionDto selectionDto = new SelectionDto();
                        selectionDto.setLabel(x.getProductSubmodifierName());
                        selectionDto.setValue(x.getPrice());
                        return selectionDto;
                    }).toList();
                    productSelectedModifierDto.setSelections(selectionDtoList);
                }
                productSelectedModifierDtos.add(productSelectedModifierDto);
            });
        }
        orderProductDto.setProductSelectedModifiers(productSelectedModifierDtos);
        return orderProductDto;
    }

    @Override
    public OrderProductDto generateTarget() {
        return new OrderProductDto();
    }
}
