package com.ecomm.paymentservice.repository;

import com.ecomm.paymentservice.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    Payment findByOrderId(String orderId);
}
