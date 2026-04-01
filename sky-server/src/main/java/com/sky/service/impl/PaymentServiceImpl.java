package com.sky.service.impl;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrderCheckoutDTO;
import com.sky.dto.PaymentQueryDTO;
import com.sky.entity.Order;
import com.sky.entity.Payment;
import com.sky.enumeration.OrderStatus;
import com.sky.enumeration.PaymentStatus;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.PaymentMapper;
import com.sky.result.PageResult;
import com.sky.service.PaymentService;
import com.sky.vo.PaymentStatusVO;
import com.sky.vo.PaymentVO;

@Service
public class PaymentServiceImpl implements PaymentService{

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * Get payment by Id
     *
     * @param id
     * @return
     */
    public Payment getPaymentById(UUID id) {
        // Fetch payment from database
        Payment payment = paymentMapper.selectById(id);
        if (payment == null) {
            throw new RuntimeException(MessageConstant.PAYMENT_NOT_FOUND);
        }
        return payment;
    }


    /**
     * Get payment list with pagination and filtering
     *
     * @param paymentQueryDTO
     * @return
     */
    public PageResult getPayments(PaymentQueryDTO paymentQueryDTO) {
        PageHelper.startPage(paymentQueryDTO.getPage(), paymentQueryDTO.getPageSize());
        // Fetch payment list from database with filtering based on paymentQueryDTO
        paymentQueryDTO.setMerchantId(BaseContext.getCurrentMerchantId());
        List<Payment> payments = paymentMapper.selectByQuery(paymentQueryDTO);
        PageInfo<Payment> pageInfo = new PageInfo<>(payments);
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * Get payment status by Id
     *
     * @param id
     * @return
     */
    public PaymentStatusVO getPaymentStatus(UUID id) {
        // Fetch payment from database
        Payment payment = paymentMapper.selectById(id);
        if (payment == null) {
            throw new RuntimeException(MessageConstant.PAYMENT_NOT_FOUND);
        }

        PaymentStatusVO paymentStatusVO = new PaymentStatusVO();
        // Copy properties from payment to paymentStatusVO
        BeanUtils.copyProperties(payment, paymentStatusVO);
        paymentStatusVO.setPaymentId(id);

        return paymentStatusVO;
    }

    public PaymentVO createPayment(UUID orderId, OrderCheckoutDTO orderCheckoutDTO) {
        // Fetch order details
        Order order = orderMapper.selectById(orderId);

        // Create payment record in database with status PENDING
        Payment payment = Payment.builder()
                .merchantId(order.getMerchantId())
                .customerId(order.getCustomerId())
                .paymentMethodId(orderCheckoutDTO.getPaymentMethodId())
                .idempotencyKey(orderCheckoutDTO.getIdempotencyKey())
                .amount(order.getTotal())
                .currency(order.getCurrency())
                .description(order.getNotes())
                .status(PaymentStatus.PENDING)
                .build();

        paymentMapper.insert(payment);

        processPaymentAsync(orderId, payment.getId());

        PaymentVO paymentVO = new PaymentVO();
        BeanUtils.copyProperties(payment, paymentVO);

        return paymentVO;
    }

    @Async
    public void processPaymentAsync(UUID orderId, UUID paymentId) {
        transactionTemplate.executeWithoutResult(status -> {
            try{
                Thread.sleep(5000 + (long) (Math.random() * 5000));

                // Simulate success/failure with 80% success rate
                boolean isSuccess = Math.random() < 0.8;

                Payment payment = paymentMapper.selectById(paymentId);
                Order order = orderMapper.selectById(orderId);

                if (isSuccess) {
                    // Update payment status to SUCCEEDED
                    payment.setStatus(PaymentStatus.SUCCEEDED);
                    payment.setPaidAt(OffsetDateTime.now());
                    paymentMapper.update(payment);

                    // Update order status to PAID
                    order.setStatus(OrderStatus.PAID);
                    order.setPaymentId(paymentId);
                    orderMapper.updateOrder(order);
                } else {
                    // Update payment status to FAILED
                    payment.setStatus(PaymentStatus.FAILED);
                    paymentMapper.update(payment);

                    // Update order status to PAYMENT_FAILED
                    order.setStatus(OrderStatus.CANCELLED);
                    order.setPaymentId(paymentId);
                    orderMapper.updateOrder(order);
                }
            } catch (Exception e) {
                // Handle failure, set payment status to FAILED
                Payment payment = paymentMapper.selectById(paymentId);
                payment.setStatus(PaymentStatus.FAILED);
                paymentMapper.update(payment);

                // Update order status to CANCELLED
                Order order = orderMapper.selectById(orderId);
                order.setStatus(OrderStatus.CANCELLED);
                order.setPaymentId(paymentId);
                orderMapper.updateOrder(order);
            }
        });
    }

}
