package com.ecomm.axonservice.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShipmentCancelledEvent {
    private String shipmentId;

    private String orderId;

    private String shipmentStatus;
}
