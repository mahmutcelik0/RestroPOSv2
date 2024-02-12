package com.restropos.systemshop.facade;

import com.restropos.systemshop.constants.UserTypes;
import com.restropos.systemshop.entity.user.BasicUser;
import com.restropos.systemshop.entity.user.Customer;
import com.restropos.systemshop.entity.user.GenericUser;
import com.restropos.systemshop.entity.user.SystemUser;
import com.restropos.systemshop.service.BasicUserService;
import com.restropos.systemshop.service.CustomerService;
import com.restropos.systemshop.service.SystemUserService;
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

    public ResponseEntity<?> addNewUser(GenericUser genericUser, UserTypes userType) {
        ResponseEntity<?> successResponse = new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
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
        return new ResponseEntity<>("User already exists", HttpStatus.NOT_ACCEPTABLE);
    }


}
