package com.restropos.systemshop.entity.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue(value = "KITCHEN")
@NoArgsConstructor
@Getter
@Setter
public class Kitchen extends SystemUser{
    public Kitchen(SystemUser systemUser) {
        super(systemUser.getId(),systemUser.getEmail(), systemUser.getPassword(), systemUser.getFirstName(), null, systemUser.isLoginDisabled(), systemUser.getWorkspace(), systemUser.getRole());
    }
}