package com.restropos.systemshop.entity.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue(value = "KITCHEN")
public class Kitchen extends BasicUser{
    public Kitchen() {
    }

    public Kitchen(BasicUser basicUser) {
        super(basicUser.getEmail(),basicUser.getPassword(),basicUser.getRole(),basicUser.getDeviceName(),basicUser.getWorkspace());
    }
}
