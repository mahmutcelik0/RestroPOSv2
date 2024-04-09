package com.restropos.systemmenu.entity;

import com.restropos.systemshop.entity.Image;
import com.restropos.systemshop.entity.Workspace;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "PRODUCTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(GenericId.class)
@Builder
public class Product {

    @Id
    @Column(name = "NAME",length = 50)
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

    @ManyToMany(cascade = CascadeType.ALL,mappedBy = "productSet")
    private Set<FeaturedGroups> featuredGroups = new HashSet<>();


}