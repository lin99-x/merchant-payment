package com.sky.service.impl;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.context.BaseContext;
import com.sky.dto.ProductDTO;
import com.sky.dto.ProductPageQueryDTO;
import com.sky.entity.Product;
import com.sky.enumeration.ProductStatus;
import com.sky.mapper.ProductMapper;
import com.sky.result.PageResult;
import com.sky.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductMapper productMapper;

    /**
     * Page query products
     *
     * @param productPageQueryDTO
     * @return
     */
    public PageResult pageQueryProducts(ProductPageQueryDTO productPageQueryDTO) {
        // Get current merchant ID
        UUID merchantId = BaseContext.getCurrentMerchantId();
        productPageQueryDTO.setMerchantId(merchantId);

        // Start pagination
        PageHelper.startPage(productPageQueryDTO.getPage(), productPageQueryDTO.getPageSize());

        List<Product> records = productMapper.pageQueryProducts(productPageQueryDTO);
        PageInfo<Product> pageInfo = new PageInfo<>(records);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * Get product by Id
     *
     * @param id
     * @return
     */
    public Product getProductById(UUID id) {
        Product product = productMapper.getById(id);
        return product;
    }

    /**
     * Create a new product
     *
     * @param productDTO
     */
    public void createProduct(ProductDTO productDTO) {
        // Get current merchant ID
        UUID merchantId = BaseContext.getCurrentMerchantId();

        // Create a new product entity
        Product product = new Product();
        // Copy properties into this new entity
        BeanUtils.copyProperties(productDTO, product);
        product.setMerchantId(merchantId);
        // Set default status to active
        product.setStatus(ProductStatus.ACTIVE);

        productMapper.insert(product);
    }

    /**
     * Update a product information
     *
     * @param id
     * @param productDTO
     */
    public void updateProduct(UUID id, ProductDTO productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        product.setId(id);
        product.setUpdatedAt(OffsetDateTime.now());

        productMapper.update(product);
    }

    /**
     * Archive a product (soft delete)
     *
     * @param id
     */
    public void archiveProduct(UUID id) {
        Product product = new Product();
        product.setId(id);
        product.setStatus(ProductStatus.ARCHIVED);

        productMapper.update(product);
    }

}
