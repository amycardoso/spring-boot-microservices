package com.microservice.inventoryservice.controller;

import com.microservice.model.Inventory;
import com.microservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryRestController {

    private final InventoryRepository inventoryRepository;

    @GetMapping("/{skuCode}")
    Boolean isInStock(@PathVariable String skuCode) {
        log.info("Checking stock for product with sku code - " + skuCode);
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
                .orElseThrow(() -> new RuntimeException("Cannot Find Product by sku code " + skuCode));
        return inventory.getStock() > 0;
    }

}