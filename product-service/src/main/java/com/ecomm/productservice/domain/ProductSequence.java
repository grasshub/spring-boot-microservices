package com.ecomm.productservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "product_sequence")
@Getter
@Setter
public class ProductSequence {
    @Id
    private long id;

    private long sequence;
}
