package com.sky.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Merchant User Login DTO")
public class MerchantUserLoginDTO implements Serializable {

    @Schema(description = "Email")
    private String email;

    @Schema(description = "Password")
    private String password;

}
