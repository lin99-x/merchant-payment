package com.sky.service;

import java.util.UUID;

import com.sky.dto.ProductDTO;
import com.sky.dto.ProductPageQueryDTO;
import com.sky.entity.Product;
import com.sky.result.PageResult;

public interface ProductService {

    /**
     * Page query Products
     * @param productPageQueryDTO
     * @return
     */
    PageResult pageQueryProducts(ProductPageQueryDTO productPageQueryDTO);

    /**
     * Get product by Id
     * @param id
     * @return
     */
    Product getProductById(UUID id);

    /**
     * Create new product
     * @param productDTO
     */
    void createProduct(ProductDTO productDTO);

    /**
     * Update product information
     * @param id
     * @param productDTO
     */
    void updateProduct(UUID id, ProductDTO productDTO);

    /**
     * Archive (soft delete) a product
     * @param id
     */
    void archiveProduct(UUID id);

}
