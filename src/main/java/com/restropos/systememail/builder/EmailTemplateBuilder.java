package com.restropos.systememail.builder;

import com.restropos.systememail.entity.RawEmailTemplate;

import java.util.Map;

public interface EmailTemplateBuilder {
    void build(Map<String, String> tokens, RawEmailTemplate template);
}
