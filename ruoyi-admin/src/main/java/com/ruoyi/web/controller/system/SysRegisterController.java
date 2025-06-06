package com.ruoyi.web.controller.system;

import cn.hutool.core.util.BooleanUtil;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.ESystemRole;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.SysRegisterService;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.controller.system.vo.LoginSmsReqVO;
import com.ruoyi.web.controller.system.vo.RegisterBySmsCodeVO;
import com.ruoyi.web.controller.xkt.vo.PhoneNumberVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注册验证
 *
 * @author ruoyi
 */
@Api(tags = "注册")
@RestController
@RequestMapping("/rest/v1/reg")
public class SysRegisterController extends BaseController {
    @Autowired
    private SysRegisterService registerService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysUserService userService;

    @ApiOperation(value = "档口供应商注册")
    @PostMapping("/registerStore")
    public AjaxResult registerStore(@Validated @RequestBody RegisterBySmsCodeVO vo) {
        checkRegisterAccess();
        AjaxResult ajax = AjaxResult.success();
        String token = registerService.registerByPhoneNumber(vo.getPhoneNumber(), vo.getPassword(), vo.getCode(),
                ESystemRole.SUPPLIER);
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    @ApiOperation(value = "电商卖家注册")
    @PostMapping("/registerSeller")
    public AjaxResult registerSeller(@Validated @RequestBody RegisterBySmsCodeVO vo) {
        checkRegisterAccess();
        AjaxResult ajax = AjaxResult.success();
        String token = registerService.registerByPhoneNumber(vo.getPhoneNumber(), vo.getPassword(), vo.getCode(),
                ESystemRole.SELLER);
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    @ApiOperation(value = "代发专员注册")
    @PostMapping("/registerAgent")
    public AjaxResult registerAgent(@Validated @RequestBody RegisterBySmsCodeVO vo) {
        checkRegisterAccess();
        AjaxResult ajax = AjaxResult.success();
        String token = registerService.registerByPhoneNumber(vo.getPhoneNumber(), vo.getPassword(), vo.getCode(),
                ESystemRole.AGENT);
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    @ApiOperation(value = "手机号是否已注册")
    @PostMapping("/isPhoneNumberRegistered")
    public R<Boolean> isPhoneNumberRegistered(@Validated @RequestBody PhoneNumberVO phoneNumberVO) {
        SysUser u = new SysUser();
        u.setPhonenumber(phoneNumberVO.getPhoneNumber());
        boolean unique = userService.checkPhoneUnique(u);
        return R.ok(!unique);
    }

    @ApiOperation(value = "发送登录短信验证码")
    @PostMapping("/sendSmsVerificationCode")
    public R sendSmsVerificationCode(@Validated @RequestBody LoginSmsReqVO vo) {
        loginService.sendSmsVerificationCode(vo.getPhoneNumber(), vo.getCode(), vo.getUuid());
        return R.ok();
    }

    private void checkRegisterAccess() {
        if (!BooleanUtil.toBoolean(configService.selectConfigByKey("sys.account.registerUser"))) {
            throw new ServiceException("当前系统没有开启注册功能");
        }
    }
}
