package com.restropos.systemrefactor.factory;

import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemrefactor.builder.EmailTemplate;
import com.restropos.systemrefactor.entity.RawEmailTemplate;

import java.util.Map;

public class EmailTemplateFactory {

    public static RawEmailTemplate generateTemplate(EmailTemplate templateType, Map<String, String> tokens) throws NotFoundException {
        return templateType.getFinalTemplate(tokens);
    }

    private EmailTemplateFactory(){

    }
}
