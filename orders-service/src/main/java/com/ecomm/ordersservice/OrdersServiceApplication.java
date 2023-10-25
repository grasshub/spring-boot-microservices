package com.ecomm.ordersservice;

import com.ecomm.ordersservice.dto.OrderDTO;
import com.ecomm.ordersservice.dto.OrderItemDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.IntStream;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class OrdersServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrdersServiceApplication.class, args);

//        // Create an instance of OrderDTO and populate it with data
//        OrderDTO orderDTO = new OrderDTO();
//        OrderItemDTO item = OrderItemDTO.builder()
//                .skuCode("Book_1")
//                .price(BigDecimal.TEN)
//                .quantity(1)
//                .build();
//
//        orderDTO.setOrderItemDTOList(Collections.singletonList(item));
//
//        IntStream.range(0, 80).parallel().forEach(i -> {
//            String response = new RestTemplate().postForObject("http://localhost:8888/api/order",
//                    orderDTO, String.class);
//            log.info(Objects.requireNonNull(response));
//        });
    }
}
