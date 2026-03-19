package com.sky.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

import com.sky.enumeration.UserRole;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Merchant User Login VO")
public class MerchantUserLoginVO implements Serializable {

    @Schema(description = "Primary Key")
    private UUID id;

    @Schema(description = "Merchant ID")
    private UUID merchantId;

    @Schema(description = "Email")
    private String email;

    @Schema(description = "Role")
    private UserRole role;

    @Schema(description = "JWT Token")
    private String token;

}
