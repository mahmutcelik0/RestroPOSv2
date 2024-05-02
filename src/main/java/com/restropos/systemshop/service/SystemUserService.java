package com.restropos.systemshop.service;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemcore.exception.AlreadyUsedException;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.model.ResponseMessage;
import com.restropos.systemcore.security.SecurityProvideService;
import com.restropos.systemshop.constants.UserTypes;
import com.restropos.systemshop.dto.SystemUserDto;
import com.restropos.systemshop.dto.SystemUserDtoResponse;
import com.restropos.systemshop.entity.Role;
import com.restropos.systemshop.entity.user.*;
import com.restropos.systemshop.populator.SystemUserDtoResponsePopulator;
import com.restropos.systemshop.repository.SystemUserRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SystemUserService {
    @Autowired
    private SystemUserRepository systemUserRepository;

    @Autowired
    private SystemUserDtoResponsePopulator systemUserDtoResponsePopulator;

    @Autowired
    private RoleService roleService;

    @Autowired
    private SecurityProvideService securityProvideService;

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

    public ResponseEntity<SystemUserDtoResponse> addStaff(SystemUser systemUser) {
        try {
            if (systemUserRepository.existsSystemUserByEmail(systemUser.getEmail()))
                throw new AlreadyUsedException(CustomResponseMessage.WRONG_CREDENTIAL);
            else if (systemUser.getRole().getRoleName().equals(UserTypes.ADMIN.getName())) {
                Admin admin = new Admin(systemUser);
                systemUser = systemUserRepository.save(admin);
            } else if (systemUser.getRole().getRoleName().equals(UserTypes.WAITER.getName())) {
                Waiter waiter = new Waiter(systemUser);
                systemUser = systemUserRepository.save(waiter);
            } else if (systemUser.getRole().getRoleName().equals(UserTypes.CASH_DESK.getName())) {
                CashDesk cashDesk = new CashDesk(systemUser);
                systemUser = systemUserRepository.save(cashDesk);
            } else if (systemUser.getRole().getRoleName().equals(UserTypes.KITCHEN.getName())) {
                Kitchen kitchen = new Kitchen(systemUser);
                systemUser = systemUserRepository.save(kitchen);
            }
            return ResponseEntity.ok(systemUserDtoResponsePopulator.populate(systemUser));
        } catch (Exception e) {
            throw new BadCredentialsException(CustomResponseMessage.WRONG_CREDENTIAL);
        }
    }

    public ResponseEntity<ResponseMessage> deleteStaff(String email) {
        try {
            systemUserRepository.deleteSystemUserByEmail(email);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, CustomResponseMessage.USER_NOT_FOUND));
        }
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK, CustomResponseMessage.USER_DELETED_SUCCESSFULLY));
    }


    public List<SystemUserDtoResponse> getStaffByRole(UserTypes userType) {
        return systemUserDtoResponsePopulator.populateAll(systemUserRepository.getAllStaffsByRoleName(userType.getName()));
    }

    public List<SystemUserDtoResponse> getAllStaffsExceptAdminDto() throws NotFoundException {
        ;
        return systemUserDtoResponsePopulator.populateAll(systemUserRepository.getAllStaffsExceptRole(UserTypes.ADMIN.getName(),securityProvideService.getWorkspace().getBusinessDomain()));
    }

    public List<SystemUser> getAllWaiters() {
        return systemUserRepository.getAllStaffsByRoleName(UserTypes.WAITER.getName());
    }

    public ResponseEntity<SystemUserDtoResponse> updateStaff(SystemUserDto systemUserDto, String email) throws NotFoundException {
        var updatedUser = systemUserRepository.findSystemUserByEmail(email).map(systemUser -> {
            if(StringUtils.isNotEmpty(systemUserDto.getFirstName())){
                systemUser.setFirstName(systemUserDto.getFirstName());
            }
            if(StringUtils.isNotEmpty(systemUserDto.getLastName())){
                systemUser.setLastName(systemUserDto.getLastName());
            }
            if(StringUtils.isNotEmpty(systemUserDto.getEmail())){
                systemUser.setEmail(systemUserDto.getEmail());
            }
            if(StringUtils.isNotEmpty(systemUserDto.getRole())){
                systemUserRepository.delete(systemUser);
                Role role = roleService.getRole(systemUserDto.getRole());
                systemUser.setRole(role);
                if(role.getRoleName().equals(UserTypes.KITCHEN.getName())){
                    return systemUserRepository.save(new Kitchen(systemUser));
                }else if(role.getRoleName().equals(UserTypes.WAITER.getName())){
                    return systemUserRepository.save(new Waiter(systemUser));
                }else if(role.getRoleName().equals(UserTypes.CASH_DESK.getName())){
                    return systemUserRepository.save(new CashDesk(systemUser));
                }
            }
            systemUserRepository.delete(systemUser);
            return systemUserRepository.save(systemUser);
        }).orElseThrow(()-> new NotFoundException(CustomResponseMessage.USER_NOT_FOUND));
        return ResponseEntity.ok(systemUserDtoResponsePopulator.populate(updatedUser));
    }

    public SystemUser getSystemUser(String email) throws NotFoundException {
        return systemUserRepository.findSystemUserByEmail(email).orElseThrow(()-> new NotFoundException(CustomResponseMessage.USER_NOT_FOUND));
    }
}