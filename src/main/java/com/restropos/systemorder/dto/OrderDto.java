package com.restropos.systemorder.dto;

import com.restropos.systemmenu.dto.WorkspaceTableDto;
import com.restropos.systemorder.OrderStatus;
import com.restropos.systemshop.dto.CustomerDto;
import com.restropos.systemshop.dto.SystemUserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private String id;
    private List<OrderProductDto> orderProducts;
    private Long totalOrderPrice;
    private OrderStatus orderStatus;
    private Date orderCreationTime;
    private WorkspaceTableDto workspaceTableDto;
    private CustomerDto customerDto;
    private SystemUserDto waiterDto;
}