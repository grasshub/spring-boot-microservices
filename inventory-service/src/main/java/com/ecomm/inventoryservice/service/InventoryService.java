package com.ecomm.inventoryservice.service;

import com.ecomm.inventoryservice.dto.InventoryResponse;
import com.ecomm.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCodeList) throws InterruptedException {
        log.info("Waiting for the other task to complete");
        // Thread.sleep(10000);
        log.info("Other task completed");
        return inventoryRepository.findBySkuCodeIn(skuCodeList).stream()
                .map(inventory ->
                    InventoryResponse.builder()
                            .skuCode(inventory.getSkuCode())
                            .isInStock(inventory.getQuantity() > 0)
                            .build()
                ).toList();
    }
}
