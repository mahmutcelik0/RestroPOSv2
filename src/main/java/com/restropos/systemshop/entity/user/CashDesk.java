package com.restropos.systemshop.entity.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue(value = "CASH_DESK")
@NoArgsConstructor
@AllArgsConstructor
public class CashDesk extends BasicUser{
    public CashDesk(BasicUser basicUser){
        super(basicUser.getEmail(),basicUser.getPassword(),basicUser.getRole(),basicUser.getDeviceName(),basicUser.getWorkspace());
    }
}
