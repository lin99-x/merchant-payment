package com.sky.vo;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.sky.enumeration.RefundStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefundVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private UUID paymentId;

    private String idempotencyKey;

    private Long amount;

    private String reason;

    private RefundStatus status;

    private OffsetDateTime refundedAt;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

}
