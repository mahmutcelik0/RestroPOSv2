package com.restropos.systemorder.entity;

import com.restropos.systemmenu.dto.ProductDto;
import com.restropos.systemmenu.dto.ProductModifierDto;
import com.restropos.systemmenu.entity.Product;
import com.restropos.systemmenu.entity.ProductModifier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "ORDER_PRODUCTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private Product product;
    private Long quantity;
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(name = "ORDER_PRODUCT_PRODUCT_MODIFIERS",
    joinColumns = {@JoinColumn(name = "ORDER_PRODUCT_ID",referencedColumnName = "id")},
    inverseJoinColumns = {@JoinColumn(name = "PRODUCT_MODIFIER_ID",referencedColumnName = "PRODUCT_MODIFIER_ID")})
    private List<ProductModifier> productModifiers;
    private Long calculatedPrice;

}
