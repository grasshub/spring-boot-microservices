package com.ecomm.productservice.exception;

import java.io.Serial;

public class ProductNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ProductNotFoundException(String id) {
        super("Product with id " + id + " not found");
    }
}
