package com.microservice.orderservice.controller;

import com.microservice.orderservice.dto.OrderDto;
import com.microservice.orderservice.model.Order;
import com.microservice.orderservice.repository.OrderRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderRepository orderRepository;
    
	@PostMapping
	public String placeOrder(@RequestBody OrderDto orderDto) {
		Order order = new Order();
		order.setOrderLineItems(orderDto.getOrderLineItemsList());
		order.setOrderNumber(UUID.randomUUID().toString());

		orderRepository.save(order);
		return "Order Place Successfully";

	}
}
