package com.restropos.systemorder.repository;

import com.restropos.systemorder.OrderStatus;
import com.restropos.systemorder.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query("select o from Order as o where o.id = ?1 and o.workspaceTable.workspace.businessDomain =?2")
    Optional<Order> findByIdAndBusinessDomain(String orderId,String businessDomain);

    @Query("select o from Order as o where o.customer.phoneNumber =?1 and o.workspaceTable.workspace.businessDomain =?2 and (o.orderStatus = ?3 or o.orderStatus = ?4)")
    List<Order> findByCustomerInfoAndBusinessDomainAndStatus(String customerInfo, String businessDomain,OrderStatus completed,OrderStatus canceled);

    @Query("select o from Order as o where o.workspaceTable.workspace.businessDomain =?1")
    List<Order> findAllBusinessDomain( String businessDomain);

}
