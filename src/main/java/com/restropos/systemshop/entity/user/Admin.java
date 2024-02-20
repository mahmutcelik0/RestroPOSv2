package com.restropos.systemshop.entity.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue(value = "ADMIN")
@Getter
@Setter
@NoArgsConstructor
public class Admin extends SystemUser {
    public Admin(@Email @NotEmpty(message = "EMAIL IS REQUIRED FIELD") String email, @NotEmpty(message = "PASSWORD IS REQUIRED FIELD") String password, String firstName, String lastName, boolean loginDisabled,Workspace workspace) {
        super(email, password,firstName,lastName,loginDisabled,workspace);
    }
}
