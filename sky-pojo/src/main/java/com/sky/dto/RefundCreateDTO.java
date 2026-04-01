package com.sky.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RefundCreateDTO implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @NotNull(message = "Amount is required")
    private Long amount;

    @Size(max = 500, message = "Reason cannot exceed 500 characters")
    private String reason;

    @NotBlank(message = "Idempotency key is required")
    private String idempotencyKey;

}
