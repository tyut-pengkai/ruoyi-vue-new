package com.ruoyi.web.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.EmailService;
import com.ruoyi.framework.web.service.impl.EmailServiceImpl;
import com.ruoyi.system.service.ISysConfigService;

/**
 * 邮箱验证码
 * 
 * @author ruoyi
 */
@RestController
public class SysEmailController extends BaseController
{
    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailServiceImpl emailServiceImpl;

    @Autowired
    private ISysConfigService configService;

    /**
     * 发送邮箱验证码
     */
    @PostMapping("/sendEmailCode")
    public AjaxResult sendEmailCode(@RequestBody EmailRequest request)
    {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser"))))
        {
            return error("当前系统没有开启注册功能！");
        }

        if (StringUtils.isEmpty(request.getEmail()))
        {
            return error("邮箱地址不能为空");
        }

        // 简单的邮箱格式验证
        if (!request.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))
        {
            return error("邮箱格式不正确");
        }

        boolean result = emailService.sendEmailCode(request.getEmail());
        if (result)
        {
            return success("验证码发送成功，请查收邮件");
        }
        else
        {
            return error("验证码发送失败，请稍后重试");
        }
    }

    /**
     * 测试邮件发送
     */
    @PostMapping("/testEmail")
    public AjaxResult testEmail(@RequestBody EmailRequest request)
    {
        if (StringUtils.isEmpty(request.getEmail()))
        {
            return error("邮箱地址不能为空");
        }

        boolean result = emailServiceImpl.sendEmail(request.getEmail(), "测试邮件", "这是一封测试邮件，如果您收到这封邮件，说明邮件发送功能配置正确！");
        if (result)
        {
            return success("测试邮件发送成功，请查收");
        }
        else
        {
            return error("测试邮件发送失败，请检查邮件配置");
        }
    }

    /**
     * 邮箱请求对象
     */
    public static class EmailRequest
    {
        private String email;

        public String getEmail()
        {
            return email;
        }

        public void setEmail(String email)
        {
            this.email = email;
        }
    }
} 