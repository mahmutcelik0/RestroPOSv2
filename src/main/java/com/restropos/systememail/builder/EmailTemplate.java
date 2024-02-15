package com.restropos.systememail.builder;

import com.restropos.systememail.entity.RawEmailTemplate;

import java.util.Map;

public abstract class EmailTemplate {
    public RawEmailTemplate getFinalTemplate(Map<String, String> tokens) {
        return buildTemplate(tokens, getTemplate());
    }

    public abstract RawEmailTemplate getTemplate();

    public abstract EmailTemplateBuilder setBuilder();

    public RawEmailTemplate buildTemplate(Map<String, String> tokens, RawEmailTemplate template) {
        EmailTemplateBuilder builder = setBuilder();
        builder.build(tokens, template);
        return template;
    }



}
