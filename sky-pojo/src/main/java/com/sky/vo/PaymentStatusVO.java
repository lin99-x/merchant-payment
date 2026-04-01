package com.sky.vo;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.sky.enumeration.PaymentStatus;

import lombok.Data;

@Data
public class PaymentStatusVO {

    private PaymentStatus status;

    private UUID paymentId;

    private OffsetDateTime updatedAt;
    
}
