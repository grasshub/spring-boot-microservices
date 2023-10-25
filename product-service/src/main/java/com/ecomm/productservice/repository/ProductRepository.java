package com.ecomm.productservice.repository;

import com.ecomm.productservice.domain.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}