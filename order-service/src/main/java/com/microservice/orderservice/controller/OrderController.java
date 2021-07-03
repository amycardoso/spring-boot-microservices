package com.microservice.orderservice.controller;

import com.microservice.orderservice.client.InventoryClient;
import com.microservice.orderservice.dto.OrderDto;
import com.microservice.orderservice.model.Order;
import com.microservice.orderservice.repository.OrderRepository;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreaker;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
	private final OrderRepository orderRepository;
	private final InventoryClient inventoryClient;
	private final Resilience4JCircuitBreakerFactory circuitBreakerFactory;

	@PostMapping
	public String placeOrder(@RequestBody OrderDto orderDto) {
		Resilience4JCircuitBreaker circuitBreaker = circuitBreakerFactory.create("inventory");
		Supplier<Boolean> booleanSupplier = () -> orderDto.getOrderLineItemsList().stream()
				.allMatch(lineItem -> {
					log.info("Making Call to Inventory Service for SkuCode {}", lineItem.getSkuCode());
					return inventoryClient.checkStock(lineItem.getSkuCode());
				});
		boolean productsInStock = circuitBreaker.run(booleanSupplier, throwable -> handleErrorCase());

		if (productsInStock) {
			Order order = new Order();
			order.setOrderLineItems(orderDto.getOrderLineItemsList());
			order.setOrderNumber(UUID.randomUUID().toString());

			orderRepository.save(order);
			log.info("Sending Order Details with Order Id {} to Notification Service", order.getId());
			return "Order Place Successfully";
		} else {
			return "Order Failed - One of the Product in your Order is out of stock";
		}
	}

	private Boolean handleErrorCase() {
		return false;
	}
}
