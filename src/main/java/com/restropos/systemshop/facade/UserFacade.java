package com.restropos.systemshop.facade;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemcore.exception.AlreadyUsedException;
import com.restropos.systemcore.model.ResponseMessage;
import com.restropos.systemshop.constants.UserTypes;
import com.restropos.systemshop.dto.CustomerDto;
import com.restropos.systemshop.dto.RegisterDto;
import com.restropos.systemshop.entity.user.*;
import com.restropos.systemshop.service.BasicUserService;
import com.restropos.systemshop.service.CustomerService;
import com.restropos.systemshop.service.SystemUserService;
import com.restropos.systemshop.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private WorkspaceService workspaceService;

    public ResponseEntity<ResponseMessage> addNewUser(GenericUser genericUser, UserTypes userType) {
        ResponseEntity<ResponseMessage> successResponse = new ResponseEntity<>(new ResponseMessage(HttpStatus.OK, CustomResponseMessage.USER_CREATED), HttpStatus.OK);
//        genericUser.setRole(); //todo user type a göre rol eklenecek
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
        return customerService.checkCustomerExists(phoneNumber);
    }
}
