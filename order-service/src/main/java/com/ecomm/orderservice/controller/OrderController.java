package com.ecomm.orderservice.controller;

import com.ecomm.orderservice.command.CreateOrderCommand;
import com.ecomm.orderservice.model.OrderRestModel;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final CommandGateway commandGateway;

    @PostMapping
    public String createOrder(@RequestBody OrderRestModel orderRestModel) {
        String orderId = UUID.randomUUID().toString();

        CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
                .orderId(orderId)
                .productId(orderRestModel.getProductId())
                .userId(orderRestModel.getUserId())
                .addressId(orderRestModel.getAddressId())
                .quantity(orderRestModel.getQuantity())
                .orderStatus("CREATED")
                .build();

        commandGateway.sendAndWait(createOrderCommand);

        return "Order Created";
    }
}
