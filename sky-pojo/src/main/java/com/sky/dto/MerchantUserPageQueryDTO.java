package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MerchantUserPageQueryDTO implements Serializable {

    private String name;

    private Integer page;

    private Integer pageSize;

}
