package com.restropos.systemorder.entity;

import com.restropos.systemmenu.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ORDER_PRODUCTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private Product product;

    private Long quantity;

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST},mappedBy = "orderProducts")
    private List<OrderSelectedModifier> orderSelectedModifiers = new ArrayList<>();

    private Long calculatedPrice;

    @ManyToOne(cascade = {CascadeType.ALL})
    private Order order;
}
