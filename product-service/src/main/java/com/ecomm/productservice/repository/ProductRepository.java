package com.ecomm.productservice.repository;

import com.ecomm.productservice.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}