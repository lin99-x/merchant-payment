package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.SecurityConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.MerchantUserDTO;
import com.sky.dto.MerchantUserLoginDTO;
import com.sky.dto.MerchantUserPageQueryDTO;
import com.sky.entity.MerchantUser;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.MerchantUserMapper;
import com.sky.result.PageResult;
import com.sky.service.MerchantUserService;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MerchantUserServiceImpl implements MerchantUserService {

    @Autowired
    private MerchantUserMapper merchantUserMapper;

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Merchant user login
     *
     * @param merchantUserLoginDTO
     * @return
     */
    public MerchantUser login(MerchantUserLoginDTO merchantUserLoginDTO) {
        String email = merchantUserLoginDTO.getEmail();
        String password = merchantUserLoginDTO.getPassword();

        //1. Find user by email
        MerchantUser merchantUser = merchantUserMapper.getByEmail(email);

        //2. Handle various exceptions (account not found, password error, account locked)
        if (merchantUser == null) {
            // Account not found
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // Password comparison
        if (!passwordEncoder.matches(password, merchantUser.getPasswordHash())) {
            // Password error
            handleFailedLoginAttempt(merchantUser);
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (!merchantUser.getIsActive()) {
            // Account is not active
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        if (merchantUser.getLockedUntil() != null && merchantUser.getLockedUntil().isAfter(OffsetDateTime.now())) {
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        // Reset failed login attempts on successful login
        merchantUserMapper.resetFailedLoginAttempts(merchantUser.getId());

        //3. Return the entity object
        return merchantUser;
    }

    public void handleFailedLoginAttempt(MerchantUser merchantUser) {
        int attempts = merchantUser.getFailedLoginAttempts() + 1;
        if (attempts >= SecurityConstant.MAX_FAILED_LOGIN_ATTEMPTS) {
            // Lock account for 15 minutes
            merchantUserMapper.lockAccount(merchantUser.getId(), attempts, OffsetDateTime.now().plusMinutes(SecurityConstant.LOCK_MINUTES));
        } else {
            // Update failed login attempts
            merchantUserMapper.updateFailedLoginAttempts(merchantUser.getId(), attempts);
        }
    }

    /**
     * Add new merchant user
     *
     * @param merchantUserDTO
     */
    public void addMerchantUser(MerchantUserDTO merchantUserDTO) {
        MerchantUser merchantUser = new MerchantUser();

        BeanUtils.copyProperties(merchantUserDTO, merchantUser);

        // Hash the password before saving, using a default password for new users
        merchantUser.setPasswordHash(passwordEncoder.encode(PasswordConstant.DEFAULT_PASSWORD));

        // Set default values for new user
        merchantUser.setIsActive(StatusConstant.ENABLE);
        merchantUser.setFailedLoginAttempts(SecurityConstant.DEFAULT_FAILED_ATTEMPTS);
        merchantUser.setLockedUntil(null);
        merchantUser.setLastLoginAt(null);

        merchantUserMapper.insert(merchantUser);
    }

    public PageResult pageQueryMerchantUsers(MerchantUserPageQueryDTO merchantUserPageQueryDTO) {
        // Start pagination
        PageHelper.startPage(merchantUserPageQueryDTO.getPage(), merchantUserPageQueryDTO.getPageSize());

        List<MerchantUser> records = merchantUserMapper.pageQueryMerchantUsers(merchantUserPageQueryDTO);
        PageInfo<MerchantUser> pageInfo = new PageInfo<>(records);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }
}
