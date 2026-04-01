package com.sky.service;

import java.util.UUID;

import com.sky.dto.RefundCreateDTO;
import com.sky.dto.RefundQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.RefundVO;

public interface RefundService {

    /**
     * Create refund for a payment
     * @param paymentId
     * @param refundCreateDTO
     * @return
     */
    RefundVO createRefund(UUID paymentId, RefundCreateDTO refundCreateDTO);

    /**
     * List refunds with pagination and filtering
     * @param refundQueryDTO
     * @return
     */
    PageResult getRefunds(RefundQueryDTO refundQueryDTO);

    /**
     * Cancel a refund
     * @param refundId
     * @return
     */
    RefundVO cancelRefund(UUID refundId);

    /**
     * Get refund details by refund id
     * @param refundId
     * @return
     */
    RefundVO getRefundById(UUID refundId);
}
