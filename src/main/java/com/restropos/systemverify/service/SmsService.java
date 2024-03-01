package com.restropos.systemverify.service;

import com.restropos.systemcore.entity.SecureToken;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.model.ResponseMessage;
import com.restropos.systemcore.service.SecureTokenService;
import com.restropos.systemcore.utils.LogUtil;
import com.restropos.systemverify.config.TwilioConfig;
import com.restropos.systemverify.dto.OtpResponseDto;
import com.restropos.systemverify.dto.OtpStatus;
import com.restropos.systemshop.entity.user.Customer;
import com.restropos.systemshop.service.CustomerService;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SmsService {
    @Autowired
    private TwilioConfig twilioConfig;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SecureTokenService tokenService;



    public ResponseEntity<ResponseMessage> sendSMS(String phoneNumber) throws NotFoundException {
        Customer customer = customerService.findCustomerByPhoneNumber(phoneNumber);

        SecureToken secureToken = tokenService.generateTokenForCustomer(customer);

        ResponseEntity<ResponseMessage> response = null;
        try {
            if(!phoneNumber.startsWith("+")) phoneNumber = "+"+phoneNumber;
            PhoneNumber to = new PhoneNumber(phoneNumber);//to
            PhoneNumber from = new PhoneNumber(twilioConfig.getPhoneNumber()); // from

            String otpMessage = "Hello "+customer.getFirstName()+" "+customer.getLastName()+", you have to verify your account using " +secureToken.getToken()+" code.";
            Message.creator(to, from,otpMessage).create(); //sends sms
            response = new ResponseEntity<>(new ResponseMessage(HttpStatus.OK,otpMessage),HttpStatus.OK);
        } catch (Exception e) {
            tokenService.delete(secureToken);
            LogUtil.printLog(e.getMessage(),SmsService.class);
            response = new ResponseEntity<>(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR,"Message couldn't send"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

}
