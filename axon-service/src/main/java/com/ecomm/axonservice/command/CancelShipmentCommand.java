package com.ecomm.axonservice.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class CancelShipmentCommand {
    @TargetAggregateIdentifier
    private String shipmentId;

    private String orderId;

    private String shipmentStatus;
}
