package com.ruoyi.framework.sms;

import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.framework.sms.ali.AliSmsServer;
import com.ruoyi.framework.sms.ali.entity.AliSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class SmsClientWrapper {

    @Autowired
    private AliSmsServer aliSmsServer;

    @Autowired
    private RedisCache redisCache;

    @Value("${sms.send:true}")
    private Boolean doSend;

    @Value("${sms.verificationCode.testModel:false}")
    private Boolean testModel;

    @Value("${sms.verificationCode.signName:}")
    private String verificationCodeSignName;

    @Value("${sms.verificationCode.templateCode:}")
    private String verificationCodeTemplateCode;

    public boolean sendSms(String signName, String phoneNumber, String templateCode, String templateParams) {
        boolean sendResult;
        if (doSend) {
            AliSmsResponse response = aliSmsServer.sendSms(signName, phoneNumber, templateCode, templateParams);
            sendResult = response.success();
        }
        // 测试的时候不发短信
        else {
            sendResult = true;
        }
        log.info("发送短信{},{},{},{}:{},doSend:{}", signName, phoneNumber, templateCode, templateParams, sendResult, doSend);
        return sendResult;
    }

    /**
     * 发送短信验证码
     *
     * @param cacheKeyPrefix
     * @param phoneNumber
     * @param code
     * @return
     */
    public boolean sendVerificationCode(String cacheKeyPrefix, String phoneNumber, String code) {
        String checkKey = CacheConstants.SMS_CAPTCHA_CODE_CD_PHONE_NUM_KEY + phoneNumber;
        if (redisCache.exists(checkKey)) {
            throw new ServiceException("验证码发送间隔需大于60S");
        }
        boolean success = sendSms(verificationCodeSignName, phoneNumber, verificationCodeTemplateCode,
                "{\"code\":\"" + code + "\"}");
        if (success) {
            redisCache.setCacheObject(checkKey, "1", 60, TimeUnit.SECONDS);
            redisCache.setCacheObject(cacheKeyPrefix + phoneNumber, code, 300, TimeUnit.SECONDS);
        }
        return success;
    }

    /**
     * 校验短信验证码
     *
     * @param cacheKeyPrefix
     * @param phoneNumber
     * @param code
     * @return
     */
    public boolean matchVerificationCode(String cacheKeyPrefix, String phoneNumber, String code) {
        if (testModel) {
            //测试模式
            return true;
        }
        String rk = cacheKeyPrefix + phoneNumber;
        String cacheCode = redisCache.getCacheObject(rk);
        if (cacheCode == null) {
            return false;
        }
        redisCache.deleteObject(rk);
        return StrUtil.equals(cacheCode, code);
    }

}
