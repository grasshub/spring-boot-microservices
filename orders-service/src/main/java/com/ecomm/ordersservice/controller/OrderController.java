package com.ecomm.ordersservice.controller;

import com.ecomm.ordersservice.dto.OrderDTO;
import com.ecomm.ordersservice.service.OrderService;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackForInventory")
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
//    @Bulkhead(name = "inventory", fallbackMethod = "fallbackInventoryBulkhead")
    public CompletableFuture<ResponseEntity<String>> order(@RequestBody OrderDTO orderDTO) {
        return CompletableFuture.supplyAsync(() -> ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.order(orderDTO)));
    }

    public CompletableFuture<ResponseEntity<String>> fallbackForInventory(OrderDTO orderDTO,
                                                                          CallNotPermittedException callNotPermittedException) {
        return CompletableFuture.supplyAsync(() -> ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("The system facing issues to order, please try to order later"));
    }

    public CompletableFuture<ResponseEntity<String>> fallbackInventoryBulkhead(OrderDTO orderDTO,
                                                                               BulkheadFullException bulkheadFullException) {
        return CompletableFuture.supplyAsync(() -> ResponseEntity.status(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED)
                .body("Bulkhead Too many request - No further calls are accepted"));
    }
}
