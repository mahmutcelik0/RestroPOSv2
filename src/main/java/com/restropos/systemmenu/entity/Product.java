package com.restropos.systemmenu.entity;

import com.restropos.systemshop.entity.Image;
import com.restropos.systemshop.entity.Workspace;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "PRODUCTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ProductId.class)
public class Product {

    @Id
    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Id
    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "BUSINESS_DOMAIN",referencedColumnName = "BUSINESS_DOMAIN")
    private Workspace workspace;

    @OneToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},mappedBy = "product")
    private List<ProductModifier> productModifiers;

    private String productDescription;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "PRODUCT_IMAGE")
    private Image image;

    private Double price;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    private Category category;


}