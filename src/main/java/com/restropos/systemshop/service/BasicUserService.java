package com.restropos.systemshop.service;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemshop.entity.user.BasicUser;
import com.restropos.systemshop.repository.BasicUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public BasicUser findBasicUserByEmail(String email) throws NotFoundException {
        return basicUserRepository.findBasicUserByEmail(email).orElseThrow(()->new NotFoundException(CustomResponseMessage.USER_NOT_FOUND));
    }

    public Optional<BasicUser> findOptionalBasicUserByEmail(String email) {
        return basicUserRepository.findBasicUserByEmail(email);
    }

}