package com.restropos.systemorder.dto;

import com.restropos.systemorder.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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
    private LocalDateTime orderCreationTime;

}