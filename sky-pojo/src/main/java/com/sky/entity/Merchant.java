package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

import com.sky.enumeration.MerchantStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Merchant implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private String name;

    private String email;

    private String apiKey;

    private String webhookSecret;

    private MerchantStatus status;

    private Map<String, Object> metadata;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
}
