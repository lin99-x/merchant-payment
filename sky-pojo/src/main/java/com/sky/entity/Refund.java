package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

import com.sky.enumeration.RefundStatus;

/**
 * Refund
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Refund implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private UUID paymentId;

    private String idempotencyKey;

    private Long amount;

    private String reason;

    private RefundStatus status;

    private OffsetDateTime refundedAt;

    private Map<String, Object> metadata;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
}
