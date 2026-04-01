package com.sky.mapper;

import java.util.List;
import java.util.UUID;

import org.apache.ibatis.annotations.Mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.PaymentQueryDTO;
import com.sky.entity.Payment;
import com.sky.enumeration.OperationType;

@Mapper
public interface PaymentMapper {

    Payment selectById(UUID id);

    List<Payment> selectByQuery(PaymentQueryDTO paymentQueryDTO);

    @AutoFill(value = OperationType.UPDATE)
    void update(Payment payment);

    @AutoFill(value = OperationType.INSERT)
    void insert(Payment payment);

}
