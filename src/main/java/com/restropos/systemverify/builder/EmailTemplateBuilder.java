package com.restropos.systemverify.builder;

import com.restropos.systemverify.entity.RawEmailTemplate;

import java.util.Map;

public interface EmailTemplateBuilder {
    void build(Map<String, String> tokens, RawEmailTemplate template);
}
