package com.restropos.systememail.command;

import com.restropos.systemcore.service.SecureTokenService;
import com.restropos.systememail.builder.EmailTemplate;
import com.restropos.systememail.builder.WorkspaceVerifyEmailTemplate;
import com.restropos.systememail.constants.EmailConstants;
import com.restropos.systememail.entity.RawEmailTemplate;
import com.restropos.systemshop.entity.user.SystemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WorkspaceVerifyEmailCommand extends EmailCommand {
    @Autowired
    private WorkspaceVerifyEmailTemplate workspaceVerifyEmailTemplate;

    @Autowired
    private SecureTokenService secureTokenService;

    @Override
    EmailTemplate getTemplate() {
        return workspaceVerifyEmailTemplate;
    }

    @Override
    Map<String, String> getTokens(SystemUser user) {
        Map<String, String> tokens = new HashMap<>();
        tokens.put(EmailConstants.FIRSTNAME.getStr(), user.getFirstName());
        tokens.put(EmailConstants.LASTNAME.getStr(), user.getLastName());
        tokens.put(EmailConstants.VERIFY_CODE.getStr(), secureTokenService.generateTokenForSystemUser(user.getEmail()).getToken());
        return tokens;
    }

    @Override
    EmailRequest generateRequest(SystemUser user, RawEmailTemplate emailTemplate) {
        return new WorkspaceVerifyEmailRequest("mahmutcelik1618@gmail.com", user.getEmail(), EmailConstants.VERIFY_ACCOUNT_SUBJECT.getStr(), emailTemplate.getTemplateContent()); // TODO: 8/23/2023 FROM PART WILL CHANGE
    }

}
