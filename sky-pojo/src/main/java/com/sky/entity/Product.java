package com.sky.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

import com.sky.enumeration.ProductStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Product
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private UUID merchantId;

    private String name;

    private String description;

    private Long price;

    private String currency;

    private ProductStatus status;

    private String imageUrl;

    private Map<String, Object> metadata;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

}
