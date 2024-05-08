package com.restropos.systemorder.api;

import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.exception.WrongCredentialsException;
import com.restropos.systemcore.model.ResponseMessage;
import com.restropos.systemorder.dto.OrderDto;
import com.restropos.systemorder.dto.ReviewDto;
import com.restropos.systemorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderApi  implements WebMvcConfigurer{
    @Autowired
    private OrderService orderService;

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

    @PostMapping("/kitchen/cancel")
    public ResponseEntity<ResponseMessage> kitchenCancelOrder(@RequestParam String orderId) throws NotFoundException {
        return orderService.kitchenCancelOrder(orderId);
    }

    @PostMapping("/waiter/serve")
    public ResponseEntity<ResponseMessage> waiterServeOrder(@RequestParam String orderId) throws NotFoundException {
        return orderService.waiterServeOrder(orderId);
    }

    @PostMapping("/cashDesk/pay/{orderId}")
    public ResponseEntity<ResponseMessage> cashDeskPayById(@PathVariable String orderId) throws NotFoundException {
        return orderService.cashDeskPay(orderId);
    }

    @PostMapping("/cashDesk/pay")
    public  ResponseEntity<ResponseMessage> cashDeskPayAll(@RequestBody List<String> orderIdList) throws NotFoundException {
        return orderService.cashDeskPayAll(orderIdList);
    }

    @GetMapping("/{customerInfo}/{businessDomain}")
    public List<OrderDto> getCustomerOrders(@PathVariable String customerInfo,@PathVariable String businessDomain) {
        return orderService.getCustomerOrders(customerInfo,businessDomain);
    }

    @GetMapping("/admin")
    public List<OrderDto> getBusinessOrders() throws NotFoundException {
        return orderService.getBusinessOrders();
    }

    @PutMapping("/review")
    public ResponseEntity<String> reviewOrder(@RequestBody ReviewDto reviewDto) throws NotFoundException, WrongCredentialsException {
        return orderService.reviewOrder(reviewDto);
    }
}
