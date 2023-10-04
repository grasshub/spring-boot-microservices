package com.ecomm.orderservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "customer_order")
public class Order {
    @Id
    private String orderId;
    private String productId;

    private String userId;

    private String addressId;

    private Integer quantity;

    private String orderStatus;

}
