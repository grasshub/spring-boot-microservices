package com.ecomm.shipmentservice.aggregate;

import com.ecomm.axonservice.command.CancelShipmentCommand;
import com.ecomm.axonservice.command.ShipOrderCommand;
import com.ecomm.axonservice.event.OrderShippedEvent;
import com.ecomm.axonservice.event.ShipmentCancelledEvent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@NoArgsConstructor
@Slf4j
public class ShipmentAggregate {
    @AggregateIdentifier
    private String shipmentId;

    private String orderId;

    private String shipmentStatus;

    @CommandHandler
    public ShipmentAggregate(ShipOrderCommand shipOrderCommand) {
        // Validate the shipment. Publish the Order Shipped event.
        log.info("Executing ShipOrderCommand for Order Id: {} and Shipment Id: {}",
                shipOrderCommand.getOrderId(), shipOrderCommand.getShipmentId());

        OrderShippedEvent orderShippedEvent = OrderShippedEvent.builder()
                .shipmentId(shipOrderCommand.getShipmentId())
                .orderId(shipOrderCommand.getOrderId())
                .build();

        AggregateLifecycle.apply(orderShippedEvent);

        log.info("OrderShippedEvent applied");
    }

    @EventSourcingHandler
    public void on(OrderShippedEvent orderShippedEvent) {
        this.orderId = orderShippedEvent.getOrderId();
        this.shipmentId = orderShippedEvent.getShipmentId();
        this.shipmentStatus = orderShippedEvent.getShipmentStatus();
    }

    @CommandHandler
    public void handle(CancelShipmentCommand cancelShipmentCommand) {
        // Validate the shipment. Publish the CancelShipmentEvent.
        log.info("Executing cancelShipmentCommand for Order Id: {} and Shipment Id: {}",
                cancelShipmentCommand.getOrderId(), cancelShipmentCommand.getShipmentId());

        ShipmentCancelledEvent shipmentCancelledEvent = ShipmentCancelledEvent.builder()
                .shipmentId(cancelShipmentCommand.getShipmentId())
                .orderId(cancelShipmentCommand.getOrderId())
                .build();

        AggregateLifecycle.apply(shipmentCancelledEvent);

        log.info("ShipmentCancelledEvent applied");
    }

    @EventSourcingHandler
    public void on(ShipmentCancelledEvent shipmentCancelledEvent) {
        this.orderId = shipmentCancelledEvent.getOrderId();
        this.shipmentId = shipmentCancelledEvent.getShipmentId();
        this.shipmentStatus = shipmentCancelledEvent.getShipmentStatus();
    }
}
