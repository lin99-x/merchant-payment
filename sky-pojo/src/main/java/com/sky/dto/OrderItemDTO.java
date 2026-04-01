package com.sky.dto;

import java.io.Serializable;
import java.util.UUID;

import lombok.Data;

@Data
public class OrderItemDTO implements Serializable {

    private UUID orderId;

    private UUID productId;

    private Integer quantity;

    private Long unitPrice;

    private Long subtotal;

}
