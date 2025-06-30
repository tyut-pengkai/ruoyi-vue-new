package com.ruoyi.framework.web.service;

import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 邮箱服务
 * 
 * @author ruoyi
 */
@Component
public class EmailService
{
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private RedisCache redisCache;
    
    @Autowired
    private ISysUserService userService;

    /**
     * 发送邮箱验证码
     * 
     * @param email 邮箱地址
     * @return 结果
     */
    public boolean sendEmailCode(String email)
    {
        if (StringUtils.isEmpty(email))
        {
            return false;
        }

        // 生成6位随机验证码
        String emailCode = String.valueOf((int)((Math.random() * 9 + 1) * 100000));
        
        // 保存验证码到Redis，有效期5分钟
        String verifyKey = CacheConstants.EMAIL_CODE_KEY + email;
        redisCache.setCacheObject(verifyKey, emailCode, 5, TimeUnit.MINUTES);

        // 异步发送邮件
        AsyncManager.me().execute(AsyncFactory.sendEmail(email, "注册验证码", 
            "您的注册验证码是：" + emailCode + "，有效期5分钟，请勿泄露给他人。"));

        log.info("发送邮箱验证码到：{}，验证码：{}", email, emailCode);
        return true;
    }

    /**
     * 验证邮箱验证码
     * 
     * @param email 邮箱地址
     * @param emailCode 验证码
     * @return 结果
     */
    public boolean validateEmailCode(String email, String emailCode)
    {
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(emailCode))
        {
            return false;
        }

        String verifyKey = CacheConstants.EMAIL_CODE_KEY + email;
        String cacheCode = redisCache.getCacheObject(verifyKey);
        
        if (cacheCode != null && emailCode.equals(cacheCode))
        {
            // 验证成功后删除验证码
            redisCache.deleteObject(verifyKey);
            return true;
        }
        
        return false;
    }

    /**
     * 发送密码重置验证码
     *
     * @param email 邮箱地址
     * @return 结果
     */
    public boolean sendPasswordResetCode(String email)
    {
        if (StringUtils.isEmpty(email))
        {
            return false;
        }

        SysUser user = userService.selectUserByEmail(email);
        if (StringUtils.isNull(user))
        {
            // 为安全起见，即使用户不存在也返回成功，避免信息泄露
            log.warn("请求为不存在的邮箱 {} 发送密码重置验证码", email);
            return true;
        }

        // 生成6位随机验证码
        String emailCode = String.valueOf((int)((Math.random() * 9 + 1) * 100000));

        // 保存验证码到Redis，有效期5分钟
        String verifyKey = CacheConstants.PASSWORD_RESET_CODE_KEY + email;
        redisCache.setCacheObject(verifyKey, emailCode, 5, TimeUnit.MINUTES);

        // 异步发送邮件
        AsyncManager.me().execute(AsyncFactory.sendEmail(email, "密码重置验证码",
                "您的密码重置验证码是：" + emailCode + "，有效期5分钟，请勿泄露给他人。"));

        log.info("发送密码重置验证码到：{}，验证码：{}", email, emailCode);
        return true;
    }

    /**
     * 验证密码重置验证码
     *
     * @param email 邮箱地址
     * @param emailCode 验证码
     * @return 结果
     */
    public boolean validatePasswordResetCode(String email, String emailCode)
    {
        String verifyKey = CacheConstants.PASSWORD_RESET_CODE_KEY + email;
        String captcha = redisCache.getCacheObject(verifyKey);
        if (captcha == null || !captcha.equals(emailCode))
        {
            return false;
        }
        // 验证成功后删除验证码
        redisCache.deleteObject(verifyKey);
        return true;
    }
} 