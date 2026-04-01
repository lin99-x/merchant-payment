package com.sky.mapper;

import java.util.List;
import java.util.UUID;

import org.apache.ibatis.annotations.Mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.OrderQueryDTO;
import com.sky.entity.Order;
import com.sky.enumeration.OperationType;

@Mapper
public interface OrderMapper {

    /**
     * Insert a new order into the database
     * @param order
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Order order);

    /**
     * Select an order by its ID
     * @param orderId
     * @return
     */
    Order selectById(UUID orderId);

    /**
     * Select orders based on query parameters
     * @param orderQueryDTO
     * @return
     */
    List<Order> selectByQuery(OrderQueryDTO orderQueryDTO);

    /**
     * Update an existing order in the database
     * @param order
     */
    @AutoFill(value = OperationType.UPDATE)
    void updateOrder(Order order);

}
