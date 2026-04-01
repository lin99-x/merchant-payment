package com.sky.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.dto.PaymentQueryDTO;
import com.sky.entity.Payment;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.PaymentService;
import com.sky.vo.PaymentStatusVO;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/admin/payments")
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * Get payment by Id
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get payment by Id")
    public Result<Payment> getPaymentById(@PathVariable UUID id) {
        Payment payment = paymentService.getPaymentById(id);
        return Result.success(payment);
    }

    /**
     * Get payment list with pagination and filtering
     *
     * @param paymentQueryDTO
     * @return
     */
    @GetMapping
    @Operation(summary = "Get payment list with pagination and filtering")
    public Result<PageResult> getPayments(@ParameterObject PaymentQueryDTO paymentQueryDTO) {
        PageResult pageResult = paymentService.getPayments(paymentQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * Get payment status by Id
     * @param id
     * @return
     */
    @GetMapping("/{id}/status")
    @Operation(summary = "Get payment status by Id")
    public Result<PaymentStatusVO> getPaymentStatus(@PathVariable UUID id) {
        PaymentStatusVO paymentStatusVO = paymentService.getPaymentStatus(id);
        return Result.success(paymentStatusVO);
    }

}
