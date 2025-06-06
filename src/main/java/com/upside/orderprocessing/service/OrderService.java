package com.upside.orderprocessing.service;

import com.upside.orderprocessing.model.Order;
import com.upside.orderprocessing.model.OrderStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderService {
    Mono<Order> processOrder(Order order);
    
    Flux<Order> processBatchOrders(Flux<Order> orders);
    
    Mono<Order> getOrderById(Long id);
    
    Mono<Order> getOrderByOrderNumber(String orderNumber);
    
    Mono<Order> updateOrderStatus(Long id, OrderStatus status);
    
    Flux<Order> getOrdersByStatus(OrderStatus status);
    
    Mono<Void> deleteOrder(Long id);
} 