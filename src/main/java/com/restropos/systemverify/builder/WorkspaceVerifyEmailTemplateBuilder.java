package com.restropos.systemverify.builder;

import com.restropos.systemcore.utils.LogUtil;
import com.restropos.systemverify.constants.EmailConstants;
import com.restropos.systemverify.entity.RawEmailTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WorkspaceVerifyEmailTemplateBuilder implements EmailTemplateBuilder {
    private final String firstName = EmailConstants.FIRSTNAME.getStr();
    private final String lastName = EmailConstants.LASTNAME.getStr();
    private final String verifyCode = EmailConstants.VERIFY_CODE.getStr();


    @Override
    public void build(Map<String, String> tokens, RawEmailTemplate template) {
        try {
            template.setTemplateContent(template.getTemplateContent().replace(firstName, tokens.get(firstName)));
            template.setTemplateContent(template.getTemplateContent().replace(lastName, tokens.get(lastName)));
            template.setTemplateContent(template.getTemplateContent().replace(verifyCode, tokens.get(verifyCode)));
        } catch (Exception e) {
            LogUtil.printLog("TOKEN DOES NOT EXIST", WorkspaceVerifyEmailTemplateBuilder.class);
        }
    }
}
