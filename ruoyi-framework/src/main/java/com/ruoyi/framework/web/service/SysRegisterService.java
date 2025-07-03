package com.ruoyi.framework.web.service;

import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.model.*;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.user.CaptchaException;
import com.ruoyi.common.exception.user.CaptchaExpireException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 注册校验方法
 *
 * @author ruoyi
 */
@Slf4j
@Component
public class SysRegisterService {

    @Value("${sms.verificationCode.signName:}")
    private String verificationCodeSignName;

    @Value("${sms.verificationCode.templateCode:}")
    private String verificationCodeTemplateCode;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private SysLoginService loginService;

    /**
     * 注册
     *
     * @param phoneNumber
     * @param password
     * @param smsVerificationCode
     * @param roles
     * @return
     */
    public String registerByPhoneNumber(String phoneNumber, String password, String smsVerificationCode,
                                        ESystemRole... roles) {
        // 短信验证
        loginService.validateSmsVerificationCode(phoneNumber, smsVerificationCode);
        UserInfoEdit userEdit = new UserInfoEdit();
        userEdit.setUserName(phoneNumber);
        userEdit.setNickName(phoneNumber);
        userEdit.setPhonenumber(phoneNumber);
        userEdit.setPassword(password);
        if (roles != null) {
            userEdit.setRoleIds(Arrays.stream(roles).map(ESystemRole::getId).collect(Collectors.toList()));
        }
        //创建账号
        Long userId = userService.createUser(userEdit);
        //日志
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(phoneNumber, Constants.REGISTER,
                MessageUtils.message("user.register.success")));
        //登录
        UserInfo userInfo = userService.getUserById(userId);
        LoginUser loginUser = new LoginUser(userInfo);
        // 生成token
        return loginService.createToken(loginUser);
    }

    /**
     * 校验验证码
     *
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public void validateCaptcha(String code, String uuid) {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            throw new CaptchaExpireException();
        }
        if (!StrUtil.emptyIfNull(code).equalsIgnoreCase(captcha)) {
            throw new CaptchaException();
        }
    }

}
