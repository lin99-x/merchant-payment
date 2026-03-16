package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Webhook
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Webhook implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private UUID merchantId;

    private String url;

    private List<String> eventTypes;

    private Boolean isActive;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
}
