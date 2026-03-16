package com.sky.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OrderItem
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private UUID orderId;

    private UUID productId;

    private Integer quantity;

    private Long unitPrice;

    private Long subtotal;

    private OffsetDateTime createdAt;
}
