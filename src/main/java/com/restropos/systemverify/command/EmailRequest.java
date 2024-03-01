package com.restropos.systemverify.command;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EmailRequest {
    protected String from;
    protected String recipients;
    protected String subject;
    protected String content;
}
