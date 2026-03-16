package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.sky.enumeration.PaymentMethodType;

/**
 * PaymentMethod
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethod implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private UUID customerId;

    private PaymentMethodType type;

    private String provider;

    private String token;

    private String last4;

    private Integer expiryMonth;

    private Integer expiryYear;

    private Boolean isDefault;

    private OffsetDateTime deletedAt;

    private OffsetDateTime createdAt;
}
