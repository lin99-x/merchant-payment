package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.sky.enumeration.TransactionStatus;

/**
 * Transaction
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private UUID paymentId;

    private String provider;

    private String providerTransactionId;

    private TransactionStatus status;

    private Long amount;

    private String currency;

    private String responseCode;

    private String responseMessage;

    private String rawResponse;

    private OffsetDateTime createdAt;

    private OffsetDateTime processedAt;
}
