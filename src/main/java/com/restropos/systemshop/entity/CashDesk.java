package com.restropos.systemshop.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue(value = "CASH_DESK")
public class CashDesk extends BasicUser{
}
