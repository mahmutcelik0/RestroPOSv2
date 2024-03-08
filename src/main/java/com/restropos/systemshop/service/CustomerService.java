package com.restropos.systemshop.service;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.exception.UnauthorizedException;
import com.restropos.systemshop.constants.UserTypes;
import com.restropos.systemshop.dto.CustomerDto;
import com.restropos.systemshop.entity.user.Customer;
import com.restropos.systemshop.populator.CustomerDtoPopulator;
import com.restropos.systemshop.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerDtoPopulator customerDtoPopulator;

    public boolean addNewCustomer(Customer customer) {
        if (!checkCustomerExists(customer.getPhoneNumber())) {
            customerRepository.save(customer);
            return true;
        } else {
            return false;
        }
    }

    public boolean checkCustomerExists(String phoneNumber) {
        return customerRepository.findCustomerByPhoneNumber(phoneNumber).isPresent();
    }

    public boolean checkCustomerValid(String phoneNumber) {
        return !checkCustomerExists(phoneNumber);
    }
    public Customer findCustomerByPhoneNumber(String phoneNumber) throws NotFoundException {
        return customerRepository.findCustomerByPhoneNumber(phoneNumber).orElseThrow(()->new NotFoundException(CustomResponseMessage.CUSTOMER_NOT_FOUND));
    }

    public void save(Customer customer){
        customerRepository.save(customer);
    }

    public ResponseEntity<CustomerDto>  getUser() throws NotFoundException, UnauthorizedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var role = authentication.getAuthorities().stream().findFirst().orElseThrow(()->new NotFoundException(CustomResponseMessage.ROLE_NOT_FOUND));

        if(!role.getAuthority().equals(UserTypes.CUSTOMER.getName())) throw new UnauthorizedException(CustomResponseMessage.USER_PERMISSION_PROBLEM);
        Customer customer = customerRepository.findCustomerByPhoneNumber(authentication.getPrincipal().toString()).orElseThrow(()->new NotFoundException(CustomResponseMessage.CUSTOMER_NOT_FOUND));

        return ResponseEntity.ok(customerDtoPopulator.populate(customer));
    }
}
