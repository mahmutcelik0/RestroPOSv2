package com.restropos.systemmenu.entity;

import com.restropos.systemshop.entity.Image;
import com.restropos.systemshop.entity.Workspace;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "PRODUCTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ProductId.class)
@Builder
public class Product {

    @Id
    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Id
    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "BUSINESS_DOMAIN",referencedColumnName = "BUSINESS_DOMAIN")
    private Workspace workspace;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "product")
    private List<ProductModifier> productModifiers;

    private String productDescription;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "PRODUCT_IMAGE")
    private Image image;

    private Double price;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    private Category category;


}