package com.ecomm.ordersservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDTO {

    private List<OrderItemDTO> orderItemDTOList;
}
