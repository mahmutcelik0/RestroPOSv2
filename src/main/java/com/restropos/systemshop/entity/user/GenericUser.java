package com.restropos.systemshop.entity.user;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class GenericUser {
    abstract boolean isSecured();




}
