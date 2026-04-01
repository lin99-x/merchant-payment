package com.sky.vo;

import com.sky.entity.OrderItem;
import com.sky.enumeration.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO implements Serializable {

    private UUID id;

    private UUID merchantId;

    private UUID customerId;

    private UUID paymentId;

    private String notes;

    private OrderStatus status;

    private Long total;

    private String currency;

    private List<OrderItem> orderItems;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

}
