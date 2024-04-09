package com.restropos.systemmenu.entity;

import com.restropos.systemshop.entity.Workspace;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "FEATURED_GROUPS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(FeaturedGroupId.class)
public class FeaturedGroups {
    @Id
    @Column(name = "GROUP_NAME",length = 50)
    private String groupName;

    @Id
    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "BUSINESS_DOMAIN",referencedColumnName = "BUSINESS_DOMAIN")
    private Workspace workspace;


    @ManyToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinTable(name = "FEATURED_PRODUCTS",
            joinColumns = {
                @JoinColumn(name = "GROUP_NAME",referencedColumnName = "GROUP_NAME"),
                @JoinColumn(name = "GROUP_BUSINESS_DOMAIN",referencedColumnName = "BUSINESS_DOMAIN")},
            inverseJoinColumns = {
                @JoinColumn(name = "PRODUCT_NAME",referencedColumnName = "NAME"),
                @JoinColumn(name = "PRODUCT_BUSINESS_DOMAIN",referencedColumnName = "BUSINESS_DOMAIN")
            }
    )
    Set<Product> productSet = new HashSet<>();


}
