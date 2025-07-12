package com.ruoyi.web.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.RegisterBody;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.SysRegisterService;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;

/**
 * 注册验证
 * 
 * @author ruoyi
 */
@RestController
public class SysRegisterController extends BaseController
{
    @Autowired
    private SysRegisterService registerService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysUserService userService;

    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterBody user)
    {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser"))))
        {
            return error("当前系统没有开启注册功能！");
        }
        String msg = registerService.register(user);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }

    /**
     * 校验用户名唯一性
     */
    @PostMapping("/checkUsernameUnique")
    public AjaxResult checkUsernameUnique(@RequestBody UsernameCheckRequest request)
    {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser"))))
        {
            return error("当前系统没有开启注册功能！");
        }

        if (StringUtils.isEmpty(request.getUsername()))
        {
            return error("用户名不能为空");
        }

        // 用户名长度校验
        if (request.getUsername().length() < 2 || request.getUsername().length() > 20)
        {
            return error("用户名长度必须在2到20个字符之间");
        }

        // 用户名格式校验（只允许字母、数字、下划线）
        if (!request.getUsername().matches("^[a-zA-Z0-9_]+$"))
        {
            return error("用户名只能包含字母、数字和下划线");
        }

        SysUser user = new SysUser();
        user.setUserName(request.getUsername());
        
        boolean isUnique = userService.checkUserNameUnique(user);
        if (isUnique)
        {
            return success("用户名可用");
        }
        else
        {
            return error("用户名已存在");
        }
    }

    /**
     * 校验邮箱唯一性
     */
    @PostMapping("/checkEmailUnique")
    public AjaxResult checkEmailUnique(@RequestBody EmailCheckRequest request)
    {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser"))))
        {
            return error("当前系统没有开启注册功能！");
        }

        if (StringUtils.isEmpty(request.getEmail()))
        {
            return error("邮箱地址不能为空");
        }

        // 邮箱格式校验
        if (!request.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))
        {
            return error("邮箱格式不正确");
        }

        SysUser user = new SysUser();
        user.setEmail(request.getEmail());
        
        boolean isUnique = userService.checkEmailUnique(user);
        if (isUnique)
        {
            return success("邮箱可用");
        }
        else
        {
            return error("邮箱已存在");
        }
    }

    /**
     * 用户名校验请求对象
     */
    public static class UsernameCheckRequest
    {
        private String username;

        public String getUsername()
        {
            return username;
        }

        public void setUsername(String username)
        {
            this.username = username;
        }
    }

    /**
     * 邮箱校验请求对象
     */
    public static class EmailCheckRequest
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
