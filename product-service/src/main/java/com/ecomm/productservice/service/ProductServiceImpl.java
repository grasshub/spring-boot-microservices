package com.ecomm.productservice.service;

import com.ecomm.productservice.domain.Product;
import com.ecomm.productservice.exception.ProductNotFoundException;
import com.ecomm.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    private final ProductSequenceService productSequenceService;
    @Override
    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> listAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product updateProduct(String id, Product product) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if (product.getName() != null)
            existingProduct.setName(product.getName());
        if (product.getDescription() != null)
            existingProduct.setDescription(product.getDescription());
        if (product.getPrice() != null)
            existingProduct.setPrice(product.getPrice());
        return productRepository.save(existingProduct);
    }

    @Override
    public Product saveProduct(Product product) {
      //  product.setId(productSequenceService.generateProductSequence(Product.SEQUENCE));
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }
}
