package com.ecomm.axonservice.command;

import com.ecomm.axonservice.model.Card;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class ValidatePaymentCommand {
    @TargetAggregateIdentifier
    private String paymentId;

    private String orderId;

    private Card card;
}
