package com.restropos.systemshop.api;

import com.restropos.systemcore.dto.EnableToken;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.exception.UnauthorizedException;
import com.restropos.systemshop.dto.SystemUserDto;
import com.restropos.systemshop.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UserApi {
    @Autowired
    private UserFacade userFacade;

    @GetMapping
    public ResponseEntity<?> getUser(@RequestParam String businessDomain) throws NotFoundException, UnauthorizedException {
        return userFacade.getUser(businessDomain);
    }



//    @PostMapping("/admin")
//    public ResponseEntity<?> addNewAdmin(@RequestBody @Valid Admin admin) {
//        return userFacade.addNewUser(admin, UserTypes.ADMIN);
//    }
//
//    @PostMapping("/waiter")
//    public ResponseEntity<?> addNewWaiter(@RequestBody @Valid Waiter waiter) {
//        return userFacade.addNewUser(waiter, UserTypes.WAITER);
//    }
//
//
//    @PostMapping("/cashDesk")
//    public ResponseEntity<?> addNewCashDesk(@RequestBody @Valid CashDesk cashDesk) {
//        return userFacade.addNewUser(cashDesk, UserTypes.CASH_DESK);
//    }
//
//    @PostMapping("/kitchen")
//    public ResponseEntity<?> addNewKitchen(@RequestBody @Valid Kitchen kitchen) {
//        return userFacade.addNewUser(kitchen, UserTypes.KITCHEN);
//    }
//
//    @PostMapping("/customer")
//    public ResponseEntity<?> addNewCustomer(@RequestBody @Valid Customer customer) {
//        return userFacade.addNewUser(customer, UserTypes.CUSTOMER);
//    }



}
