package com.restropos.systemshop.facade;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemcore.exception.AlreadyUsedException;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.exception.UnauthorizedException;
import com.restropos.systemcore.model.ResponseMessage;
import com.restropos.systemshop.constants.UserTypes;
import com.restropos.systemshop.dto.CustomerDto;
import com.restropos.systemshop.dto.UserDto;
import com.restropos.systemshop.entity.user.BasicUser;
import com.restropos.systemshop.entity.user.Customer;
import com.restropos.systemshop.entity.user.GenericUser;
import com.restropos.systemshop.entity.user.SystemUser;
import com.restropos.systemshop.populator.BasicUserDtoPopulator;
import com.restropos.systemshop.populator.SystemUserDtoPopulator;
import com.restropos.systemshop.populator.WorkspaceDtoPopulator;
import com.restropos.systemshop.service.BasicUserService;
import com.restropos.systemshop.service.CustomerService;
import com.restropos.systemshop.service.SystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserFacade {
    @Autowired
    private BasicUserService basicUserService;

    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SystemUserDtoPopulator systemUserDtoPopulator;

    @Autowired
    private BasicUserDtoPopulator basicUserDtoPopulator;

    @Autowired
    private WorkspaceDtoPopulator workspaceDtoPopulator;

    public ResponseEntity<ResponseMessage> addNewUser(GenericUser genericUser, UserTypes userType) {
        ResponseEntity<ResponseMessage> successResponse = new ResponseEntity<>(new ResponseMessage(HttpStatus.OK, CustomResponseMessage.USER_CREATED), HttpStatus.OK);
//        genericUser.setRole(); //todo user type a göre rol eklenecek -- burada şu an gerek yok ilerde kullanılacak olursa o zaman ekle
        if (userType.equals(UserTypes.CUSTOMER)) {
            if (customerService.addNewCustomer((Customer) genericUser))
                return successResponse;
        } else if (userType.equals(UserTypes.CASH_DESK) || userType.equals(UserTypes.KITCHEN)) {
            if (basicUserService.addNewBasicUser((BasicUser) genericUser))
                return successResponse;
        } else if (userType.equals(UserTypes.ADMIN) || userType.equals(UserTypes.WAITER)) {
            if (systemUserService.addNewSystemUser((SystemUser) genericUser))
                return successResponse;
        }
        return new ResponseEntity<>(ResponseMessage.builder()
                .message(CustomResponseMessage.USER_ALREADY_EXISTS)
                .status(HttpStatus.INTERNAL_SERVER_ERROR).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }



    public ResponseEntity<ResponseMessage> registerNewCustomer(CustomerDto customerDto) throws AlreadyUsedException {
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

            return new ResponseEntity<>(new ResponseMessage(HttpStatus.OK,CustomResponseMessage.CUSTOMER_CREATED), HttpStatus.OK);
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
}
