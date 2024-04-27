package com.restropos.systemorder.api;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.model.ResponseMessage;
import com.restropos.systemcore.security.SecurityProvideService;
import com.restropos.systemcore.utils.LogUtil;
import com.restropos.systemorder.dto.OrderDto;
import com.restropos.systemorder.entity.Order;
import com.restropos.systemorder.populator.OrderDtoPopulator;
import com.restropos.systemorder.service.FirebaseService;
import com.restropos.systemorder.service.OrderService;
import com.restropos.systemshop.entity.Workspace;
import com.restropos.systemshop.entity.user.SystemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderApi  implements WebMvcConfigurer{
    @Autowired
    private OrderService orderService;

    @Autowired
    private FirebaseService firebaseService;


    @PostMapping
    public ResponseEntity<ResponseMessage> createOrder(@RequestBody OrderDto orderDto) throws NotFoundException {
        return orderService.createOrder(orderDto);
    }

    @PostMapping("/waiter/take")
    public ResponseEntity<ResponseMessage> waiterTakeOrder(@RequestParam String orderId) throws NotFoundException {
        return orderService.waiterTakeOrder(orderId);
    }

    @PostMapping("/kitchen/take")
    public ResponseEntity<ResponseMessage> kitchenTakeOrder(@RequestParam String orderId) throws NotFoundException {
        return orderService.kitchenTakeOrder(orderId);
    }

    @DeleteMapping
    public void deleteOrder(@RequestBody OrderDto orderDto){
        firebaseService.delete(orderService.getOrder(1L));
    }


}
