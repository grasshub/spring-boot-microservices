package com.ecomm.productservice.service;

import com.ecomm.productservice.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Optional<Product> getProductById(Long id);

    List<Product> listAllProducts();

    Product updateProduct(Long id, Product product);

    Product saveProduct(Product product);

    void deleteProduct(Long id);
}
