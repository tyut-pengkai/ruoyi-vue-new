package com.ruoyi.framework.web.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.user.UserPasswordNotMatchException;
import com.ruoyi.common.exception.user.UserPasswordRetryLimitExceedException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.security.context.AuthenticationContextHolder;

/**
 * 登录密码方法
 * 
 * @author ruoyi
 */
@Component
public class SysPasswordService
{
    @Value(value = "${user.password.maxRetryCount}")
    private int maxRetryCount;

    @Value(value = "${user.password.lockTime}")
    private int lockTime;

    // 本地缓存替代Redis
    private static Map<String, CacheItem> passwordCache = new HashMap<>();
    
    // 缓存项内部类，用于存储值和过期时间
    private static class CacheItem {
        private Integer value;
        private long expireTime;
        
        public CacheItem(Integer value, long expireTime) {
            this.value = value;
            this.expireTime = expireTime;
        }
        
        public Integer getValue() {
            return value;
        }
        
        public long getExpireTime() {
            return expireTime;
        }
        
        public boolean isExpired() {
            return System.currentTimeMillis() > expireTime;
        }
    }

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

    public void validate(SysUser user)
    {
        Authentication usernamePasswordAuthenticationToken = AuthenticationContextHolder.getContext();
        String username = usernamePasswordAuthenticationToken.getName();
        String password = usernamePasswordAuthenticationToken.getCredentials().toString();

        Integer retryCount = getCacheObject(getCacheKey(username));

        if (retryCount == null)
        {
            retryCount = 0;
        }

        if (retryCount >= Integer.valueOf(maxRetryCount).intValue())
        {
            throw new UserPasswordRetryLimitExceedException(maxRetryCount, lockTime);
        }

        if (!matches(user, password))
        {
            retryCount = retryCount + 1;
            setCacheObject(getCacheKey(username), retryCount, lockTime, TimeUnit.MINUTES);
            throw new UserPasswordNotMatchException();
        }
        else
        {
            clearLoginRecordCache(username);
        }
    }

    public boolean matches(SysUser user, String rawPassword)
    {
        return SecurityUtils.matchesPassword(rawPassword, user.getPassword());
    }

    public void clearLoginRecordCache(String loginName)
    {
        if (hasKey(getCacheKey(loginName)))
        {
            deleteObject(getCacheKey(loginName));
        }
    }

    /**
     * 本地缓存实现 - 设置缓存对象
     */
    private void setCacheObject(String key, Integer value, int timeout, TimeUnit timeUnit) {
        long timeoutMs = timeUnit.toMillis(timeout);
        passwordCache.put(key, new CacheItem(value, System.currentTimeMillis() + timeoutMs));
    }
    
    /**
     * 本地缓存实现 - 获取缓存对象
     */
    private Integer getCacheObject(String key) {
        CacheItem item = passwordCache.get(key);
        if (item != null) {
            if (!item.isExpired()) {
                return item.getValue();
            } else {
                // 过期则删除
                passwordCache.remove(key);
            }
        }
        return null;
    }
    
    /**
     * 本地缓存实现 - 判断缓存是否存在
     */
    private boolean hasKey(String key) {
        CacheItem item = passwordCache.get(key);
        if (item != null) {
            if (item.isExpired()) {
                // 过期则删除
                passwordCache.remove(key);
                return false;
            }
            return true;
        }
        return false;
    }
    
    /**
     * 本地缓存实现 - 删除缓存对象
     */
    private boolean deleteObject(String key) {
        return passwordCache.remove(key) != null;
    }
}
