package com.restropos.systemshop.service;

import com.restropos.systemshop.entity.Customer;
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

}
