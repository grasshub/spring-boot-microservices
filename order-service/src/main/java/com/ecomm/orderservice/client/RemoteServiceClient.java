package com.ecomm.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "payment-service", url = "localhost:4444/api/payment")
public interface RemoteServiceClient {

    @GetMapping("/{orderId}")
    ResponseEntity<String> getPaymentIdByOrderId(@PathVariable String orderId);
}
