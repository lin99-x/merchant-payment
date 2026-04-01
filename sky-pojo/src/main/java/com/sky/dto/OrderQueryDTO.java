package com.sky.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.sky.enumeration.OrderStatus;

@Data
public class OrderQueryDTO implements Serializable {

    private int page;

    private int pageSize;

    private OrderStatus status;

    private UUID customerId;

    private UUID merchantId;

    private UUID paymentId;

    private OffsetDateTime startTime;

    private OffsetDateTime endTime;

}
