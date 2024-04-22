package com.restropos.systemorder.repository;

import com.restropos.systemorder.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
