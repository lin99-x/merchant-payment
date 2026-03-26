package com.sky.mapper;

import java.util.List;
import java.util.UUID;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.ProductPageQueryDTO;
import com.sky.entity.Product;
import com.sky.enumeration.OperationType;

@Mapper
public interface ProductMapper {

    /**
     * Page query products
     *
     * @param productPageQueryDTO
     * @return
     */
    List<Product> pageQueryProducts(ProductPageQueryDTO productPageQueryDTO);

    /**
     * Get product by id
     *
     * @param id
     * @return
     */
    Product getById(UUID id);

    /**
     * Insert new product
     *
     * @param product
     */
    @Insert("insert into products (merchant_id, name, description, price, currency, status, image_url, created_at, updated_at) " +
            "values (#{merchantId}, #{name}, #{description}, #{price}, #{currency}, #{status}::product_status, #{imageUrl}, now(), now())")
    @AutoFill(value = OperationType.INSERT)
    void insert(Product product);

    /**
     * Update product information
     * @param product
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Product product);

}
