package com.restropos.systememail.builder;

import com.restropos.systememail.constants.EmailTemplates;
import com.restropos.systememail.entity.RawEmailTemplate;
import com.restropos.systememail.service.EmailTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkspaceVerifyEmailTemplate extends EmailTemplate {

    @Autowired
    public EmailTemplateService emailTemplateService;

    @Override
    public RawEmailTemplate getTemplate() {
        try {
            return emailTemplateService.getEmailTemplateByEmailTemplateName(EmailTemplates.WORKSPACE_VERIFY_TEMPLATE);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EmailTemplateBuilder setBuilder() {
        return new WorkspaceVerifyEmailTemplateBuilder();
    }
}
