package com.ecomm.notificationservice.service;

import com.ecomm.notificationservice.event.OrderPlacedEvent;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final ObservationRegistry observationRegistry;

    private final Tracer tracer;

    @KafkaListener(topics = "notificationTopic")
    public void handle(OrderPlacedEvent orderPlacedEvent) {
        Observation.createNotStarted("on-message", this.observationRegistry).observe(() -> {
           log.info("Got message <{}>", orderPlacedEvent);
           log.info("TraceId- {}, Received Notification for Order - {}",
                   Objects.requireNonNull(this.tracer.currentSpan()).context().traceId(),
                   orderPlacedEvent.getOrderNumber());
        });
        // Send out an email notification
    }
}
