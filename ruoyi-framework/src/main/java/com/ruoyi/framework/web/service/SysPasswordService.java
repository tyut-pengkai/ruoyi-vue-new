package com.ruoyi.framework.web.service;

import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.user.UserPasswordNotMatchException;
import com.ruoyi.common.exception.user.UserPasswordRetryLimitExceedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 登录密码方法
 * 
 * @author ruoyi
 */
@Component
public class SysPasswordService
{
    @Autowired
    private RedisCache redisCache;

    @Value(value = "${user.password.maxRetryCount}")
    private int maxRetryCount;

    @Value(value = "${user.password.lockTime}")
    private int lockTime;

    /**
     * 登录账户密码错误次数缓存键名
     * 
     * @param username 用户名
     * @return 缓存键key
     */
    private String getCacheKey(String username)
    {
        return CacheConstants.PWD_ERR_CNT_KEY + username;
    }

    public void validate(SysUser user, String password)
    {
        String username = user.getUserName();

        String cacheKey = getCacheKey(username);
        Integer retryCount = redisCache.getCacheObject(cacheKey);

        if (retryCount == null)
        {
            retryCount = 0;
        }

        if (retryCount >= Integer.valueOf(maxRetryCount).intValue())
        {
            long expire = redisCache.getExpire(cacheKey) / 60;
            throw new UserPasswordRetryLimitExceedException(expire);
        }

        if (!matches(user, password))
        {
            retryCount = retryCount + 1;
            redisCache.setCacheObject(cacheKey, retryCount, lockTime, TimeUnit.MINUTES);
            throw new UserPasswordNotMatchException();
        }
        else
        {
            clearLoginRecordCache(username);
        }
    }

    public boolean matches(SysUser user, String rawPassword)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    public void clearLoginRecordCache(String loginName)
    {
        if (StrUtil.isEmpty(loginName)) {
            return;
        }
        String cacheKey = getCacheKey(loginName);
        if (redisCache.hasKey(cacheKey))
        {
            redisCache.deleteObject(cacheKey);
        }
    }
}
