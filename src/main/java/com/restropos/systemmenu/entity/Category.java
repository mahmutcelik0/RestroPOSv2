package com.restropos.systemmenu.entity;

import com.restropos.systemshop.entity.Image;
import com.restropos.systemshop.entity.Workspace;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "CATEGORIES")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "BUSINESS_DOMAIN",referencedColumnName = "BUSINESS_DOMAIN")
    private Workspace workspace;

    @OneToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.REMOVE})
    @JoinColumn(name = "IMAGE_NAME")
    private Image image;

    private String categoryTitle;

    @OneToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.REMOVE},mappedBy = "category")
    private List<Product> products;
}