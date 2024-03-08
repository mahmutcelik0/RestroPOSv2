package com.restropos.systemshop.service;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemshop.constants.UserTypes;
import com.restropos.systemshop.dto.BasicUserDto;
import com.restropos.systemshop.entity.user.BasicUser;
import com.restropos.systemshop.populator.BasicUserDtoPopulator;
import com.restropos.systemshop.repository.BasicUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BasicUserService {
    @Autowired
    private BasicUserRepository basicUserRepository;

    @Autowired
    private BasicUserDtoPopulator basicUserDtoPopulator;

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

    public List<BasicUserDto> getAllKitchenStaffs() {
        return basicUserDtoPopulator.populateAll(basicUserRepository.getAllStaffsByRole(UserTypes.KITCHEN.name()));
    }

    public List<BasicUserDto> getAllCashDeskStaffs() {
        return basicUserDtoPopulator.populateAll(basicUserRepository.getAllStaffsByRole(UserTypes.CASH_DESK.name()));
    }


}