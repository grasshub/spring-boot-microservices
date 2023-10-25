package com.ecomm.productservice;

import com.ecomm.productservice.domain.Product;
import com.ecomm.productservice.dto.ProductDTO;
import com.ecomm.productservice.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.2");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductService productService;

    @Test
    void shouldCreateProduct() throws Exception {
        ProductDTO productDTO = getProductDTO();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated());
        assertEquals(1, productService.listAllProducts().size());

        for (Product product : productService.listAllProducts()) {
            String id = product.getId();
            mockMvc.perform(MockMvcRequestBuilders.get("/api/product/" + id))
                    .andExpect(status().isOk());
            assertEquals("Book 1", product.getName());
        }
    }

    private ProductDTO getProductDTO() {
        return ProductDTO.builder()
                .name("Book 1")
                .description("First book ordered")
                .price(BigDecimal.valueOf(25))
                .build();
    }

    @Test
    void contextLoads() {
    }

}
