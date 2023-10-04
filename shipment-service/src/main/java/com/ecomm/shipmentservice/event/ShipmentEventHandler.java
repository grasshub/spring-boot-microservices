package com.ecomm.shipmentservice.event;

import com.ecomm.axonservice.event.OrderShippedEvent;
import com.ecomm.axonservice.event.ShipmentCancelledEvent;
import com.ecomm.shipmentservice.domain.Shipment;
import com.ecomm.shipmentservice.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ShipmentEventHandler {

    private final ShipmentRepository shipmentRepository;

    @EventHandler
    public void on(OrderShippedEvent orderShippedEvent) {
        Shipment shipment = new Shipment();
        BeanUtils.copyProperties(orderShippedEvent, shipment);
        shipment.setShipmentStatus("COMPLETED");
        shipmentRepository.save(shipment);
    }

    @EventHandler
    public void on(ShipmentCancelledEvent shipmentCancelledEvent) {
        Optional<Shipment> shipment = shipmentRepository.findById(shipmentCancelledEvent.getShipmentId());

        if (shipment.isPresent()) {
            shipment.get().setShipmentStatus("CANCELLED");
            shipmentRepository.save(shipment.get());
        }
    }
}
