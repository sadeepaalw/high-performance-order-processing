package com.upside.orderprocessing.service;

import com.upside.orderprocessing.model.Order;
import com.upside.orderprocessing.model.OrderStatus;
import com.upside.orderprocessing.repository.OrderRepository;
import com.upside.orderprocessing.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order testOrder;

    @BeforeEach
    void setUp() {
        testOrder = new Order();
        testOrder.setOrderNumber("TEST-001");
        testOrder.setStatus(OrderStatus.PENDING);
        testOrder.setTotalAmount(new BigDecimal("100.00"));
        testOrder.setCustomerId("CUST-001");
        testOrder.setProductId("PROD-001");
        testOrder.setQuantity(1);
    }

    @Test
    void processOrder_Success() {
        when(orderRepository.save(any(Order.class))).thenReturn(Mono.just(testOrder));

        StepVerifier.create(orderService.processOrder(testOrder))
                .expectNextMatches(order -> 
                    order.getOrderNumber().equals("TEST-001") &&
                    order.getStatus() == OrderStatus.PROCESSING)
                .verifyComplete();
    }

    @Test
    void processBatchOrders_Success() {
        Order order2 = new Order();
        order2.setOrderNumber("TEST-002");
        order2.setStatus(OrderStatus.PENDING);

        when(orderRepository.save(any(Order.class)))
                .thenReturn(Mono.just(testOrder))
                .thenReturn(Mono.just(order2));

        StepVerifier.create(orderService.processBatchOrders(Flux.just(testOrder, order2)))
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void getOrderById_Success() {
        when(orderRepository.findById(1L)).thenReturn(Mono.just(testOrder));

        StepVerifier.create(orderService.getOrderById(1L))
                .expectNext(testOrder)
                .verifyComplete();
    }

    @Test
    void getOrderById_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(orderService.getOrderById(1L))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void updateOrderStatus_Success() {
        when(orderRepository.updateOrderStatus(1L, OrderStatus.COMPLETED))
                .thenReturn(Mono.just(1));
        when(orderRepository.findById(1L))
                .thenReturn(Mono.just(testOrder));

        StepVerifier.create(orderService.updateOrderStatus(1L, OrderStatus.COMPLETED))
                .expectNext(testOrder)
                .verifyComplete();
    }

    @Test
    void getOrdersByStatus_Success() {
        when(orderRepository.findByStatus(OrderStatus.PENDING))
                .thenReturn(Flux.just(testOrder));

        StepVerifier.create(orderService.getOrdersByStatus(OrderStatus.PENDING))
                .expectNext(testOrder)
                .verifyComplete();
    }

    @Test
    void deleteOrder_Success() {
        when(orderRepository.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(orderService.deleteOrder(1L))
                .verifyComplete();
    }
} 