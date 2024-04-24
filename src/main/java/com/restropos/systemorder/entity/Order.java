package com.restropos.systemorder.entity;

import com.restropos.systemmenu.entity.WorkspaceTable;
import com.restropos.systemorder.OrderStatus;
import com.restropos.systemorder.dto.OrderProductDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "ORDERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated
    private OrderStatus orderStatus;
    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST},mappedBy = "order")
    private List<OrderProduct> orderProducts;

    private Long totalOrderPrice;

    private LocalDateTime orderCreationTime;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private WorkspaceTable workspaceTable;

}
