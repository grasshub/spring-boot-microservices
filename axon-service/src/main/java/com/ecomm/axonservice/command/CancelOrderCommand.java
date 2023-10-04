package com.ecomm.axonservice.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class CancelOrderCommand {
    @TargetAggregateIdentifier
    private String orderId;

    private String orderStatus;
}
