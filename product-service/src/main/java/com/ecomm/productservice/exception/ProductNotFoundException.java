package com.ecomm.productservice.exception;

import java.io.Serial;

public class ProductNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ProductNotFoundException(long id) {
        super("Product with id " + id + " not found");
    }
}
