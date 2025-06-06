package com.upside.orderprocessing.integration;

import com.upside.orderprocessing.model.Order;
import com.upside.orderprocessing.model.OrderStatus;
import com.upside.orderprocessing.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Duration;

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class OrderProcessingIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll().block();
    }

    @Test
    void endToEndOrderProcessing() {
        // Create and process a batch of orders
        Order order1 = createTestOrder("ORD-001");
        Order order2 = createTestOrder("ORD-002");

        webTestClient.post()
                .uri("/api/orders/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Flux.just(order1, order2))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Order.class)
                .hasSize(2);

        // Verify orders in database
        StepVerifier.create(orderRepository.findAll())
                .expectNextCount(2)
                .verifyComplete();

        // Update order status
        webTestClient.put()
                .uri("/api/orders/1/status?status=COMPLETED")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("COMPLETED");

        // Stream orders
        webTestClient.get()
                .uri("/api/orders/stream?status=COMPLETED")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Order.class)
                .hasSize(1);

        // Stress test
        webTestClient.post()
                .uri("/api/orders/stress-test?orderCount=1000")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testConcurrentOrderProcessing() {
        // Create 100 orders
        Flux<Order> orders = Flux.range(1, 100)
                .map(i -> createTestOrder("ORD-" + i));

        // Process orders concurrently
        webTestClient.post()
                .uri("/api/orders/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(orders)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Order.class)
                .hasSize(100);

        // Verify all orders are processed
        StepVerifier.create(orderRepository.findAll())
                .expectNextCount(100)
                .verifyComplete();
    }

    @Test
    void testOrderStreaming() {
        // Create and save some orders
        Order order1 = createTestOrder("ORD-001");
        Order order2 = createTestOrder("ORD-002");
        orderRepository.saveAll(Flux.just(order1, order2)).blockLast();

        // Test streaming
        webTestClient.get()
                .uri("/api/orders/stream")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Order.class)
                .hasSize(2);
    }

    private Order createTestOrder(String orderNumber) {
        Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(new BigDecimal("100.00"));
        order.setCustomerId("CUST-001");
        order.setProductId("PROD-001");
        order.setQuantity(1);
        return order;
    }
} 