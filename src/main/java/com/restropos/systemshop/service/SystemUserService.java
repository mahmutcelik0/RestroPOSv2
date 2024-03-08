package com.restropos.systemshop.service;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemshop.constants.UserTypes;
import com.restropos.systemshop.dto.SystemUserDto;
import com.restropos.systemshop.entity.user.SystemUser;
import com.restropos.systemshop.populator.SystemUserDtoPopulator;
import com.restropos.systemshop.repository.SystemUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SystemUserService {
    @Autowired
    private SystemUserRepository systemUserRepository;

    @Autowired
    private SystemUserDtoPopulator systemUserDtoPopulator;

    public boolean addNewSystemUser(SystemUser systemUser) {
        if (!checkSystemUserExists(systemUser.getEmail())) {
            systemUserRepository.save(systemUser);
            return true;
        } else {
            return false;
        }
    }

    public SystemUser save(SystemUser systemUser){
        return systemUserRepository.save(systemUser);
    }

    public boolean checkSystemUserExists(String email) {
        return systemUserRepository.findSystemUserByEmail(email).isPresent();
    }

    public SystemUser findSystemUserByEmail(String email) throws NotFoundException {
        return systemUserRepository.findSystemUserByEmail(email).orElseThrow(()-> new NotFoundException(CustomResponseMessage.USER_NOT_FOUND));
    }

    public Optional<SystemUser> findOptionalSystemUserByEmail(String email){
        return systemUserRepository.findSystemUserByEmail(email);
    }

    public List<SystemUserDto> getAllAdminStaffs() {
        return systemUserDtoPopulator.populateAll(systemUserRepository.getAllStaffsByRoleName(UserTypes.ADMIN.getName()));
    }

    public List<SystemUserDto> getAllWaiterStaffs() {
        return systemUserDtoPopulator.populateAll(systemUserRepository.getAllStaffsByRoleName(UserTypes.WAITER.getName()));
    }
}