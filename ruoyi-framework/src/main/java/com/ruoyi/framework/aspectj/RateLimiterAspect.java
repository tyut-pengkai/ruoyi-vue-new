package com.ruoyi.framework.aspectj;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.ruoyi.common.annotation.RateLimiter;
import com.ruoyi.common.enums.LimitType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;

/**
 * 限流处理
 *
 * @author ruoyi
 */
@Aspect
@Component
public class RateLimiterAspect
{
    private static final Logger log = LoggerFactory.getLogger(RateLimiterAspect.class);

    // 本地缓存实现限流
    private static final Map<String, CacheItem> CACHE_MAP = new HashMap<>();

    @Before("@annotation(rateLimiter)")
    public void doBefore(JoinPoint point, RateLimiter rateLimiter) throws Throwable
    {
        int time = rateLimiter.time();
        int count = rateLimiter.count();

        String combineKey = getCombineKey(rateLimiter, point);
        try
        {
            long number = 0;
            synchronized (CACHE_MAP)
            {
                CacheItem cacheItem = CACHE_MAP.get(combineKey);
                long currentTime = System.currentTimeMillis();
                long expireTime = currentTime - time * 1000;

                if (cacheItem == null || cacheItem.getLastResetTime() < expireTime)
                {
                    cacheItem = new CacheItem(currentTime, 1);
                    CACHE_MAP.put(combineKey, cacheItem);
                    number = 1;
                }
                else
                {
                    number = cacheItem.getCount() + 1;
                    if (number > count)
                    {
                        throw new ServiceException("访问过于频繁，请稍候再试");
                    }
                    cacheItem.setCount(number);
                }
            }
            log.info("限制请求'{}',当前请求'{}',缓存key'{}'", count, number, combineKey);
        }
        catch (ServiceException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new RuntimeException("服务器限流异常，请稍候再试");
        }
    }

    // 缓存项内部类
    private static class CacheItem
    {
        private long lastResetTime;
        private long count;

        public CacheItem(long lastResetTime, long count)
        {
            this.lastResetTime = lastResetTime;
            this.count = count;
        }

        public long getLastResetTime()
        {
            return lastResetTime;
        }

        public long getCount()
        {
            return count;
        }

        public void setCount(long count)
        {
            this.count = count;
        }
    }

    public String getCombineKey(RateLimiter rateLimiter, JoinPoint point)
    {
        StringBuffer stringBuffer = new StringBuffer(rateLimiter.key());
        if (rateLimiter.limitType() == LimitType.IP)
        {
            stringBuffer.append(IpUtils.getIpAddr()).append("-");
        }
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = method.getDeclaringClass();
        stringBuffer.append(targetClass.getName()).append("-").append(method.getName());
        return stringBuffer.toString();
    }
}
