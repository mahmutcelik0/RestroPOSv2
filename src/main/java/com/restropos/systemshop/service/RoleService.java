package com.restropos.systemshop.service;

import com.restropos.systemshop.entity.Role;
import com.restropos.systemshop.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role getRole(String roleName){
        return roleRepository.getReferenceById(roleName);
    }




}
