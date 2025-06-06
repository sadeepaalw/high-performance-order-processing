package com.upside.orderprocessing.service.impl;

import com.upside.orderprocessing.model.Order;
import com.upside.orderprocessing.model.OrderStatus;
import com.upside.orderprocessing.repository.OrderRepository;
import com.upside.orderprocessing.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    public Mono<Order> processOrder(Order order) {
        return Mono.just(order)
                .doOnNext(o -> o.setStatus(OrderStatus.PROCESSING))
                .flatMap(orderRepository::save)
                .doOnSuccess(savedOrder -> log.debug("Processed order: {}", savedOrder.getOrderNumber()))
                .doOnError(e -> log.error("Error processing order: {}", order.getOrderNumber(), e));
    }

    @Override
    public Flux<Order> processBatchOrders(Flux<Order> orders) {
        return orders
                .doOnNext(order -> order.setStatus(OrderStatus.PROCESSING))
                .flatMap(orderRepository::save)
                .doOnNext(savedOrder -> log.debug("Processed order: {}", savedOrder.getOrderNumber()))
                .doOnError(e -> log.error("Error processing batch orders", e));
    }

    @Override
    @Cacheable(value = "orders", key = "#id")
    public Mono<Order> getOrderById(Long id) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Order not found: " + id)));
    }

    @Override
    @Cacheable(value = "orders", key = "#orderNumber")
    public Mono<Order> getOrderByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .switchIfEmpty(Mono.error(new RuntimeException("Order not found: " + orderNumber)));
    }

    @Override
    @CacheEvict(value = "orders", key = "#id")
    public Mono<Order> updateOrderStatus(Long id, OrderStatus status) {
        return orderRepository.updateOrderStatus(id, status)
                .flatMap(updated -> {
                    if (updated == 0) {
                        return Mono.error(new RuntimeException("Order not found: " + id));
                    }
                    return getOrderById(id);
                });
    }

    @Override
    public Flux<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    @Override
    @CacheEvict(value = "orders", key = "#id")
    public Mono<Void> deleteOrder(Long id) {
        return orderRepository.deleteById(id);
    }
} 