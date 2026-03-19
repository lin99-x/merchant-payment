package com.sky.service;

import java.util.UUID;

import com.sky.dto.MerchantUserDTO;
import com.sky.dto.MerchantUserLoginDTO;
import com.sky.dto.MerchantUserPageQueryDTO;
import com.sky.entity.MerchantUser;
import com.sky.result.PageResult;

public interface MerchantUserService {

    /**
     * Merchant user login
     * @param merchantUserLoginDTO
     * @return
     */
    MerchantUser login(MerchantUserLoginDTO merchantUserLoginDTO);

    /**
     * Add new merchant user
     * @param merchantUserDTO
     */
    void addMerchantUser(MerchantUserDTO merchantUserDTO);

    /**
     * Page query merchant users
     * @param merchantUserPageQueryDTO
     * @return
     */
    PageResult pageQueryMerchantUsers(MerchantUserPageQueryDTO merchantUserPageQueryDTO);

    /**
     * Activate or deactivate merchant user account
     * @param status
     * @param id
     */
    void updateMerchantUserStatus(Integer status, UUID id);
}
