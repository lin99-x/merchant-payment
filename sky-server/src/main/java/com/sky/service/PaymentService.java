package com.sky.service;

import java.util.UUID;

import com.sky.dto.OrderCheckoutDTO;
import com.sky.dto.PaymentQueryDTO;
import com.sky.entity.Payment;
import com.sky.result.PageResult;
import com.sky.vo.PaymentStatusVO;
import com.sky.vo.PaymentVO;

public interface PaymentService {

    /**
     * Get payment by Id
     * @param id
     * @return
     */
    Payment getPaymentById(UUID id);

    /**
     * Get payment list with pagination and filtering
     * @param paymentQueryDTO
     * @return
     */
    PageResult getPayments(PaymentQueryDTO paymentQueryDTO);

    /**
     * Get payment status by Id
     * @param id
     * @return
     */
    PaymentStatusVO getPaymentStatus(UUID id);

    /**
     * Create payment for an order
     * @param orderId
     * @param orderCheckoutDTO
     * @return
     */
    PaymentVO createPayment(UUID orderId, OrderCheckoutDTO orderCheckoutDTO);

    /**
     * Process payment asynchronously (e.g. after order is created, we can process payment in background to improve user experience)
     * @param paymentId
     */
    void processPaymentAsync(UUID orderId, UUID paymentId);

}
