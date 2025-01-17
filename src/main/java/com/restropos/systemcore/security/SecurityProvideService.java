package com.restropos.systemcore.security;

import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemshop.entity.Workspace;
import com.restropos.systemshop.entity.user.Customer;
import com.restropos.systemshop.entity.user.SystemUser;
import com.restropos.systemshop.service.CustomerService;
import com.restropos.systemshop.service.SystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityProvideService {
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private CustomerService customerService;

    public Workspace getWorkspace() throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return systemUserService.getSystemUser(authentication.getPrincipal().toString()).getWorkspace();
    }

    public SystemUser getSystemUser() throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return systemUserService.getSystemUser(authentication.getPrincipal().toString());
    }

    public Customer getCustomer() throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return customerService.findCustomerByPhoneNumber(authentication.getPrincipal().toString());
    }
}
