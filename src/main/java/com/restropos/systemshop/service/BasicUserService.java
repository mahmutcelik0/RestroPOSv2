package com.restropos.systemshop.service;

import com.restropos.systemshop.entity.BasicUser;
import com.restropos.systemshop.repository.BasicUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BasicUserService {
    @Autowired
    private BasicUserRepository basicUserRepository;

    public boolean addNewBasicUser(BasicUser basicUser) {
        if (!checkBasicUserExists(basicUser.getEmail())) {
            basicUserRepository.save(basicUser);
            return true;
        } else {
            return false;
        }
    }


    public boolean checkBasicUserExists(String email) {
        return basicUserRepository.findBasicUserByEmail(email).isPresent();
    }


}
