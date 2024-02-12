package com.restropos.systemcore.security;

import com.restropos.systemshop.constants.UserTypes;
import com.restropos.systemshop.entity.user.BasicUser;
import com.restropos.systemshop.entity.user.Customer;
import com.restropos.systemshop.entity.user.SystemUser;
import com.restropos.systemshop.service.BasicUserService;
import com.restropos.systemshop.service.CustomerService;
import com.restropos.systemshop.service.SystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private BasicUserService basicUserService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SystemUserService systemUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public UserDetails loadUserByUsername(String username, String userType) throws UsernameNotFoundException {
        if (userType.equals(UserTypes.ADMIN.getName()) || userType.equals(UserTypes.WAITER.getName())) {
            SystemUser systemUser = systemUserService.findSystemUserByEmail(username);

            return new org.springframework.security.core.userdetails.User(systemUser.getEmail(), systemUser.getPassword(), List.of(new SimpleGrantedAuthority(systemUser.getRole().getRoleName())));
        } else if (userType.equals(UserTypes.KITCHEN.getName()) || userType.equals(UserTypes.CASH_DESK.getName())) {
            BasicUser basicUser = basicUserService.findBasicUserByEmail(username);

            return new org.springframework.security.core.userdetails.User(basicUser.getEmail(), basicUser.getPassword(), List.of(new SimpleGrantedAuthority(basicUser.getRole().getRoleName())));
        } else if (userType.equals(UserTypes.CUSTOMER.getName())) {
            Customer customer = customerService.findCustomerByPhoneNUmber(username);

            return new org.springframework.security.core.userdetails.User(customer.getPhoneNumber(),customer.getPhoneNumber(), List.of(new SimpleGrantedAuthority(customer.getRole().getRoleName())));
        }
        return null;
    }

}
