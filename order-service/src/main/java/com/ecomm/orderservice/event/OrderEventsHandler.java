package com.ecomm.orderservice.event;

import com.ecomm.axonservice.event.OrderCancelledEvent;
import com.ecomm.axonservice.event.OrderCompletedEvent;
import com.ecomm.orderservice.domain.Order;
import com.ecomm.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderEventsHandler {
    private final OrderRepository orderRepository;

    @EventHandler
    public void on(OrderCreatedEvent orderCreatedEvent) {
        Order order = new Order();
        BeanUtils.copyProperties(orderCreatedEvent, order);
        // Set the appropriate status
        order.setOrderStatus("CREATED");
        orderRepository.save(order);
    }

    @EventHandler
    public void on(OrderCompletedEvent orderCompletedEvent) {
        Optional<Order> order = orderRepository.findById(orderCompletedEvent.getOrderId());
        if (order.isPresent()) {
            order.get().setOrderStatus("COMPLETED");
            orderRepository.save(order.get());
        }
    }

    @EventHandler
    public void on(OrderCancelledEvent orderCancelledEvent) {
        Optional<Order> order = orderRepository.findById(orderCancelledEvent.getOrderId());
        if (order.isPresent()) {
            order.get().setOrderStatus("CANCELLED");
            orderRepository.save(order.get());
        }
    }
}
