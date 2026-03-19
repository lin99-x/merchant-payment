package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sky.enumeration.UserRole;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private UUID merchantId;

    private String email;

    private String name;

    @JsonIgnore
    private String passwordHash;

    private UserRole role;

    private Boolean isActive;

    private Integer failedLoginAttempts;

    private OffsetDateTime lockedUntil;

    private OffsetDateTime lastLoginAt;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

}
