package com.sky.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.sky.enumeration.RefundStatus;

import lombok.Data;

@Data
public class RefundQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer page;

    private Integer pageSize;

    private RefundStatus status;

    private UUID paymentId;

    private OffsetDateTime startTime;

    private OffsetDateTime endTime;

}
