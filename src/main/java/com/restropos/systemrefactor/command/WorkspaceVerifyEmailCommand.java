package com.restropos.systemrefactor.command;

import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.service.SecureTokenService;
import com.restropos.systemrefactor.builder.EmailTemplate;
import com.restropos.systemrefactor.builder.WorkspaceVerifyEmailTemplate;
import com.restropos.systemrefactor.constants.EmailConstants;
import com.restropos.systemrefactor.entity.RawEmailTemplate;
import com.restropos.systemshop.entity.user.SystemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WorkspaceVerifyEmailCommand extends EmailCommand {
    @Value("${spring.mail.username}")
    private String senderMail;

    @Autowired
    private WorkspaceVerifyEmailTemplate workspaceVerifyEmailTemplate;

    @Autowired
    private SecureTokenService secureTokenService;

    @Override
    EmailTemplate getTemplate() {
        return workspaceVerifyEmailTemplate;
    }

    @Override
    Map<String, String> getTokens(SystemUser user) throws NotFoundException {
        Map<String, String> tokens = new HashMap<>();
        tokens.put(EmailConstants.FIRSTNAME.getStr(), user.getFirstName());
        tokens.put(EmailConstants.LASTNAME.getStr(), user.getLastName());
        tokens.put(EmailConstants.VERIFY_CODE.getStr(), secureTokenService.generateTokenForSystemUser(user.getEmail()).getToken());
        return tokens;
    }

    @Override
    EmailRequest generateRequest(SystemUser user, RawEmailTemplate emailTemplate) {
        return new WorkspaceVerifyEmailRequest(senderMail, user.getEmail(), EmailConstants.VERIFY_ACCOUNT_SUBJECT.getStr(), emailTemplate.getTemplateContent());
    }

}
