package com.ecomm.productservice.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "product")
@Getter
@Setter
@Builder
public class Product {

//    @Transient
//    public static final String SEQUENCE = "product_sequence";

    @Id
    private String id;

    private String name;
    private String description;
    private BigDecimal price;

    // Constructors, Getters, and Setters
}
