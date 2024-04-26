package com.restropos.systemorder.populator;

import com.restropos.systemcore.populator.AbstractPopulator;
import com.restropos.systemmenu.populator.WorkspaceTableDtoPopulator;
import com.restropos.systemorder.dto.OrderDto;
import com.restropos.systemorder.entity.Order;
import com.restropos.systemshop.populator.CustomerDtoPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderDtoPopulator extends AbstractPopulator<Order,OrderDto> {
    @Autowired
    private OrderProductDtoPopulator orderProductDtoPopulator;

    @Autowired
    private WorkspaceTableDtoPopulator workspaceTableDtoPopulator;

    @Autowired
    private CustomerDtoPopulator customerDtoPopulator;
    @Override
    protected OrderDto populate(Order order, OrderDto orderDto) {
        orderDto.setId(String.valueOf(order.getId())); //todo değişecek
        orderDto.setOrderStatus(order.getOrderStatus());
        orderDto.setTotalOrderPrice(order.getTotalOrderPrice());
        orderDto.setOrderProducts(orderProductDtoPopulator.populateAll(order.getOrderProducts()));
        orderDto.setOrderCreationTime(order.getOrderCreationTime());
        orderDto.setWorkspaceTableDto(workspaceTableDtoPopulator.populate(order.getWorkspaceTable()));
        orderDto.setCustomerDto(customerDtoPopulator.populate(order.getCustomer()));
        return orderDto;
    }

    @Override
    public OrderDto generateTarget() {
        return new OrderDto();
    }
}
