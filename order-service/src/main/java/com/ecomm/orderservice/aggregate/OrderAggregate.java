package com.ecomm.orderservice.aggregate;

import com.ecomm.axonservice.command.CancelOrderCommand;
import com.ecomm.axonservice.command.CompleteOrderCommand;
import com.ecomm.axonservice.event.OrderCancelledEvent;
import com.ecomm.axonservice.event.OrderCompletedEvent;
import com.ecomm.orderservice.command.CreateOrderCommand;
import com.ecomm.orderservice.event.OrderCreatedEvent;

import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
@NoArgsConstructor
public class OrderAggregate {
    @AggregateIdentifier
    private String orderId;
    private String productId;

    private String userId;

    private String addressId;

    private Integer quantity;

    private String orderStatus;

    @CommandHandler
    public OrderAggregate(CreateOrderCommand createOrderCommand) {
        // Validate the command
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        BeanUtils.copyProperties(createOrderCommand, orderCreatedEvent);
        AggregateLifecycle.apply(orderCreatedEvent);
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent orderCreatedEvent) {
        this.orderId = orderCreatedEvent.getOrderId();
        this.productId = orderCreatedEvent.getProductId();
        this.userId = orderCreatedEvent.getUserId();
        this.addressId = orderCreatedEvent.getAddressId();
        this.quantity = orderCreatedEvent.getQuantity();
        this.orderStatus = orderCreatedEvent.getOrderStatus();
    }

    @CommandHandler
    public void handle(CompleteOrderCommand completeOrderCommand) {
        // Validate the command.
        OrderCompletedEvent orderCompletedEvent = OrderCompletedEvent.builder()
                .orderId(completeOrderCommand.getOrderId())
                .orderStatus(completeOrderCommand.getOrderStatus())
                .build();

        AggregateLifecycle.apply(orderCompletedEvent);
    }

    @EventSourcingHandler
    public void on(OrderCompletedEvent orderCompletedEvent) {
        this.orderStatus = orderCompletedEvent.getOrderStatus();
    }

    @CommandHandler
    public void handle(CancelOrderCommand cancelOrderCommand) {
        // Validate the command.
        OrderCancelledEvent orderCancelledEvent = new OrderCancelledEvent();
        BeanUtils.copyProperties(cancelOrderCommand, orderCancelledEvent);

        AggregateLifecycle.apply(orderCancelledEvent);
    }

    @EventSourcingHandler
    public void on(OrderCancelledEvent orderCancelledEvent) {
        this.orderStatus = orderCancelledEvent.getOrderStatus();
    }

}
