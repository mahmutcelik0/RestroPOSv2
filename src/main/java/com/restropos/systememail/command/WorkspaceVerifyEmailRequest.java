package com.restropos.systememail.command;


public class WorkspaceVerifyEmailRequest extends EmailRequest {
    public WorkspaceVerifyEmailRequest(String from, String recipients, String subject, String content) {
        super(from, recipients, subject, content);
    }
}
