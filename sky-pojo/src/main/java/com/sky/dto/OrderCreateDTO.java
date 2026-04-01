package com.sky.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;

@Data
public class OrderCreateDTO implements Serializable {

    private UUID merchantId;

    private UUID customerId;

    private Long total;

    private String currency;

    private String notes;

    @NotEmpty(message = "Order must contain at least one item")
    private List<OrderItemDTO> orderItems;

}