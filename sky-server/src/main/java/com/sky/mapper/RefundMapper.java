package com.sky.mapper;

import java.util.List;
import java.util.UUID;

import org.apache.ibatis.annotations.Mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.RefundQueryDTO;
import com.sky.entity.Refund;
import com.sky.enumeration.OperationType;

@Mapper
public interface RefundMapper {

    Refund selectById(UUID id);

    Refund selectByPaymentId(UUID paymentId);

    @AutoFill(value = OperationType.INSERT)
    void insert(Refund refund);

    @AutoFill(value = OperationType.UPDATE)
    void update(Refund refund);

    List<Refund> selectByQuery(RefundQueryDTO refundQueryDTO);

}
