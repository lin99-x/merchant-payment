package com.sky.controller.admin;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.dto.ProductDTO;
import com.sky.dto.ProductPageQueryDTO;
import com.sky.entity.Product;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/admin/products")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Get all products belonging to the authenticated merchant
     *
     * @return
     */
    @GetMapping("/page")
    @Operation(summary = "Page query merchant products")
    public Result<PageResult> getProductsByMerchantId(@ParameterObject ProductPageQueryDTO productPageQueryDTO) {
        log.info("Page query products: {}", productPageQueryDTO);

        PageResult pageResult = productService.pageQueryProducts(productPageQueryDTO);

        return Result.success(pageResult);
    }

    /**
     * Get product by ID
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a single product by ID")
    public Result<Product> getProductById(@PathVariable UUID id) {
        Product product = productService.getProductById(id);
        return Result.success(product);
    }

    /**
     * Create new product
     *
     * @param productDTO
     * @return
     */
    @PostMapping
    @Operation(summary = "Create a new product")
    public Result<String> createProduct(@RequestBody ProductDTO productDTO) {
        log.info("Create product: {}", productDTO);
        productService.createProduct(productDTO);
        return Result.success();
    }

    /**
     * Update product information
     *
     * @param id
     * @param productDTO
     * @return
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update product information")
    public Result<String> updateProduct(@PathVariable UUID id, @RequestBody ProductDTO productDTO) {
        productService.updateProduct(id, productDTO);
        return Result.success();
    }

    /**
     * Archive a product
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Archive a product")
    public Result<String> archiveProduct(@PathVariable UUID id) {
        productService.archiveProduct(id);
        return Result.success();
    }

}
