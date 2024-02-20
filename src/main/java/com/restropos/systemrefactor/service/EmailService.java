package com.restropos.systemrefactor.service;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.utils.LogUtil;
import com.restropos.systemrefactor.command.WorkspaceVerifyEmailCommand;
import com.restropos.systemrefactor.command.WorkspaceVerifyEmailResponse;
import com.restropos.systemshop.entity.user.SystemUser;
import com.restropos.systemshop.service.SystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private WorkspaceVerifyEmailCommand emailCommand;

    @Autowired
    private SystemUserService userService;

    public WorkspaceVerifyEmailResponse sendWorkspaceVerifyEmail(String email){
        try {
            SystemUser user = userService.findSystemUserByEmail(email);
            return new WorkspaceVerifyEmailResponse(emailCommand.sendEmail(user).getResponseMessage()) ;
        }catch (UsernameNotFoundException e){
            LogUtil.printLog("USER NOT FOUND", EmailService.class);
            return new WorkspaceVerifyEmailResponse(CustomResponseMessage.EMAIL_NOT_FOUND);
        }catch (NotFoundException e){
            return new WorkspaceVerifyEmailResponse(CustomResponseMessage.TEMPLATE_NOT_FOUND_EXCEPTION);
        }
    }
}
