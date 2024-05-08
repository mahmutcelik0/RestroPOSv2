package com.restropos.systemorder.populator;

import com.restropos.systemcore.populator.AbstractPopulator;
import com.restropos.systemmenu.populator.WorkspaceTableDtoPopulator;
import com.restropos.systemorder.dto.OrderDto;
import com.restropos.systemorder.entity.Order;
import com.restropos.systemshop.populator.CustomerDtoPopulator;
import com.restropos.systemshop.populator.SystemUserDtoPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class OrderDtoPopulator extends AbstractPopulator<Order,OrderDto> {
    @Autowired
    private OrderProductDtoPopulator orderProductDtoPopulator;

    @Autowired
    private WorkspaceTableDtoPopulator workspaceTableDtoPopulator;

    @Autowired
    private CustomerDtoPopulator customerDtoPopulator;

    @Autowired
    private SystemUserDtoPopulator systemUserDtoPopulator;

    @Override
    protected OrderDto populate(Order order, OrderDto orderDto) {
        orderDto.setId(String.valueOf(order.getId()));
        orderDto.setOrderStatus(order.getOrderStatus());
        orderDto.setTotalOrderPrice(order.getTotalOrderPrice());
        orderDto.setOrderProducts(orderProductDtoPopulator.populateAll(order.getOrderProducts()));
        orderDto.setOrderCreationTime(order.getOrderCreationTime());
        orderDto.setWorkspaceTableDto(workspaceTableDtoPopulator.populate(order.getWorkspaceTable()));
        orderDto.setCustomerDto(customerDtoPopulator.populate(order.getCustomer()));
        if(!ObjectUtils.isEmpty(order.getWaiter())){
            orderDto.setWaiterDto(systemUserDtoPopulator.populate(order.getWaiter()));
        }
        if(!ObjectUtils.isEmpty(order.getKitchen())){
            orderDto.setKitchenDto(systemUserDtoPopulator.populate(order.getKitchen()));
        }
        if(!ObjectUtils.isEmpty(order.getCashDesk())){
            orderDto.setCashDeskDto(systemUserDtoPopulator.populate(order.getCashDesk()));
        }
        if(!ObjectUtils.isEmpty(order.getReviewComment())){
            orderDto.setOrderReviewComment(order.getReviewComment());
        }
        if(!ObjectUtils.isEmpty(order.getReviewStar())){
            orderDto.setOrderReviewStar(order.getReviewStar().getNumber());
        }

        return orderDto;
    }

    @Override
    public OrderDto generateTarget() {
        return new OrderDto();
    }
}
