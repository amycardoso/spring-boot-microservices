package com.amycardoso.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amycardoso.inventoryservice.model.Inventory;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findBySkuCodeIn(List<String> skuCode);
}