package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class ProductDTO implements Serializable {

    private String name;

    private String description;

    private Long price;

    private String currency;

    private String imageUrl;

}
