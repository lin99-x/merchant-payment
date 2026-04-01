package com.sky.mapper;

import java.util.List;
import java.util.UUID;

import org.apache.ibatis.annotations.Mapper;

import com.sky.entity.OrderItem;

@Mapper
public interface OrderItemMapper {

    /**
     * Insert a batch of order items into the database
     * @param orderItems
     */
    void insertBatch(List<OrderItem> orderItems);

    /**
     * Select order items by order ID
     * @param orderId
     * @return
     */
    List<OrderItem> selectByOrderId(UUID orderId);

}
