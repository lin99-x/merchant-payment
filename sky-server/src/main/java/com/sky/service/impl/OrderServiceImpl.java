package com.sky.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrderCancelDTO;
import com.sky.dto.OrderCheckoutDTO;
import com.sky.dto.OrderCreateDTO;
import com.sky.dto.OrderQueryDTO;
import com.sky.entity.Order;
import com.sky.entity.OrderItem;
import com.sky.enumeration.OrderStatus;
import com.sky.mapper.OrderItemMapper;
import com.sky.mapper.OrderMapper;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.service.PaymentService;
import com.sky.vo.OrderVO;
import com.sky.vo.PaymentVO;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private PaymentService paymentService;

    /**
     * Create an order
     *
     * @param orderCreateDTO
     * @return
     */
    @Transactional
    public OrderVO createOrder(OrderCreateDTO orderCreateDTO) {
        Order order = new Order();
        BeanUtils.copyProperties(orderCreateDTO, order);
        // Set default status to PENDING when creating an order
        order.setStatus(OrderStatus.PENDING);
        // Save order to database, return generated order ID
        orderMapper.insert(order);
        UUID orderId = order.getId();

        // Convert OrderItemDTOs to OrderItems and set orderId
        List<OrderItem> orderItems = orderCreateDTO.getOrderItems().stream().map(itemDTO -> {
            OrderItem item = new OrderItem();
            BeanUtils.copyProperties(itemDTO, item);
            item.setOrderId(orderId);
            return item;
        }).collect(Collectors.toList());

        // Insert order items into database
        orderItemMapper.insertBatch(orderItems);

        // Convert Order entity to OrderVO and return
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        orderVO.setOrderItems(orderItems);

        return orderVO;
    }

    /**
     * Get order by ID
     *
     * @param orderId
     * @return
     */
    public OrderVO getOrderById(UUID orderId) {
        // Fetch order from database
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            return null;
        }
        // Fetch order items from database
        List<OrderItem> orderItems = orderItemMapper.selectByOrderId(orderId);

        // Convert Order entity to OrderVO
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        orderVO.setOrderItems(orderItems);

        return orderVO;
    }

    /**
     * List orders based on query parameters
     *
     * @param orderQueryDTO
     * @return
     */
    public PageResult listOrders(OrderQueryDTO orderQueryDTO) {
        // Start pagination
        PageHelper.startPage(orderQueryDTO.getPage(), orderQueryDTO.getPageSize());

        List<Order> orders = orderMapper.selectByQuery(orderQueryDTO);

        // Convert Order entities to OrderVOs
        List<OrderVO> orderVOs = orders.stream().map(order -> {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);
            // Fetch order items for each order
            List<OrderItem> orderItems = orderItemMapper.selectByOrderId(order.getId());
            orderVO.setOrderItems(orderItems);
            return orderVO;
        }).collect(Collectors.toList());

        PageInfo<OrderVO> pageInfo = new PageInfo<>(orderVOs);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * Checkout an order
     *
     * @param orderId
     * @param orderCheckoutDTO
     * @return
     */
    public OrderVO checkout(UUID orderId, OrderCheckoutDTO orderCheckoutDTO) {
        // Fetch order from database
        Order order = orderMapper.selectById(orderId);
        // Validate order
        validateOrder(order);

        // Handle idempotency key
        String idempotencyKey = orderCheckoutDTO.getIdempotencyKey();
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            idempotencyKey = UUID.randomUUID().toString();
        }

        // Check Redis for duplicate
        String existingPaymentId = (String) redisTemplate.opsForValue().get("payment:idem:" + idempotencyKey);
        if (existingPaymentId != null) {
            // Duplicate request, return existing payment result
            order = orderMapper.selectById(orderId);
            return buildOrderVO(order);
        }

        // Call payment service to proess payment
        PaymentVO paymentVO = paymentService.createPayment(orderId, orderCheckoutDTO);
        UUID paymentId = paymentVO.getId();

        // Store idempotency key in Redis with real paymentId
        redisTemplate.opsForValue().set("payment:idem:" + idempotencyKey, paymentId.toString(), 24, TimeUnit.HOURS);

        // Set payment lock in Redis
        redisTemplate.opsForValue().set("payment:lock:" +paymentId, "locked", 30, TimeUnit.MINUTES);

        // Update order status to CHECKED_OUT and link paymentId
        order.setStatus(OrderStatus.CHECKOUT);
        order.setPaymentId(paymentId);
        orderMapper.updateOrder(order);

        // Return orderVO
        return buildOrderVO(order);
    }

    /**
     * Validate order before checkout
     * @param order
     */
    private void validateOrder(Order order) {
        if (order == null) {
            throw new RuntimeException(MessageConstant.ORDER_NOT_FOUND);
        }
        // Check if order belongs to current merchant
        if (!order.getMerchantId().equals(BaseContext.getCurrentMerchantId())) {
            throw new RuntimeException(MessageConstant.ORDER_NOT_FOUND);
        }
        // Check if order is in PENDING status
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException(MessageConstant.ORDER_CANNOT_CHECKOUT);
        }
        // Check if order has items
        List<OrderItem> orderItems = orderItemMapper.selectByOrderId(order.getId());
        if (orderItems == null || orderItems.isEmpty()) {
            throw new RuntimeException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
    }

    /**
     * Build OrderVO from Order entity
     * @param order
     * @return
     */
    private OrderVO buildOrderVO(Order order) {
        // Fetch order items from database
        List<OrderItem> orderItems = orderItemMapper.selectByOrderId(order.getId());

        // Convert Order entity to OrderVO
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        orderVO.setOrderItems(orderItems);

        return orderVO;
    }

    /**
     * Cancel an order
     * @param orderId
     * @param orderCancelDTO
     * @return
     */
    public void cancelOrder(UUID orderId, OrderCancelDTO orderCancelDTO) {
        // Fetch order from database
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException(MessageConstant.ORDER_NOT_FOUND);
        }

        // Check if order belongs to current merchant
        if (!order.getMerchantId().equals(BaseContext.getCurrentMerchantId())) {
            throw new RuntimeException(MessageConstant.ORDER_NOT_FOUND);
        }

        // Validate order status can be cancelled
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException(MessageConstant.ORDER_CANNOT_CANCEL);
        }
        
        // Change order status to CANCELED
        order.setStatus(OrderStatus.CANCELLED);
        order.setNotes(orderCancelDTO.getReason());

        // Update order in database
        orderMapper.updateOrder(order);
    }

}
