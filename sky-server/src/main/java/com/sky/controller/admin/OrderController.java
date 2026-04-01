package com.sky.controller.admin;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.context.BaseContext;
import com.sky.dto.OrderCancelDTO;
import com.sky.dto.OrderCheckoutDTO;
import com.sky.dto.OrderCreateDTO;
import com.sky.dto.OrderQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderVO;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/admin/orders")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Create an order
     *
     * @param orderCreateDTO
     * @return
     */
    @PostMapping
    @Operation(summary = "Create a new order")
    public Result<OrderVO> createOrder(@RequestBody OrderCreateDTO orderCreateDTO) {
        log.info("Creating order: {}", orderCreateDTO);

        OrderVO orderVO = orderService.createOrder(orderCreateDTO);
        return Result.success(orderVO);
    }

    /**
     * Get order by ID
     *
     * @param orderId
     * @return
     */
    @GetMapping("/{orderId}")
    @Operation(summary = "Get order details by ID")
    public Result<OrderVO> getOrderById(@PathVariable UUID orderId) {
        log.info("Getting order by ID: {}", orderId);

        OrderVO orderVO = orderService.getOrderById(orderId);
        return Result.success(orderVO);
    }

    /**
     * List orders based on query parameters
     *
     * @param orderQueryDTO
     * @return
     */
    @GetMapping
    @Operation(summary = "List orders with pagination and filtering")
    public Result<PageResult> listOrders(@ParameterObject OrderQueryDTO orderQueryDTO) {
        log.info("Listing orders with query: {}", orderQueryDTO);

        orderQueryDTO.setMerchantId(BaseContext.getCurrentMerchantId());

        PageResult pageResult = orderService.listOrders(orderQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * Cancel an order
     * @param id
     * @param orderCancelDTO
     * @return
     */
    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel an order")
    public Result<String> cancelOrder(@PathVariable UUID id, @RequestBody OrderCancelDTO orderCancelDTO) {
        log.info("Canceling order with ID: {}, reason: {}", id, orderCancelDTO.getReason());

        orderService.cancelOrder(id, orderCancelDTO);

        return Result.success();
    }

    /**
     * Checkout an order
     * @param id
     * @param orderCheckoutDTO
     * @return
     */
    @PostMapping("/{id}/checkout")
    @Operation(summary = "Checkout an order")
    public Result<OrderVO> checkout(@PathVariable UUID id, @RequestBody OrderCheckoutDTO orderCheckoutDTO) {
        log.info("Checking out for order: {}", id);
        OrderVO orderVO = orderService.checkout(id, orderCheckoutDTO);
        return Result.success(orderVO);
    }

}
