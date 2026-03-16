package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

import com.sky.enumeration.DeliveryStatus;

/**
 * WebhookEvent
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebhookEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private UUID webhookId;

    private String eventType;

    private Map<String, Object> payload;

    private DeliveryStatus deliveryStatus;

    private Integer retryCount;

    private OffsetDateTime nextRetryAt;

    private OffsetDateTime deliveredAt;

    private OffsetDateTime createdAt;
}
