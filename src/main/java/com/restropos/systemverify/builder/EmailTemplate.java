package com.restropos.systemverify.builder;

import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemverify.entity.RawEmailTemplate;

import java.util.Map;

public abstract class EmailTemplate {
    public RawEmailTemplate getFinalTemplate(Map<String, String> tokens) throws NotFoundException {
        return buildTemplate(tokens, getTemplate());
    }

    public abstract RawEmailTemplate getTemplate() throws NotFoundException;

    public abstract EmailTemplateBuilder setBuilder();

    public RawEmailTemplate buildTemplate(Map<String, String> tokens, RawEmailTemplate template) {
        EmailTemplateBuilder builder = setBuilder();
        builder.build(tokens, template);
        return template;
    }



}
