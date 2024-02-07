package com.restropos.systemshop.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue(value = "ADMIN")
@Getter
@Setter
@NoArgsConstructor
public class Admin extends SystemUser {
}
