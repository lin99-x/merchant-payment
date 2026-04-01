package com.sky.controller.admin;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.dto.RefundCreateDTO;
import com.sky.dto.RefundQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.RefundService;
import com.sky.vo.RefundVO;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/admin/refunds")
@Slf4j
public class RefundController {

    @Autowired
    private RefundService refundService;

    /**
     * Create refund for a payment
     * @param id
     * @param refundCreateDTO
     * @return
     */
    @PostMapping("/{id}")
    @Operation(summary = "Create refund")
    public Result<RefundVO> createRefund(@PathVariable UUID id, @RequestBody RefundCreateDTO refundCreateDTO) {
        log.info("Creating refund for payment id: {}, refundCreateDTO: {}", id, refundCreateDTO);
        RefundVO refundVO = refundService.createRefund(id, refundCreateDTO);
        return Result.success(refundVO);
    }

    @GetMapping
    @Operation(summary = "List refunds with pagenation and filtering")
    public Result<PageResult> getRefunds(@ParameterObject RefundQueryDTO refundQueryDTO) {
        log.info("Getting refunds with refundQueryDTO: {}", refundQueryDTO);
        PageResult pageResult = refundService.getRefunds(refundQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel a refund")
    public Result<RefundVO> cancelRefund(@PathVariable UUID id) {
        log.info("Canceling refund with id: {}", id);

        RefundVO refundVO = refundService.cancelRefund(id);
        return Result.success(refundVO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get refund details by id")
    public Result<RefundVO> getRefundById(@PathVariable UUID id) {
        log.info("Getting refund details for id: {}", id);
        RefundVO refundVO = refundService.getRefundById(id);
        return Result.success(refundVO);
    }

}
