package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

import com.sky.enumeration.PaymentStatus;

/**
 * Payment
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private UUID merchantId;

    private UUID customerId;

    private UUID paymentMethodId;

    private String idempotencyKey;

    private Long amount;

    private String currency;

    private PaymentStatus status;

    private String description;

    private OffsetDateTime paidAt;

    private Map<String, Object> metadata;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
}
