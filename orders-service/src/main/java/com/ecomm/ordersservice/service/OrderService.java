package com.ecomm.ordersservice.service;

import com.ecomm.ordersservice.domain.Order;
import com.ecomm.ordersservice.domain.OrderItem;
import com.ecomm.ordersservice.dto.InventoryResponse;
import com.ecomm.ordersservice.dto.OrderDTO;
import com.ecomm.ordersservice.dto.OrderItemDTO;
import com.ecomm.ordersservice.event.OrderPlacedEvent;
import com.ecomm.ordersservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;

    private final WebClient.Builder webClientBuilder;

    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public String order(OrderDTO orderDTO) {
        Order order = new Order();

        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderItem> orderItemList = orderDTO.getOrderItemDTOList().stream()
                .map(this::mapFromDTO)
                .toList();
        order.setOrderItemList(orderItemList);

        List<String> skuCodeList = order.getOrderItemList().stream().map(OrderItem::getSkuCode).toList();

        // Call Inventory Service, and place the order if product is in stock.
        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodeList).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();
        boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);

        if (allProductsInStock) {
            log.info("All products are in stock");
            orderRepository.save(order);
            kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
            return "Order Placed Successfully";
        } else
            throw new IllegalArgumentException("Product is not in stock, please try again later");
    }

    private OrderItem mapFromDTO(OrderItemDTO orderItemDTO) {
        OrderItem orderItem = new OrderItem();
        orderItem.setSkuCode(orderItemDTO.getSkuCode());
        orderItem.setPrice(orderItemDTO.getPrice());
        orderItem.setQuantity(orderItemDTO.getQuantity());

        return orderItem;
    }
}
