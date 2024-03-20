package com.restropos.systemcore.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restropos.systemmenu.dto.ProductDto;
import com.restropos.systemshop.dto.CustomerDto;
import com.restropos.systemshop.dto.RegisterDto;

public class JsonUtils {
    public static RegisterDto registerDtoToJson(String text) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(text, RegisterDto.class);
    }

    public static CustomerDto customerDtoToJson(String text) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(text, CustomerDto.class);
    }

    public static ProductDto productDtoToJson(String text) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(text, ProductDto.class);
    }

    private JsonUtils(){

    }
}