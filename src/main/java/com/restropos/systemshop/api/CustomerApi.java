package com.restropos.systemshop.api;

import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.exception.UnauthorizedException;
import com.restropos.systemshop.dto.CustomerDto;
import com.restropos.systemshop.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerApi {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/token")
    public ResponseEntity<CustomerDto> getUser() throws UnauthorizedException, NotFoundException {
        return customerService.getUser();
    }
}
