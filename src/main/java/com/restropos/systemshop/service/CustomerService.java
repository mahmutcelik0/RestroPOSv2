package com.restropos.systemshop.service;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemshop.entity.user.Customer;
import com.restropos.systemshop.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

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

    public Customer findCustomerByPhoneNumber(String phoneNumber) throws NotFoundException {
        return customerRepository.findCustomerByPhoneNumber(phoneNumber).orElseThrow(()->new NotFoundException(CustomResponseMessage.CUSTOMER_NOT_FOUND));
    }

    public void save(Customer customer){
        customerRepository.save(customer);
    }
}
