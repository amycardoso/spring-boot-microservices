package com.amycardoso.orderservice.repository;

import com.amycardoso.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}