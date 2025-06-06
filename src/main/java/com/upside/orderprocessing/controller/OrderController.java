package com.upside.orderprocessing.controller;

import com.upside.orderprocessing.model.Order;
import com.upside.orderprocessing.model.OrderStatus;
import com.upside.orderprocessing.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final AtomicInteger activeStressTests = new AtomicInteger(0);

    @PostMapping("/batch")
    public Flux<Order> processBatchOrders(@RequestBody Flux<Order> orders) {
        return orderService.processBatchOrders(orders);
    }

    @GetMapping("/{id}")
    public Mono<Order> getOrder(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @PutMapping("/{id}/status")
    public Mono<Order> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        return orderService.updateOrderStatus(id, status);
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Order> getOrderStream(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false, defaultValue = "0") int limit) {
        Flux<Order> orders = orderService.getOrdersByStatus(status);
        if (limit > 0) {
            orders = orders.take(limit);
        }
        return orders;
    }

    @PostMapping("/stress-test")
    public Mono<Map<String, Object>> stressTest(
            @RequestParam int orderCount,
            @RequestParam(required = false, defaultValue = "100") int batchSize) {
        if (orderCount > 10000) {
            return Mono.just(Map.of(
                "error", "Maximum 10,000 orders allowed for stress test",
                "status", "rejected"
            ));
        }

        if (activeStressTests.incrementAndGet() > 1) {
            activeStressTests.decrementAndGet();
            return Mono.just(Map.of(
                "error", "Another stress test is already running",
                "status", "rejected"
            ));
        }

        Instant startTime = Instant.now();
        AtomicInteger processedCount = new AtomicInteger(0);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        return Flux.range(1, orderCount)
                .map(i -> createTestOrder())
                .buffer(batchSize)
                .flatMap(batch -> orderService.processBatchOrders(Flux.fromIterable(batch))
                        .doOnNext(order -> {
                            processedCount.incrementAndGet();
                            if (order.getStatus() == OrderStatus.PROCESSING) {
                                successCount.incrementAndGet();
                            } else {
                                failureCount.incrementAndGet();
                            }
                        })
                        .onErrorResume(e -> {
                            log.error("Error processing batch", e);
                            failureCount.addAndGet(batch.size());
                            return Mono.empty();
                        }))
                .subscribeOn(Schedulers.boundedElastic())
                .doFinally(signalType -> {
                    activeStressTests.decrementAndGet();
                    Duration duration = Duration.between(startTime, Instant.now());
                    log.info("Stress test completed: processed={}, success={}, failure={}, duration={}",
                            processedCount.get(), successCount.get(), failureCount.get(), duration);
                })
                .then(Mono.defer(() -> {
                    Duration duration = Duration.between(startTime, Instant.now());
                    Map<String, Object> results = new HashMap<>();
                    results.put("status", "completed");
                    results.put("totalOrders", orderCount);
                    results.put("processedOrders", processedCount.get());
                    results.put("successfulOrders", successCount.get());
                    results.put("failedOrders", failureCount.get());
                    results.put("durationMillis", duration.toMillis());
                    results.put("throughput", calculateThroughput(processedCount.get(), duration));
                    return Mono.just(results);
                }));
    }

    private Order createTestOrder() {
        Order order = new Order();
        order.setOrderNumber("STRESS-" + UUID.randomUUID().toString());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(new BigDecimal("100.00"));
        order.setCustomerId("CUST-" + UUID.randomUUID().toString().substring(0, 8));
        order.setProductId("PROD-" + UUID.randomUUID().toString().substring(0, 8));
        order.setQuantity(1);
        return order;
    }

    private double calculateThroughput(int processedOrders, Duration duration) {
        return duration.toMillis() > 0 ? 
            (double) processedOrders / duration.toMillis() * 1000 : 0;
    }
} 