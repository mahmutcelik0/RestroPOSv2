package com.restropos.systemorder.entity;

import com.restropos.systemmenu.entity.Product;
import com.restropos.systemmenu.entity.ProductModifier;
import com.restropos.systemmenu.entity.ProductSubmodifier;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ORDER_PRODUCTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private Product product;

    private Long quantity;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(name = "ORDER_PRODUCT_MODIFIERS",
    joinColumns = {@JoinColumn(name = "ORDER_PRODUCT_ID",referencedColumnName = "id")},
    inverseJoinColumns = {@JoinColumn(name = "PRODUCT_MODIFIER_ID",referencedColumnName = "PRODUCT_MODIFIER_ID")})
    private Set<ProductModifier> productModifiers = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(name = "ORDER_PRODUCT_SUBMODIFIERS",
            joinColumns = {@JoinColumn(name = "ORDER_PRODUCT_ID",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "PRODUCT_SUBMODIFIER_ID",referencedColumnName = "PRODUCT_SUBMODIFIER_ID")})
    private Set<ProductSubmodifier> productSubmodifiers = new HashSet<>();


    private Long calculatedPrice;

    @ManyToOne(cascade = {CascadeType.ALL})
    private Order order;
}
