package com.ecomm.shipmentservice.repository;

import com.ecomm.shipmentservice.domain.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, String> {
}
