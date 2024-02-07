package com.restropos.systemshop.service;

import com.restropos.systemshop.entity.SystemUser;
import com.restropos.systemshop.repository.SystemUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemUserService {
    @Autowired
    private SystemUserRepository systemUserRepository;

    public boolean addNewSystemUser(SystemUser systemUser) {
        if (!checkSystemUserExists(systemUser.getEmail())) {
            systemUserRepository.save(systemUser);
            return true;
        } else {
            return false;
        }
    }


    public boolean checkSystemUserExists(String email) {
        return systemUserRepository.findSystemUserByEmail(email).isPresent();
    }

}
