package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * MerchantUserSession
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantUserSession implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private UUID userId;

    private String tokenHash;

    private OffsetDateTime expiresAt;

    private OffsetDateTime createdAt;

    private String userAgent;

    private String ipAddress;
}
