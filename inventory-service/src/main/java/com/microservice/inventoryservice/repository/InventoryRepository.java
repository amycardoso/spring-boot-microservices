package com.microservice.inventoryservice.repository;

import com.microservice.inventoryservice.model.Inventory;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findBySkuCode(String skuCode);
}