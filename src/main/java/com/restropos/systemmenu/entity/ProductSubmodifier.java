package com.restropos.systemmenu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PRODUCT_SUBMODIFIERS",
        uniqueConstraints = {
                @UniqueConstraint(name = "productSubmodifierAndPriceUniqueConstraint", columnNames = {"PRODUCT_SUBMODIFIER_NAME","PRICE"})
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProductSubmodifier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_SUBMODIFIER_ID")
    private Long id;

    @Column(name = "PRODUCT_SUBMODIFIER_NAME")
    private String productSubmodifierName;

    @Column(name = "PRICE")
    private Double price;

    @ManyToMany(cascade = CascadeType.ALL,mappedBy = "productSubmodifierSet")
    private Set<ProductModifier> productModifiers = new HashSet<>();

}
