package com.restropos.systemshop.api;

import com.restropos.systemcore.dto.BearerToken;
import com.restropos.systemcore.dto.LoginDto;
import com.restropos.systemcore.security.UsernamePasswordAuthenticationProvider;
import com.restropos.systemcore.utils.JwtTokenUtil;
import com.restropos.systemshop.dto.EmailSecuredUserDto;
import com.restropos.systemshop.populator.BasicUserDtoPopulator;
import com.restropos.systemshop.populator.CustomerDtoPopulator;
import com.restropos.systemshop.populator.SystemUserDtoPopulator;
import com.restropos.systemshop.repository.BasicUserRepository;
import com.restropos.systemshop.repository.CustomerRepository;
import com.restropos.systemshop.repository.SystemUserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.Role;
import java.util.List;
import java.util.Set;

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

}
