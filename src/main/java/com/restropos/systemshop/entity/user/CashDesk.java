package com.restropos.systemshop.entity.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue(value = "CASH_DESK")
@Getter
@Setter
@NoArgsConstructor
public class CashDesk extends SystemUser{
    public CashDesk(SystemUser systemUser) {
        super(systemUser.getId(),systemUser.getEmail(), systemUser.getPassword(), systemUser.getFirstName(), systemUser.getLastName(), systemUser.isLoginDisabled(), systemUser.getWorkspace(), systemUser.getRole());
    }
}
