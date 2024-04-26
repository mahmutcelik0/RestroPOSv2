package com.restropos.systemorder.entity;

import com.restropos.systemmenu.entity.ProductModifier;
import com.restropos.systemmenu.entity.ProductSubmodifier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ORDER_SELECTED_MODIFIERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderSelectedModifier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_SELECTED_MODIFIER_ID")
    private Long id;

    @ManyToOne(cascade = {CascadeType.ALL})
    private OrderProduct orderProducts;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "PRODUCT_MODIFIER_ID",referencedColumnName = "PRODUCT_MODIFIER_ID")
    private ProductModifier productModifier;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(
            name = "ORDER_PRODUCT_SELECTED_PRODUCT_SUBMODIFIERS",
            joinColumns = {@JoinColumn(name = "ORDER_SELECTED_MODIFIER_ID", referencedColumnName = "ORDER_SELECTED_MODIFIER_ID")},
            inverseJoinColumns = {@JoinColumn(name = "PRODUCT_SUBMODIFIER_ID", referencedColumnName = "PRODUCT_SUBMODIFIER_ID")}
    )
    private Set<ProductSubmodifier> productSubmodifiers = new HashSet<>();

}
