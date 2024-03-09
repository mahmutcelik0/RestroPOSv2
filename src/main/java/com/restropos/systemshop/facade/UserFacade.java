package com.restropos.systemshop.facade;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemcore.exception.AlreadyUsedException;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.exception.UnauthorizedException;
import com.restropos.systemcore.exception.WrongCredentialsException;
import com.restropos.systemcore.model.ResponseMessage;
import com.restropos.systemshop.constants.UserTypes;
import com.restropos.systemshop.dto.BasicUserDto;
import com.restropos.systemshop.dto.CustomerDto;
import com.restropos.systemshop.dto.SystemUserDto;
import com.restropos.systemshop.dto.UserDto;
import com.restropos.systemshop.entity.user.BasicUser;
import com.restropos.systemshop.entity.user.Customer;
import com.restropos.systemshop.entity.user.SystemUser;
import com.restropos.systemshop.populator.WorkspaceDtoPopulator;
import com.restropos.systemshop.service.*;
import com.restropos.systemverify.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFacade {
    @Autowired
    private BasicUserService basicUserService;

    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private WorkspaceDtoPopulator workspaceDtoPopulator;

    @Autowired
    private SmsService smsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    private RoleService roleService;

    public ResponseEntity<ResponseMessage> registerNewCustomer(CustomerDto customerDto) throws AlreadyUsedException, NotFoundException {
        if (customerService.checkCustomerExists(customerDto.getPhoneNumber())) {
            throw new AlreadyUsedException(CustomResponseMessage.PHONE_NUMBER_ALREADY_USED);
        } else {
            Customer customer = Customer.builder()
                    .firstName(customerDto.getFirstName())
                    .lastName(customerDto.getLastName())
                    .phoneNumber(customerDto.getPhoneNumber())
                    .loginDisabled(true)
                    .profilePhoto(customerDto.getProfilePhoto())
                    .build();

            customerService.save(customer);
            return smsService.sendSMS(customer.getPhoneNumber());
        }
    }

    public boolean customerValid(String phoneNumber){
        return customerService.checkCustomerValid(phoneNumber);
    }

    public ResponseEntity<UserDto> getUser(String businessDomain) throws NotFoundException, UnauthorizedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var role = authentication.getAuthorities().stream().findFirst().orElseThrow(()-> new NotFoundException(CustomResponseMessage.ROLE_NOT_FOUND));

        if(role.getAuthority().equals(UserTypes.ADMIN.getName()) || role.getAuthority().equals(UserTypes.WAITER.getName())){
            SystemUser systemUser = systemUserService.findSystemUserByEmail(authentication.getPrincipal().toString());
            if(!systemUser.getWorkspace().getBusinessDomain().equals(businessDomain)) throw new UnauthorizedException(CustomResponseMessage.USER_PERMISSION_PROBLEM);
            UserDto userDto = UserDto.builder()
                    .firstName(systemUser.getFirstName())
                    .lastName(systemUser.getLastName())
                    .email(systemUser.getEmail())
                    .workspaceDto(workspaceDtoPopulator.populate(systemUser.getWorkspace()))
                    .role(systemUser.getRole().getRoleName())
                    .build();
            return ResponseEntity.ok(userDto);
        }else if(role.getAuthority().equals(UserTypes.KITCHEN.getName()) || role.getAuthority().equals(UserTypes.CASH_DESK.getName())){
            BasicUser basicUser = basicUserService.findBasicUserByEmail(authentication.getPrincipal().toString());
            if(!basicUser.getWorkspace().getBusinessDomain().equals(businessDomain)) throw new UnauthorizedException(CustomResponseMessage.USER_PERMISSION_PROBLEM);
            UserDto userDto = UserDto.builder()
                    .deviceName(basicUser.getDeviceName())
                    .email(basicUser.getEmail())
                    .workspaceDto(workspaceDtoPopulator.populate(basicUser.getWorkspace()))
                    .role(basicUser.getRole().getRoleName())
                    .build();
            return ResponseEntity.ok(userDto);
        }
        return ResponseEntity.badRequest().body(null);
    }

    public List<BasicUserDto> getAllKitchenStaffs() {
        return basicUserService.getAllStaffs(UserTypes.KITCHEN);
    }

    public List<BasicUserDto> getAllCashDeskStaffs() {
        return basicUserService.getAllStaffs(UserTypes.CASH_DESK);
    }

    public List<SystemUserDto> getAllAdminStaffs() {
        return systemUserService.getAllStaffs(UserTypes.ADMIN);
    }

    public List<SystemUserDto> getAllWaiterStaffs() {
        return systemUserService.getAllStaffs(UserTypes.WAITER);
    }

    public ResponseEntity<ResponseMessage> deleteKitchenAndCashDeskStaff(String email) {
        return basicUserService.deleteStaff(email);
    }

    public ResponseEntity<ResponseMessage> deleteAdminAndWaiterStaff(String email) {
        return systemUserService.deleteStaff(email);
    }

    public ResponseEntity<ResponseMessage> addNewKitchenAndCashDeskStaffs(BasicUserDto basicUserDto) throws WrongCredentialsException, NotFoundException {
        String basicUserRoleName = basicUserDto.getRole();
        if(!(basicUserRoleName.equals(UserTypes.KITCHEN.getName()) || basicUserRoleName.equals(UserTypes.CASH_DESK.getName()))) throw new WrongCredentialsException(CustomResponseMessage.USER_PERMISSION_PROBLEM);

        BasicUser basicUser = BasicUser.builder()
                .deviceName(basicUserDto.getDeviceName())
                .role(roleService.getRole(basicUserRoleName))
                .email(basicUserDto.getEmail())
                .password(passwordEncoder.encode(basicUserDto.getPassword()))
                .workspace(workspaceService.getWorkspace(basicUserDto.getWorkspaceDto().getBusinessName()))
                .build();

        return basicUserService.addStaff(basicUser);
    }

    public ResponseEntity<ResponseMessage> addNewAdminAndWaiterStaffs(SystemUserDto systemUserDto) throws WrongCredentialsException, NotFoundException {
        String systemUserRoleName = systemUserDto.getRole();
        if(!(systemUserRoleName.equals(UserTypes.ADMIN.getName()) || systemUserRoleName.equals(UserTypes.WAITER.getName()))) throw new WrongCredentialsException(CustomResponseMessage.USER_PERMISSION_PROBLEM);

        SystemUser systemUser = SystemUser.builder()
                .firstName(systemUserDto.getFirstName())
                .lastName(systemUserDto.getLastName())
                .email(systemUserDto.getEmail())
                .role(roleService.getRole(systemUserRoleName))
                .password(passwordEncoder.encode(systemUserDto.getPassword()))
                .workspace(workspaceService.getWorkspace(systemUserDto.getWorkspaceDto().getBusinessName()))
                .build();

        return systemUserService.addStaff(systemUser);
    }
}
