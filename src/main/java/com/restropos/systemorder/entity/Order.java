package com.restropos.systemorder.entity;

import com.restropos.systemmenu.entity.WorkspaceTable;
import com.restropos.systemorder.OrderStatus;
import com.restropos.systemshop.entity.user.Customer;
import com.restropos.systemshop.entity.user.SystemUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

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

    private Date orderCreationTime;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private WorkspaceTable workspaceTable;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private Customer customer;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "WAITER_ID",referencedColumnName = "USER_ID")
    private SystemUser waiter;


    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "KITCHEN_ID",referencedColumnName = "USER_ID")
    private SystemUser kitchen;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "CASH_DESK_ID",referencedColumnName = "USER_ID")
    private SystemUser cashDesk;
}
