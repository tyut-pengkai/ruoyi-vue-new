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
    private ISysConfigService configService;

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
        //登录
        UserInfo userInfo = userService.getUserById(userId);
        LoginUser loginUser = new LoginUser(userInfo);
        // 生成token
        return loginService.createToken(loginUser);
    }

    /**
     * 注册
     */
    public String register(RegisterBody registerBody) {
        String msg = "", username = registerBody.getUsername(), password = registerBody.getPassword();
        UserInfoEdit sysUser = new UserInfoEdit();
        sysUser.setUserName(username);

        // 验证码开关
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            validateCaptcha(registerBody.getCode(), registerBody.getUuid());
        }

        if (StringUtils.isEmpty(username)) {
            msg = "用户名不能为空";
        } else if (StringUtils.isEmpty(password)) {
            msg = "用户密码不能为空";
        } else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            msg = "账户长度必须在2到20个字符之间";
        } else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            msg = "密码长度必须在5到20个字符之间";
        } else if (!userService.checkUserNameUnique(sysUser)) {
            msg = "保存用户'" + username + "'失败，注册账号已存在";
        } else {
            sysUser.setNickName(username);
            sysUser.setPassword(SecurityUtils.encryptPassword(password));
            boolean regFlag = false;
            try {
                //TODO USER
                userService.createUser(sysUser);
            } catch (Exception e) {
                log.error("用户注册失败", e);
            }
            if (!regFlag) {
                msg = "注册失败,请联系系统管理人员";
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.register.success")));
            }
        }
        return msg;
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
