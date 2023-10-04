package com.ecomm.paymentservice.controller;

import com.ecomm.paymentservice.domain.Payment;
import com.ecomm.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentRepository paymentRepository;

    @GetMapping("/{orderId}")
    public ResponseEntity<String> getUserPayment(@PathVariable String orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId);

        return ResponseEntity.ok(payment.getPaymentId());
    }
}
