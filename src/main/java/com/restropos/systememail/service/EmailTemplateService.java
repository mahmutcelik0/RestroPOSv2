package com.restropos.systememail.service;

import com.restropos.systememail.constants.EmailTemplates;
import com.restropos.systememail.entity.RawEmailTemplate;
import com.restropos.systememail.repository.EmailTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailTemplateService {
    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    public RawEmailTemplate getEmailTemplateByEmailTemplateName(EmailTemplates emailTemplates) throws RuntimeException {
        return emailTemplateRepository.findById(emailTemplates).orElseThrow(() -> new RuntimeException("TemplateNotFound"));
    }

    public List<RawEmailTemplate> getAllTemplates(){
        return emailTemplateRepository.findAll();
    }
}
