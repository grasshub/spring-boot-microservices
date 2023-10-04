package com.ecomm.orderservice.saga;

import com.ecomm.axonservice.command.CancelOrderCommand;
import com.ecomm.axonservice.command.CancelPaymentCommand;
import com.ecomm.axonservice.command.CancelShipmentCommand;
import com.ecomm.axonservice.command.CompleteOrderCommand;
import com.ecomm.axonservice.command.ShipOrderCommand;
import com.ecomm.axonservice.command.ValidatePaymentCommand;
import com.ecomm.axonservice.event.OrderCancelledEvent;
import com.ecomm.axonservice.event.OrderCompletedEvent;
import com.ecomm.axonservice.event.OrderShippedEvent;
import com.ecomm.axonservice.event.PaymentCancelledEvent;
import com.ecomm.axonservice.event.PaymentProcessedEvent;
import com.ecomm.axonservice.event.ShipmentCancelledEvent;
import com.ecomm.axonservice.model.User;
import com.ecomm.axonservice.query.GetUserPaymentQuery;
import com.ecomm.orderservice.client.RemoteServiceClient;
import com.ecomm.orderservice.event.OrderCreatedEvent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.UUID;

@Saga
@Slf4j
@NoArgsConstructor
public class OrderProcessingSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private transient QueryGateway queryGateway;

    @Autowired
    private transient RemoteServiceClient remoteServiceClient;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent orderCreatedEvent) {
        log.info("OrderCrateEvent in Saga for Order Id : {}", orderCreatedEvent.getOrderId());

        GetUserPaymentQuery getUserPaymentQuery = new GetUserPaymentQuery(orderCreatedEvent.getUserId());

        User user = null;

        try {
            user = queryGateway.query(getUserPaymentQuery, ResponseTypes.instanceOf(User.class)).join();
        } catch (Exception exception) {
            log.error(exception.getMessage());
            // Start the compensation transaction
            cancelOrderCommand(orderCreatedEvent.getOrderId());
        }

        ValidatePaymentCommand validatePaymentCommand = ValidatePaymentCommand.builder()
                .paymentId(UUID.randomUUID().toString())
                .orderId(orderCreatedEvent.getOrderId())
                .card(user.getCard())
                .build();

        commandGateway.sendAndWait(validatePaymentCommand);
    }

    private void cancelOrderCommand(String orderId) {
        CancelOrderCommand cancelOrderCommand = CancelOrderCommand.builder()
                .orderId(orderId)
                .build();

        commandGateway.send(cancelOrderCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentProcessedEvent paymentProcessedEvent) {
        log.info("paymentProcessedEvent in Saga for Order Id : {}", paymentProcessedEvent.getOrderId());

        try {
            ShipOrderCommand shipOrderCommand = ShipOrderCommand.builder()
                    .shipmentId(UUID.randomUUID().toString())
                    .orderId(paymentProcessedEvent.getOrderId())
                    .build();

            commandGateway.send(shipOrderCommand);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            // Start the compensation transaction
            cancelPaymentCommand(paymentProcessedEvent);
        }
    }

    private void cancelPaymentCommand(PaymentProcessedEvent paymentProcessedEvent) {
        CancelPaymentCommand cancelPaymentCommand = CancelPaymentCommand.builder()
                .paymentId(paymentProcessedEvent.getPaymentId())
                .orderId(paymentProcessedEvent.getOrderId())
                .build();

        commandGateway.send(cancelPaymentCommand);
    }
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderShippedEvent orderShippedEvent) {
        log.info("orderShippedEvent in Saga for Order Id : {}", orderShippedEvent.getOrderId());

        try {
            // This block of code is used to test the chaining of compensation transactions of cancelShipmentCommand ->
            // cancelPaymentCommand -> cancelOrderCommand.
            if (true)
                throw new Exception();
            CompleteOrderCommand completeOrderCommand = CompleteOrderCommand.builder()
                    .orderId(orderShippedEvent.getOrderId())
                    .orderStatus("APPROVED")
                    .build();

            commandGateway.send(completeOrderCommand);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            // Start the compensation transaction
            cancelShipmentCommand(orderShippedEvent);
        }
    }

    private void cancelShipmentCommand(OrderShippedEvent orderShippedEvent) {
        CancelShipmentCommand cancelShipmentCommand = CancelShipmentCommand.builder()
                .shipmentId(orderShippedEvent.getShipmentId())
                .orderId(orderShippedEvent.getOrderId())
                .build();

        commandGateway.send(cancelShipmentCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    @EndSaga
    public void handle(OrderCompletedEvent orderCompletedEvent) {
        log.info("OrderCompletedEvent in Saga for Order Id : {}", orderCompletedEvent.getOrderId());
        // Dummy line to avoid single log line debugger not stopped issue
        Date now = new Date();
    }

    @SagaEventHandler(associationProperty = "orderId")
    @EndSaga
    public void handle(OrderCancelledEvent orderCancelledEvent) {
        log.info("OrderCancelledEvent in Saga for Order Id : {}", orderCancelledEvent.getOrderId());
        Date now = new Date();
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentCancelledEvent paymentCancelledEvent) {
        log.info("paymentCancelledEvent in Saga for Order Id : {}", paymentCancelledEvent.getOrderId());
        cancelOrderCommand(paymentCancelledEvent.getOrderId());
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ShipmentCancelledEvent shipmentCancelledEvent) {
        log.info("shipmentCancelledEvent in Saga for Order Id : {}", shipmentCancelledEvent.getOrderId());
        ResponseEntity<String> responseEntity = remoteServiceClient.getPaymentIdByOrderId(shipmentCancelledEvent.getOrderId());
        String paymentId = responseEntity.getBody();

        CancelPaymentCommand cancelPaymentCommand = CancelPaymentCommand.builder()
                .paymentId(paymentId)
                .orderId(shipmentCancelledEvent.getOrderId())
                .build();

        commandGateway.send(cancelPaymentCommand);
    }
}
