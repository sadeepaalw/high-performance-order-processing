package com.upside.orderprocessing.controller;

import com.upside.orderprocessing.model.Order;
import com.upside.orderprocessing.model.OrderStatus;
import com.upside.orderprocessing.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private OrderService orderService;

    private Order testOrder;

    @Test
    void processBatchOrders_Success() {
        testOrder = createTestOrder();
        when(orderService.processBatchOrders(any(Flux.class)))
                .thenReturn(Flux.just(testOrder));

        webTestClient.post()
                .uri("/api/orders/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("[{\"orderNumber\":\"TEST-001\"}]")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Order.class)
                .hasSize(1);
    }

    @Test
    void getOrder_Success() {
        testOrder = createTestOrder();
        when(orderService.getOrderById(1L))
                .thenReturn(Mono.just(testOrder));

        webTestClient.get()
                .uri("/api/orders/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.orderNumber").isEqualTo("TEST-001");
    }

    @Test
    void updateOrderStatus_Success() {
        testOrder = createTestOrder();
        when(orderService.updateOrderStatus(1L, OrderStatus.COMPLETED))
                .thenReturn(Mono.just(testOrder));

        webTestClient.put()
                .uri("/api/orders/1/status?status=COMPLETED")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("COMPLETED");
    }

    @Test
    void getOrderStream_Success() {
        testOrder = createTestOrder();
        when(orderService.getOrdersByStatus(OrderStatus.PENDING))
                .thenReturn(Flux.just(testOrder));

        webTestClient.get()
                .uri("/api/orders/stream?status=PENDING")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Order.class)
                .hasSize(1);
    }

    @Test
    void stressTest_Success() {
        when(orderService.processBatchOrders(any(Flux.class)))
                .thenReturn(Flux.just(createTestOrder()));

        webTestClient.post()
                .uri("/api/orders/stress-test?orderCount=1000")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Stress test initiated with 1000 orders");
    }

    @Test
    void stressTest_ExceedsLimit() {
        webTestClient.post()
                .uri("/api/orders/stress-test?orderCount=10001")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Maximum 10,000 orders allowed for stress test");
    }

    private Order createTestOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setOrderNumber("TEST-001");
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(new BigDecimal("100.00"));
        order.setCustomerId("CUST-001");
        order.setProductId("PROD-001");
        order.setQuantity(1);
        return order;
    }
} 