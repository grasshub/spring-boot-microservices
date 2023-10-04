package com.ecomm.paymentservice.aggregate;

import com.ecomm.axonservice.command.CancelPaymentCommand;
import com.ecomm.axonservice.command.ValidatePaymentCommand;
import com.ecomm.axonservice.event.PaymentCancelledEvent;
import com.ecomm.axonservice.event.PaymentProcessedEvent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
@Slf4j
@NoArgsConstructor
public class PaymentAggregate {
    @AggregateIdentifier
    private String paymentId;

    private String orderId;

    private String paymentStatus;

    @CommandHandler
    public PaymentAggregate(ValidatePaymentCommand validatePaymentCommand) {
        // Validate the payment. Publish the payment processed event.
        log.info("Executing ValidatePaymentCommand for Order Id: {} and Payment Id: {}",
                validatePaymentCommand.getOrderId(), validatePaymentCommand.getPaymentId());

        PaymentProcessedEvent paymentProcessedEvent = new PaymentProcessedEvent(
                validatePaymentCommand.getPaymentId(), validatePaymentCommand.getOrderId());

        AggregateLifecycle.apply(paymentProcessedEvent);

        log.info("PaymentProcessedEvent applied");
    }

    @EventSourcingHandler
    public void on(PaymentProcessedEvent paymentProcessedEvent) {
        this.paymentId = paymentProcessedEvent.getPaymentId();
        this.orderId = paymentProcessedEvent.getPaymentId();
    }

    @CommandHandler
    public void handle(CancelPaymentCommand cancelPaymentCommand) {
        log.info("Executing CancelPaymentCommand for Order Id: {} and Payment Id: {}",
                cancelPaymentCommand.getOrderId(), cancelPaymentCommand.getPaymentId());
        PaymentCancelledEvent paymentCancelledEvent = new PaymentCancelledEvent();
        BeanUtils.copyProperties(cancelPaymentCommand, paymentCancelledEvent);

        AggregateLifecycle.apply(paymentCancelledEvent);

        log.info("CancelPaymentCommand applied");
    }

    @EventSourcingHandler
    public void on(PaymentCancelledEvent paymentCancelledEvent) {
        this.paymentStatus = paymentCancelledEvent.getPaymentStatus();
    }

}
