package com.sky.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.sky.enumeration.PaymentStatus;

import lombok.Data;

@Data
public class PaymentQueryDTO {

    private Integer page;

    private Integer pageSize;

    private PaymentStatus status;

    private UUID orderId;

    private UUID customerId;

    private UUID merchantId;

    private OffsetDateTime startTime;

    private OffsetDateTime endTime;

}
