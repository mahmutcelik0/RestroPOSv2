package com.restropos.systemmenu.entity;

import com.restropos.systemorder.entity.OrderProduct;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PRODUCT_SUBMODIFIERS",
        uniqueConstraints = {
                @UniqueConstraint(name = "productSubmodifierAndPriceUniqueConstraint", columnNames = {"PRODUCT_SUBMODIFIER_NAME","PRICE"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSubmodifier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_SUBMODIFIER_ID")
    private Long id;

    @Column(name = "PRODUCT_SUBMODIFIER_NAME")
    private String productSubmodifierName;

    @Column(name = "PRICE")
    private Double price;

    @ManyToMany(cascade = {CascadeType.ALL},mappedBy = "productSubmodifierSet")
    private Set<ProductModifier> productModifiers = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.ALL},mappedBy = "productSubmodifiers")
    private Set<OrderProduct> orderProducts = new HashSet<>();

    public ProductSubmodifier(String productSubmodifierName, Double price) {
        this.productSubmodifierName = productSubmodifierName;
        this.price = price;
    }
}
