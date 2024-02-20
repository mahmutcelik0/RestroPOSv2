package com.restropos.systemrefactor.constants;

public enum EmailTemplates {
    WORKSPACE_VERIFY_TEMPLATE("workspaceVerifyTemplate");


    private final String str;

    EmailTemplates(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }
}
