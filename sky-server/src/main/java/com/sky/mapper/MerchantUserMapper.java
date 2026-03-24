package com.sky.mapper;

import com.sky.dto.MerchantUserPageQueryDTO;
import com.sky.entity.MerchantUser;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.UUID;

@Mapper
public interface MerchantUserMapper {

    /**
     * Find user by email
     *
     * @param email
     * @return
     */
    @Select("select * from merchant_users where email = #{email}")
    MerchantUser getByEmail(String email);

    /**
     * Reset failed login attempts
     *
     * @param userId
     */
    @Update("update merchant_users set failed_login_attempts = 0, locked_until = null, last_login_at = now() where id = #{userId}")
    void resetFailedLoginAttempts(UUID userId);

    @Insert("insert into merchant_users (merchant_id, email, password_hash, role, is_active, failed_login_attempts, locked_until, last_login_at, created_at, updated_at) " +
            "values (#{merchantId}, #{email}, #{passwordHash}, #{role}::user_role, #{isActive}, #{failedLoginAttempts}, #{lockedUntil}, #{lastLoginAt}, now(), now())")
    void insert(MerchantUser merchantUser);

    /**
     * Page query merchant users
     *
     * @param merchantUserPageQueryDTO
     * @return
     */
	List<MerchantUser> pageQueryMerchantUsers(MerchantUserPageQueryDTO merchantUserPageQueryDTO);

    void updateMerchantUser(MerchantUser merchantUser);

    MerchantUser getById(UUID id);

}
