package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.MerchantUserDTO;
import com.sky.dto.MerchantUserLoginDTO;
import com.sky.dto.MerchantUserPageQueryDTO;
import com.sky.entity.MerchantUser;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.MerchantUserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.MerchantUserLoginVO;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * MerchantUserController
 */
@RestController
@RequestMapping("/admin/merchantUser")
@Slf4j
public class MerchantUserController {

    @Autowired
    private MerchantUserService merchantUserService;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * Login
     *
     * @param merchantUserLoginDTO
     * @return
     */
    @PostMapping("/login")
    @Operation(summary = "Merchant user login")
    public Result<MerchantUserLoginVO> login(@RequestBody MerchantUserLoginDTO merchantUserLoginDTO) {
        log.info("Merchant user login: {}", merchantUserLoginDTO.getEmail());

        MerchantUser merchantUser = merchantUserService.login(merchantUserLoginDTO);

        log.info("Merchant user: {}", merchantUser);

        // Login successful, generate JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, merchantUser.getId());
        claims.put(JwtClaimsConstant.MERCHANT_ID, merchantUser.getMerchantId());

        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        MerchantUserLoginVO merchantUserLoginVO = MerchantUserLoginVO.builder()
                .id(merchantUser.getId())
                .merchantId(merchantUser.getMerchantId())
                .email(merchantUser.getEmail())
                .role(merchantUser.getRole())
                .token(token)
                .build();

        return Result.success(merchantUserLoginVO);
    }

    /**
     * Logout
     *
     * @return
     */
    @PostMapping("/logout")
    @Operation(summary = "Merchant user logout")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * Add new merchant user
     *
     * @param merchantUserDTO
     * @return
     */
    @PostMapping
    @Operation(summary = "Add new merchant user")
    public Result<String> addMerchantUser(@RequestBody MerchantUserDTO merchantUserDTO) {
        log.info("Adding new merchant user: {}", merchantUserDTO);

        merchantUserService.addMerchantUser(merchantUserDTO);

        return Result.success();
    }

    /**
     * Page query merchant users
     *
     * @param merchantUserPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @Operation(summary = "Page query merchant users")
    public Result<PageResult> pageQueryMerchantUsers(@ParameterObject MerchantUserPageQueryDTO merchantUserPageQueryDTO) {
        log.info("Page query merchant users: {}", merchantUserPageQueryDTO);

        PageResult pageResult = merchantUserService.pageQueryMerchantUsers(merchantUserPageQueryDTO);

        return Result.success(pageResult);
    }

    /**
     * Activate or deactivate merchant user account
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @Operation(summary = "Activate or deactivate merchant user account")
    public Result<String> updateMerchantUserStatus(@PathVariable Integer status, UUID id) {
        log.info("Updating merchant user status: id={}, status={}", id, status);

        merchantUserService.updateMerchantUserStatus(status, id);

        return Result.success();
    }


}
