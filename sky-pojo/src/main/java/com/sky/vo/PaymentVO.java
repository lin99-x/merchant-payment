package com.sky.vo;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

import com.sky.enumeration.PaymentStatus;

import lombok.Data;

@Data
public class PaymentVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private UUID merchantId;

    private UUID customerId;

    private UUID paymentMethodId;

    private Long amount;

    private String currency;

    private PaymentStatus status;

    private String description;

    private OffsetDateTime paidAt;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

}
