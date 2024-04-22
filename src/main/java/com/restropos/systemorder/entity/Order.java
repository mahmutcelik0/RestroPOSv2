package com.restropos.systemorder.entity;

import com.restropos.systemorder.OrderStatus;
import com.restropos.systemorder.dto.OrderProductDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "ORDERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated
    private OrderStatus orderStatus;
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(name = "ORDER_ORDER_PRODUCTS",
    joinColumns = {@JoinColumn(name = "ORDER_ID", referencedColumnName = "id")},
    inverseJoinColumns = {@JoinColumn(name = "ORDER_PRODUCT_ID", referencedColumnName = "id")})
    private List<OrderProduct> orderProducts;
    private Long totalOrderPrice;

}
