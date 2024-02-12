package com.restropos.systemshop.api;

import com.restropos.systemcore.entity.SecureToken;
import com.restropos.systemcore.service.SecureTokenService;
import com.restropos.systemshop.populator.BasicUserDtoPopulator;
import com.restropos.systemshop.populator.CustomerDtoPopulator;
import com.restropos.systemshop.populator.SystemUserDtoPopulator;
import com.restropos.systemshop.repository.BasicUserRepository;
import com.restropos.systemshop.repository.CustomerRepository;
import com.restropos.systemshop.repository.SystemUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tests")
public class CustomTestApi {
    @Autowired
    private BasicUserRepository basicUserRepository;

    @Autowired
    private BasicUserDtoPopulator basicUserDtoPopulator;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerDtoPopulator customerDtoPopulator;

    @Autowired
    private SystemUserRepository systemUserRepository;

    @Autowired
    private SystemUserDtoPopulator systemUserDtoPopulator;

    @Autowired
    private SecureTokenService secureTokenService;



    @GetMapping("/basicUser")
    public List<?> getBasicUser(){
        return List.of(basicUserDtoPopulator.populate(basicUserRepository.findBasicUserByEmail("mahmut.382@gmail.com").orElseThrow(()->new RuntimeException("aa"))));
    }

    @GetMapping("/customer")
    public List<?> getCustomer(){
        return List.of(customerDtoPopulator.populate(customerRepository.findCustomerByPhoneNumber("+905466053396").orElseThrow(()->new RuntimeException("aa"))));
    }

    @GetMapping("/systemUser")
    public List<?> getSystemUser(){
        return List.of(systemUserDtoPopulator.populate(systemUserRepository.findSystemUserByEmail("mahmut.382@gmail.com").orElseThrow(()->new RuntimeException("aa"))));
    }

    @GetMapping("/token")
    public SecureToken getToken(){
        return secureTokenService.generateTokenForBasicUser(basicUserRepository.findBasicUserByEmail("mahmut.382@hotmail.com").get());
    }

}
