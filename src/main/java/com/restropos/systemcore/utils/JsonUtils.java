package com.restropos.systemcore.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restropos.systemshop.dto.RegisterDto;
import org.springframework.stereotype.Component;

@Component
public class JsonUtils {
    public RegisterDto textToJson(String text) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(text, RegisterDto.class);
    }
}