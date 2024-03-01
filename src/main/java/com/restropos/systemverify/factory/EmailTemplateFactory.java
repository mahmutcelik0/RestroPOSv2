package com.restropos.systemverify.factory;

import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemverify.builder.EmailTemplate;
import com.restropos.systemverify.entity.RawEmailTemplate;

import java.util.Map;

public class EmailTemplateFactory {

    public static RawEmailTemplate generateTemplate(EmailTemplate templateType, Map<String, String> tokens) throws NotFoundException {
        return templateType.getFinalTemplate(tokens);
    }

    private EmailTemplateFactory(){

    }
}
