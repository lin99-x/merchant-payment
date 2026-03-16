package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Customer
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private UUID merchantId;

    private String email;

    private String phone;

    private String name;

    private Map<String, Object> metadata;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
}
