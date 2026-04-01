package com.sky.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class OrderCancelDTO implements Serializable {

    private String reason;

}
