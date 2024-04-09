package com.restropos.systemcore.security;

import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemshop.entity.Workspace;
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

    public Workspace getWorkspace() throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return systemUserService.getSystemUser(authentication.getPrincipal().toString()).getWorkspace();
    }
}
