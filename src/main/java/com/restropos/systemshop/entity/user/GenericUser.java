package com.restropos.systemshop.entity.user;

import com.restropos.systemshop.entity.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class GenericUser {
    abstract boolean isSecured();

    @OneToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ROLE",referencedColumnName = "ROLE_NAME")
    private Role role;

}
