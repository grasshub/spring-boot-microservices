package com.ecomm.ordersservice.repository;

import com.ecomm.ordersservice.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
