package com.ecomm.axonservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Card {
    private String name;

    private String cardNumber;

    private Integer validUntilMonth;

    private Integer validUntilYear;

    private Integer cvv;
}
