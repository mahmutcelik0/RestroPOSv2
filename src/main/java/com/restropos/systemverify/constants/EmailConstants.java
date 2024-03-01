package com.restropos.systemverify.constants;


public enum EmailConstants {
    FIRSTNAME("${FIRSTNAME}"),
    LASTNAME("${LASTNAME}"),
    VERIFY_ACCOUNT_SUBJECT("VERIFY ACCOUNT - RestroPOS"),
    VERIFY_CODE("{VERIFY_CODE}");

    private final String str;

    EmailConstants(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }
}
