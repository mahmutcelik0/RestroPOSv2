package com.restropos.systemverify.service;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemverify.constants.EmailTemplates;
import com.restropos.systemverify.entity.RawEmailTemplate;
import com.restropos.systemverify.repository.EmailTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailTemplateService {
    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    public RawEmailTemplate getEmailTemplateByEmailTemplateName(EmailTemplates emailTemplates) throws NotFoundException {
        return emailTemplateRepository.findById(emailTemplates).orElseThrow(() -> new NotFoundException(CustomResponseMessage.TEMPLATE_NOT_FOUND_EXCEPTION));
    }

    public List<RawEmailTemplate> getAllTemplates(){
        return emailTemplateRepository.findAll();
    }
}
