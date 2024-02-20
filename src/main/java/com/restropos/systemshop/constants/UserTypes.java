package com.restropos.systemshop.constants;

public enum UserTypes {
    ADMIN("ADMIN"),
    CASH_DESK("CASH_DESK"),
    CUSTOMER("CUSTOMER"),
    KITCHEN("KITCHEN"),
    WAITER("WAITER");


    private final String name;

    UserTypes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
