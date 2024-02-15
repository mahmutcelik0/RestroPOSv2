package com.restropos.systememail.command;

import com.restropos.systemcore.utils.LogUtil;
import com.restropos.systememail.builder.EmailTemplate;
import com.restropos.systememail.entity.RawEmailTemplate;
import com.restropos.systememail.factory.EmailTemplateFactory;
import com.restropos.systemshop.entity.user.EmailSecuredUser;
import com.restropos.systemshop.entity.user.SystemUser;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Map;

public abstract class EmailCommand {
    @Autowired
    private JavaMailSender mailSender;

    public EmailResponse sendEmail(SystemUser user) {
        RawEmailTemplate emailTemplate = EmailTemplateFactory.generateTemplate(getTemplate(), getTokens(user));
        EmailRequest emailRequest = generateRequest(user, emailTemplate);
        try {
            mailSender.send(generateMailMessage(emailRequest));
            return new EmailResponse("Mail has sent successfully!");
        } catch (MailException e) {
            LogUtil.printLog("Problem in EmailCommand", EmailCommand.class);
            return new EmailResponse(e.getMessage());
        }
    }

    protected MimeMessage generateMailMessage(EmailRequest request) {
        MimeMessage message = mailSender.createMimeMessage();

        setEmailMessage(message, request);

        return message;
    }

    abstract EmailTemplate getTemplate();

    abstract Map<String, String> getTokens(SystemUser user);

    abstract EmailRequest generateRequest(SystemUser user, RawEmailTemplate emailTemplate);

    public void setEmailMessage(MimeMessage message, EmailRequest request) {
        try {
            message.setFrom(request.getFrom());
            message.setRecipients(MimeMessage.RecipientType.TO, request.getRecipients());
            message.setSubject(request.getSubject());
            message.setContent(request.getContent(), "text/html; charset=utf-8");
        } catch (MessagingException e) {
            LogUtil.printLog("MESSAGE PROBLEM :", EmailCommand.class);
        }
    }
}
