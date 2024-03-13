package com.restropos.systemshop.entity.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue(value = "WAITER")
@Getter
@Setter
@NoArgsConstructor
public class Waiter extends SystemUser {
    public Waiter(SystemUser systemUser) {
        super(systemUser.getId(),systemUser.getEmail(), systemUser.getPassword(), systemUser.getFirstName(), systemUser.getLastName(), systemUser.isLoginDisabled(), systemUser.getWorkspace(), systemUser.getRole());
    }
}
