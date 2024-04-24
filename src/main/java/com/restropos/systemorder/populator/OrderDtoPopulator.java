package com.restropos.systemorder.populator;

import com.restropos.systemcore.populator.AbstractPopulator;
import com.restropos.systemorder.dto.OrderDto;
import com.restropos.systemorder.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderDtoPopulator extends AbstractPopulator<Order,OrderDto> {
    @Autowired
    private OrderProductDtoPopulator orderProductDtoPopulator;

    @Override
    protected OrderDto populate(Order order, OrderDto orderDto) {
        orderDto.setId(String.valueOf(order.getId())); //todo değişecek
        orderDto.setOrderStatus(order.getOrderStatus());
        orderDto.setTotalOrderPrice(order.getTotalOrderPrice());
        orderDto.setOrderProducts(orderProductDtoPopulator.populateAll(order.getOrderProducts()));
        orderDto.setOrderCreationTime(order.getOrderCreationTime());
        return orderDto;
    }

    @Override
    public OrderDto generateTarget() {
        return new OrderDto();
    }
}
