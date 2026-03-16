package com.sky.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.sky.enumeration.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Order
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable{

    private static final long serialVersionUID = 1L;

    private UUID id;

    private UUID merchantId;

    private UUID customerId;

    private UUID paymentId;

    private OrderStatus status;

    private Long total;

    private String currency;

    private String notes;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
}
