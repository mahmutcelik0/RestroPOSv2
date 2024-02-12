package com.restropos.systemshop.service;

import com.restropos.systemshop.entity.user.BasicUser;
import com.restropos.systemshop.entity.user.SystemUser;
import com.restropos.systemshop.repository.SystemUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import javax.sound.midi.SysexMessage;
import javax.swing.text.html.Option;
import java.util.Optional;

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

    public void save(SystemUser systemUser){
        systemUserRepository.save(systemUser);
    }

    public boolean checkSystemUserExists(String email) {
        return systemUserRepository.findSystemUserByEmail(email).isPresent();
    }

    public SystemUser findSystemUserByEmail(String email){
        return systemUserRepository.findSystemUserByEmail(email).orElseThrow(()-> new RuntimeException("not found"));
    }

    public Optional<SystemUser> findOptionalSystemUserByEmail(String email){
        return systemUserRepository.findSystemUserByEmail(email);
    }


}
