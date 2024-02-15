package com.restropos.systememail.factory;

import com.restropos.systememail.builder.EmailTemplate;
import com.restropos.systememail.entity.RawEmailTemplate;

import java.util.Map;

public class EmailTemplateFactory {

    public static RawEmailTemplate generateTemplate(EmailTemplate templateType, Map<String, String> tokens) {
        return templateType.getFinalTemplate(tokens);
    }

    private EmailTemplateFactory(){

    }
}
