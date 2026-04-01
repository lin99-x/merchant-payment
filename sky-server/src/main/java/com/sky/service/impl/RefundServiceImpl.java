package com.sky.service.impl;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.RefundCreateDTO;
import com.sky.dto.RefundQueryDTO;
import com.sky.entity.Payment;
import com.sky.entity.Refund;
import com.sky.enumeration.PaymentStatus;
import com.sky.enumeration.RefundStatus;
import com.sky.mapper.PaymentMapper;
import com.sky.mapper.RefundMapper;
import com.sky.result.PageResult;
import com.sky.service.RefundService;
import com.sky.vo.RefundVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RefundServiceImpl implements RefundService {

    @Autowired
    private RefundMapper refundMapper;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * Create refund for a payment
     *
     * @param paymentId
     * @param refundCreateDTO
     * @return
     */
    public RefundVO createRefund(UUID paymentId, RefundCreateDTO refundCreateDTO) {
        // Validate payment exists
        Payment payment = paymentMapper.selectById(paymentId);
        if (payment == null) {
            throw new RuntimeException(MessageConstant.PAYMENT_NOT_FOUND);
        }

        // Validate payment belongs to current merchant
        UUID merchantId = BaseContext.getCurrentMerchantId();
        if (!payment.getMerchantId().equals(merchantId)) {
            throw new RuntimeException(MessageConstant.PAYMENT_NOT_BELONG_TO_CURRENT_MERCHANT);
        }

        // Validate payment is successful and can be refunded
        if (!payment.getStatus().equals(PaymentStatus.SUCCEEDED)) {
            throw new RuntimeException(MessageConstant.PAYMENT_CANNOT_BE_REFUNDED);
        }

        // Check idempotency using Redis
        String idempotencyKey = refundCreateDTO.getIdempotencyKey();
        String existingRefundId = (String) redisTemplate.opsForValue().get("refund:idem:" + idempotencyKey);
        if (existingRefundId != null) {
            Refund existingRefund = refundMapper.selectById(UUID.fromString(existingRefundId));
            return convertToVO(existingRefund);
        }

        // Validate refund amount does not exceed payment amount
        Long refundAmound = refundCreateDTO.getAmount();
        if (refundAmound.compareTo(payment.getAmount()) > 0) {
            throw new RuntimeException(MessageConstant.REFUND_AMOUNT_EXCEEDS_PAYMENT_AMOUNT);
        }

        // Check for existing refunds and calculate total refunded amount
        Refund existingRefund = refundMapper.selectByPaymentId(paymentId);
        if (existingRefund != null && existingRefund.getStatus().equals(RefundStatus.SUCCEEDED)) {
            Long totalRefundAmount = existingRefund.getAmount() + refundAmound;
            if (totalRefundAmount.compareTo(payment.getAmount()) > 0) {
                throw new RuntimeException(MessageConstant.REFUND_AMOUNT_EXCEEDS_PAYMENT_AMOUNT);
            }
        }

        // Create refund record in database
        Refund refund = Refund.builder()
                .paymentId(paymentId)
                .amount(refundAmound)
                .reason(refundCreateDTO.getReason())
                .status(RefundStatus.PENDING)
                .idempotencyKey(idempotencyKey)
                .build();

        refundMapper.insert(refund);
        // Get generated refund ID
        UUID refundId = refund.getId();

        // Store idempotency key in Redis with refund ID
        redisTemplate.opsForValue().set("refund:idem:" + idempotencyKey, refundId.toString(), 24, TimeUnit.HOURS);

        // Trigger asynchronous refund processing (e.g. send message to message queue)
        refundAsyncProcess(refundId);

        return convertToVO(refund);
    }

    /**
     * Get a list of refunds based on query criteria.
     *
     * @param refundQueryDTO
     * @return
     */
    public PageResult getRefunds(RefundQueryDTO refundQueryDTO) {
        PageHelper.startPage(refundQueryDTO.getPage(), refundQueryDTO.getPageSize());
        // Fetch refund list from database with filtering based on refundQueryDTO
        List<Refund> refunds = refundMapper.selectByQuery(refundQueryDTO);
        PageInfo<Refund> pageInfo = new PageInfo<>(refunds);
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * Cancel a refund
     *
     * @param refundId
     * @return
     */
    public RefundVO cancelRefund(UUID refundId) {
        Refund refund = refundMapper.selectById(refundId);
        if (refund == null) {
            throw new RuntimeException(MessageConstant.REFUND_NOT_FOUND);
        }

        if (!refund.getStatus().equals(RefundStatus.PENDING)) {
            throw new RuntimeException(MessageConstant.ONLY_PENDING_REFUND_CAN_BE_CANCELED);
        }

        refund.setStatus(RefundStatus.CANCELLED);
        refundMapper.update(refund);

        return convertToVO(refund);
    }

    /**
     * Get refund details by refund id
     *
     * @param refundId
     * @return
     */
    public RefundVO getRefundById(UUID refundId) {
        Refund refund = refundMapper.selectById(refundId);
        if (refund == null) {
            throw new RuntimeException(MessageConstant.REFUND_NOT_FOUND);
        }
        return convertToVO(refund);
    }

    @Async
    public void refundAsyncProcess(UUID refundId) {
        transactionTemplate.executeWithoutResult(status -> {
            try{
                // Simulate refund processing delay
                Thread.sleep(3000 + RandomUtils.nextInt(2000));

                // 90% success rate simulation
                boolean isSuccess = RandomUtils.nextInt(100) < 90;

                Refund refund = refundMapper.selectById(refundId);

                if (isSuccess) {
                    refund.setStatus(RefundStatus.SUCCEEDED);
                    refund.setRefundedAt(OffsetDateTime.now());

                    // Update payment status
                    Payment payment = paymentMapper.selectById(refund.getPaymentId());
                    payment.setStatus(PaymentStatus.REFUNDED);
                    paymentMapper.update(payment);
                } else {
                    refund.setStatus(RefundStatus.FAILED);
                }
                refundMapper.update(refund);
            } catch (Exception e) {
                log.error("Refund processing failed: {}", e.getMessage());
                Refund refund = refundMapper.selectById(refundId);
                refund.setStatus(RefundStatus.FAILED);
                refundMapper.update(refund);
            }
        });
    }

    private RefundVO convertToVO(Refund refund) {
        return RefundVO.builder()
                .id(refund.getId())
                .paymentId(refund.getPaymentId())
                .idempotencyKey(refund.getIdempotencyKey())
                .amount(refund.getAmount())
                .reason(refund.getReason())
                .status(refund.getStatus())
                .refundedAt(refund.getRefundedAt())
                .createdAt(refund.getCreatedAt())
                .updatedAt(refund.getUpdatedAt())
                .build();
    }

}
