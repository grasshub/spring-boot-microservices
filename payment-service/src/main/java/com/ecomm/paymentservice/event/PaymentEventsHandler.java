package com.ecomm.paymentservice.event;

import com.ecomm.axonservice.event.PaymentCancelledEvent;
import com.ecomm.axonservice.event.PaymentProcessedEvent;
import com.ecomm.paymentservice.domain.Payment;
import com.ecomm.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PaymentEventsHandler {

    private final PaymentRepository paymentRepository;

    @EventHandler
    public void on(PaymentProcessedEvent paymentProcessedEvent) {
        Payment payment = Payment.builder()
                .paymentId(paymentProcessedEvent.getPaymentId())
                .orderId(paymentProcessedEvent.getOrderId())
                .paymentStatus("COMPLETED")
                .build();

        paymentRepository.save(payment);
    }

    @EventHandler
    public void on(PaymentCancelledEvent paymentCancelledEvent) {
        Optional<Payment> payment = paymentRepository.findById(paymentCancelledEvent.getPaymentId());

        if (payment.isPresent()) {
            payment.get().setPaymentStatus("CANCELLED");
            paymentRepository.save(payment.get());
        }
    }
}
