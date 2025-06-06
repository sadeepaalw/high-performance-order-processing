package com.upside.orderprocessing.repository;

import com.upside.orderprocessing.model.Order;
import com.upside.orderprocessing.model.OrderStatus;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {
    Mono<Order> findByOrderNumber(String orderNumber);
    
    Flux<Order> findByStatus(OrderStatus status);
    
    @Modifying
    @Query("UPDATE orders SET status = :status WHERE id = :id")
    Mono<Integer> updateOrderStatus(Long id, OrderStatus status);
    
    @Query("SELECT * FROM orders WHERE status = :status AND created_at >= :startTime")
    Flux<Order> findRecentOrdersByStatus(OrderStatus status, LocalDateTime startTime);
} 