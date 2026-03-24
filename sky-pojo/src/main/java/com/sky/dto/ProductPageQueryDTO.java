package com.sky.dto;

import java.io.Serializable;
import java.util.UUID;

import lombok.Data;

@Data
public class ProductPageQueryDTO implements Serializable{

    private String name;

    private Integer page;

    private Integer pageSize;

    private UUID merchantId;

}
