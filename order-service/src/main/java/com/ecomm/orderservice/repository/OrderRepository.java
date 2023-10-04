package com.ecomm.orderservice.repository;

import com.ecomm.orderservice.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
}
