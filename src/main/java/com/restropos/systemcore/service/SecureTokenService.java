package com.restropos.systemcore.service;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemcore.dto.EnableToken;
import com.restropos.systemcore.entity.SecureToken;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.exception.TimeExceededException;
import com.restropos.systemcore.exception.WrongCredentialsException;
import com.restropos.systemcore.model.ResponseMessage;
import com.restropos.systemcore.repository.SecureTokenRepository;
import com.restropos.systemshop.constants.UserTypes;
import com.restropos.systemshop.entity.user.Customer;
import com.restropos.systemshop.entity.user.SystemUser;
import com.restropos.systemshop.service.CustomerService;
import com.restropos.systemshop.service.SystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SecureTokenService {
    @Autowired
    private SecureTokenRepository secureTokenRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SystemUserService systemUserService;

    public SecureToken generateTokenForSystemUser(String adminEmail) throws NotFoundException {
        SystemUser systemUser = systemUserService.findSystemUserByEmail(adminEmail);

        SecureToken secureToken = new SecureToken(generateRandomCode(), LocalDateTime.now(), LocalDateTime.now().plusMinutes(2), systemUser);
        return generateToken(secureToken);
    }

    public SecureToken generateTokenForCustomer(Customer customer) {
        SecureToken secureToken = new SecureToken(generateRandomCode(), LocalDateTime.now(), LocalDateTime.now().plusMinutes(2), customer);
        return generateToken(secureToken);
    }

    private SecureToken generateToken(SecureToken secureToken) {
        secureTokenRepository.existsSecureTokenByToken(secureToken.getToken());

        while (secureTokenRepository.existsSecureTokenByToken(secureToken.getToken())) {
            secureToken.setToken(generateRandomCode());
        }

        return secureTokenRepository.save(secureToken);
    }


    public String generateRandomCode() {
        SecureRandom random = new SecureRandom();
        return String.valueOf(random.nextInt(100000, 999999));
    }

    public ResponseEntity<ResponseMessage> enableAccountWithToken(EnableToken enableToken) throws NotFoundException, TimeExceededException {
        Optional<SecureToken> secureToken = secureTokenRepository.findSecureTokenByAccountInformation(enableToken.getAccountInformation(),enableToken.getTokenCode());

        if (secureToken.isEmpty()) throw new NotFoundException(CustomResponseMessage.TOKEN_NOT_FOUND);

        var token = secureToken.get();

        if (token.isExpired()) throw new TimeExceededException(CustomResponseMessage.TIME_EXCEEDED);
        else {
            var userRoleName = ObjectUtils.isEmpty(token.getCustomer()) ? token.getSystemUser().getRole().getRoleName() : UserTypes.CUSTOMER.getName();
            if (userRoleName.equals(UserTypes.ADMIN.getName()) || userRoleName.equals(UserTypes.WAITER.getName())) {
                var user = systemUserService.findSystemUserByEmail(token.getSystemUser().getEmail());
                user.setLoginDisabled(false);
                systemUserService.save(user);
            } else if (userRoleName.equals(UserTypes.CUSTOMER.getName())) {
                var user = customerService.findCustomerByPhoneNumber(token.getCustomer().getPhoneNumber());
                user.setLoginDisabled(false);
                customerService.save(user);
            }
            secureTokenRepository.delete(token);
        }
        return new ResponseEntity<>(new ResponseMessage(HttpStatus.OK,CustomResponseMessage.ACCOUNT_ACTIVATED), HttpStatus.OK);
    }
}
