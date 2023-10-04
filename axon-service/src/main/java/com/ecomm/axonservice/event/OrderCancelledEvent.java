package com.ecomm.axonservice.event;

import lombok.Data;

@Data
public class OrderCancelledEvent {
    private String orderId;

    private String orderStatus;
}
