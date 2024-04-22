package com.restropos.systemorder.service;

import com.restropos.systemorder.dto.OrderDto;
import com.restropos.systemorder.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public void createOrder(OrderDto orderDto) {
        
    }
}
