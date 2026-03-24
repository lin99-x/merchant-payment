package com.sky.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

import com.sky.enumeration.UserRole;

@Data
public class MerchantUserDTO implements Serializable {

    private UUID id;

    private UUID merchantId;

    private String email;

    private String name;

    private UserRole role;

}
