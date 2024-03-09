package com.restropos.systemshop.service;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemcore.exception.AlreadyUsedException;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.model.ResponseMessage;
import com.restropos.systemshop.constants.UserTypes;
import com.restropos.systemshop.dto.SystemUserDto;
import com.restropos.systemshop.entity.user.Admin;
import com.restropos.systemshop.entity.user.SystemUser;
import com.restropos.systemshop.entity.user.Waiter;
import com.restropos.systemshop.populator.SystemUserDtoPopulator;
import com.restropos.systemshop.repository.SystemUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SystemUserService {
    @Autowired
    private SystemUserRepository systemUserRepository;

    @Autowired
    private SystemUserDtoPopulator systemUserDtoPopulator;

    public SystemUser save(SystemUser systemUser) {
        return systemUserRepository.save(systemUser);
    }

    public boolean checkSystemUserExists(String email) {
        return systemUserRepository.findSystemUserByEmail(email).isPresent();
    }

    public SystemUser findSystemUserByEmail(String email) throws NotFoundException {
        return systemUserRepository.findSystemUserByEmail(email).orElseThrow(() -> new NotFoundException(CustomResponseMessage.USER_NOT_FOUND));
    }

    public Optional<SystemUser> findOptionalSystemUserByEmail(String email) {
        return systemUserRepository.findSystemUserByEmail(email);
    }

    public List<SystemUserDto> getAllStaffs(UserTypes userType) {
        return systemUserDtoPopulator.populateAll(systemUserRepository.getAllStaffsByRoleName(userType.getName()));
    }

    public ResponseEntity<ResponseMessage> addStaff(SystemUser systemUser){
        try {
            if(systemUserRepository.existsSystemUserByEmail(systemUser.getEmail()))
                throw new AlreadyUsedException(CustomResponseMessage.WRONG_CREDENTIAL);
            else if (systemUser.getRole().getRoleName().equals(UserTypes.ADMIN.getName())){
                Admin admin = new Admin(systemUser);
                systemUserRepository.save(admin);
            }else {
                Waiter waiter = new Waiter(systemUser);
                systemUserRepository.save(waiter);
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ResponseMessage(HttpStatus.BAD_REQUEST,CustomResponseMessage.WRONG_CREDENTIAL));
        }

        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK,CustomResponseMessage.USER_CREATED));
    }

    public ResponseEntity<ResponseMessage> deleteStaff(String email) {
        try {
            systemUserRepository.deleteSystemUserByEmail(email);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, CustomResponseMessage.USER_NOT_FOUND));
        }
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK, CustomResponseMessage.USER_DELETED_SUCCESSFULLY));
    }


}