package com.restropos.systemrefactor.builder;

import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemrefactor.constants.EmailTemplates;
import com.restropos.systemrefactor.entity.RawEmailTemplate;
import com.restropos.systemrefactor.service.EmailTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkspaceVerifyEmailTemplate extends EmailTemplate {

    @Autowired
    public EmailTemplateService emailTemplateService;

    @Override
    public RawEmailTemplate getTemplate() throws NotFoundException {
        return emailTemplateService.getEmailTemplateByEmailTemplateName(EmailTemplates.WORKSPACE_VERIFY_TEMPLATE);
    }

    @Override
    public EmailTemplateBuilder setBuilder() {
        return new WorkspaceVerifyEmailTemplateBuilder();
    }
}
