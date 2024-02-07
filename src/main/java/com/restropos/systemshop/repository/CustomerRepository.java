package com.restropos.systemshop.repository;

import com.restropos.systemshop.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface CustomerRepository extends JpaRepository<Customer,Long> {
    @Query("select c from Customer as c where c.phoneNumber = ?1")
    Optional<Customer> findCustomerByPhoneNumber(String phoneNumber);
}
