package com.restropos.systemmenu.entity;

import com.restropos.systemmenu.constants.ChoiceEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PRODUCT_MODIFIERS",
        uniqueConstraints =
                {
                        @UniqueConstraint(name = "uniqueModifierSubmodifierPrice",columnNames =
                                {"PRODUCT_MODIFIER_NAME", "PRODUCT_NAME","BUSINESS_DOMAIN"}
                        )
                }
        )
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProductModifier implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_MODIFIER_ID")
    private Long id;

    @Column(name = "PRODUCT_MODIFIER_NAME")
    private String productModifierName;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumns(
            {
                    @JoinColumn(name = "PRODUCT_NAME", referencedColumnName = "PRODUCT_NAME"),
                    @JoinColumn(name = "BUSINESS_DOMAIN", referencedColumnName = "BUSINESS_DOMAIN")
            }
    )
    private Product product;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "MODIFIERS_SUBMODIFIERS",
            joinColumns = {@JoinColumn(name = "PRODUCT_MODIFIER_ID",referencedColumnName = "PRODUCT_MODIFIER_ID")},
            inverseJoinColumns = {@JoinColumn(name = "PRODUCT_SUBMODIFIERS_ID",referencedColumnName = "PRODUCT_SUBMODIFIER_ID")}
    )
    private Set<ProductSubmodifier> productSubmodifierSet = new HashSet<>();

    private boolean isRequired;

    @Enumerated
    private ChoiceEnum choice;


}
