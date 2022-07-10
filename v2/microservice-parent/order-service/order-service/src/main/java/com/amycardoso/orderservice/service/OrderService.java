package com.amycardoso.orderservice.service;

import com.amycardoso.orderservice.dto.OrderLineItemsDto;
import com.amycardoso.orderservice.dto.OrderRequest;
import com.amycardoso.orderservice.model.Order;
import com.amycardoso.orderservice.model.OrderLineItems;
import com.amycardoso.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;

    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::dtoToModel)
                .toList();

        order.setOrderLineItemsList(orderLineItems);
        orderRepository.save(order);
        return order.getOrderNumber();
    }

    private OrderLineItems dtoToModel(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}