package com.sky.service;

import java.util.UUID;

import com.sky.dto.OrderCancelDTO;
import com.sky.dto.OrderCheckoutDTO;
import com.sky.dto.OrderCreateDTO;
import com.sky.dto.OrderQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderVO;

public interface OrderService {

    /**
     * Create an order
     * @param orderCreateDTO
     * @return
     */
    OrderVO createOrder(OrderCreateDTO orderCreateDTO);

    /**
     * Get order by ID
     * @param orderId
     * @return
     */
    OrderVO getOrderById(UUID orderId);

    /**
     * List orders based on query parameters
     * @param orderQueryDTO
     * @return
     */
    PageResult listOrders(OrderQueryDTO orderQueryDTO);

    /**
     * Checkout an order
     * @param orderId
     * @param orderCheckoutDTO
     * @return
     */
    OrderVO checkout(UUID orderId, OrderCheckoutDTO orderCheckoutDTO);

    /**
     * Cancel an order
     * @param orderId
     * @param orderCancelDTO
     * @return
     */
    void cancelOrder(UUID orderId, OrderCancelDTO orderCancelDTO);

}
