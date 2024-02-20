package com.restropos.systemcore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class ResponseMessage {
    private HttpStatus status;
    private String message;
}
