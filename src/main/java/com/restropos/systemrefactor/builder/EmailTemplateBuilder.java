package com.restropos.systemrefactor.builder;

import com.restropos.systemrefactor.entity.RawEmailTemplate;

import java.util.Map;

public interface EmailTemplateBuilder {
    void build(Map<String, String> tokens, RawEmailTemplate template);
}
