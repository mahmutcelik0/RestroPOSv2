package com.restropos.systemrefactor.service;

import com.restropos.systemcore.entity.SecureToken;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.service.SecureTokenService;
import com.restropos.systemrefactor.config.TwilioConfig;
import com.restropos.systemrefactor.dto.OtpResponseDto;
import com.restropos.systemrefactor.dto.OtpStatus;
import com.restropos.systemshop.entity.user.Customer;
import com.restropos.systemshop.service.CustomerService;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsService {
    @Autowired
    private TwilioConfig twilioConfig;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SecureTokenService tokenService;



    public OtpResponseDto sendSMS(String phoneNumber) throws NotFoundException {
        if(!phoneNumber.startsWith("+")) phoneNumber = "+"+phoneNumber;

        Customer customer = customerService.findCustomerByPhoneNumber(phoneNumber);

        SecureToken secureToken = tokenService.generateTokenForCustomer(customer);

        OtpResponseDto otpResponseDto = null;
        try {
            PhoneNumber to = new PhoneNumber(phoneNumber);//to
            PhoneNumber from = new PhoneNumber(twilioConfig.getPhoneNumber()); // from

            String otpMessage = "Hello "+customer.getFirstName()+" "+customer.getLastName()+", you have to verify your account using " +secureToken.getToken()+" code.";
            Message.creator(to, from,otpMessage).create(); //sends sms
            otpResponseDto = new OtpResponseDto(OtpStatus.DELIVERED, otpMessage);
        } catch (Exception e) {
            //secure token silinmeli todo sil
            e.printStackTrace();
            otpResponseDto = new OtpResponseDto(OtpStatus.FAILED, e.getMessage());
        }
        return otpResponseDto;
    }

}
