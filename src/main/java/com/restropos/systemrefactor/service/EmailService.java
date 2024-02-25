package com.restropos.systemrefactor.service;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.model.ResponseMessage;
import com.restropos.systemcore.utils.LogUtil;
import com.restropos.systemrefactor.command.WorkspaceVerifyEmailCommand;
import com.restropos.systemshop.entity.user.SystemUser;
import com.restropos.systemshop.service.SystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private WorkspaceVerifyEmailCommand emailCommand;

    @Autowired
    private SystemUserService userService;

    public ResponseEntity<ResponseMessage> sendWorkspaceVerifyEmail(String email){
        try {
            SystemUser user = userService.findSystemUserByEmail(email);
            return new ResponseEntity<>(new ResponseMessage(HttpStatus.OK,emailCommand.sendEmail(user).getResponseMessage()), HttpStatus.OK);
        }catch (UsernameNotFoundException e){
            LogUtil.printLog("USER NOT FOUND", EmailService.class);
            return new ResponseEntity<>(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR,CustomResponseMessage.EMAIL_NOT_FOUND),HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (NotFoundException e){
            return new ResponseEntity<>(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR,CustomResponseMessage.TEMPLATE_NOT_FOUND_EXCEPTION),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
