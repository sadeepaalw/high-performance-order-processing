package com.upside.orderprocessing.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("orders")
@Data
@NoArgsConstructor
public class Order {
    @Id
    private Long id;

    private String orderNumber;

    private OrderStatus status;

    private BigDecimal totalAmount;

    private String customerId;

    private String productId;

    private Integer quantity;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private Long version;
} 