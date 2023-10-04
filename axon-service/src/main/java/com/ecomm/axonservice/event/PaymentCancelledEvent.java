package com.ecomm.axonservice.event;

import lombok.Data;

@Data
public class PaymentCancelledEvent {
    private String paymentId;

    private String orderId;

    private String paymentStatus;
}
