package com.sky.dto;

import java.io.Serializable;
import java.util.UUID;

import lombok.Data;

@Data
public class OrderCheckoutDTO implements Serializable {

    private UUID paymentMethodId;

    private String idempotencyKey;

}
