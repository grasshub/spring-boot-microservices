package com.ecomm.ordersservice.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemDTO {
    private Long id;

    private String skuCode;

    private BigDecimal price;

    private Integer quantity;
}
