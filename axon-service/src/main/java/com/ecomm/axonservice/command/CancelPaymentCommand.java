package com.ecomm.axonservice.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class CancelPaymentCommand {
    @TargetAggregateIdentifier
    private String paymentId;

    private String orderId;

    private String paymentStatus;
}
