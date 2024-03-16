package com.restropos.systemmenu.entity;

import com.restropos.systemshop.entity.Workspace;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class ProductId implements Serializable {
    @Column(name = "PRODUCT_NAME")
    private String productName;
    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "BUSINESS_DOMAIN",referencedColumnName = "BUSINESS_DOMAIN")
    private Workspace workspace;
}
