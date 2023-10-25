package com.ecomm.productservice.controller;

import com.ecomm.productservice.domain.Product;
import com.ecomm.productservice.dto.ProductDTO;
import com.ecomm.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductDTO productDTO) {
        // For the safety, entity object should not be exposed as input parameter for methods with @RequestMapping
        Product product = Product.builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .build();
        Product savedProduct = productService.saveProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable String id, @RequestBody ProductDTO productDTO) {
        // For the safety, entity object should not be exposed as input parameter for methods with @RequestMapping
        Product product = Product.builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .build();
        Product updatedProduct = productService.updateProduct(id, product);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable String id) {
        return productService.getProductById(id).map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.listAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

