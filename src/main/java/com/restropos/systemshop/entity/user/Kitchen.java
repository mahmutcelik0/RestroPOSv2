package com.restropos.systemshop.entity.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue(value = "KITCHEN")
public class Kitchen extends BasicUser{
}
