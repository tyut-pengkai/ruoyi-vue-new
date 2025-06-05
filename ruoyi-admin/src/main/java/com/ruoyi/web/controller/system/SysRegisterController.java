package com.ruoyi.web.controller.system;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.model.ESystemRole;
import com.ruoyi.common.core.domain.model.RegisterBody;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.SysRegisterService;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.web.controller.system.vo.LoginSmsReqVO;
import com.ruoyi.web.controller.system.vo.RegisterBySmsCodeVO;
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

    @ApiOperation(value = "档口供应商注册")
    @PostMapping("/registerStore")
    public AjaxResult registerStore(@Validated @RequestBody RegisterBySmsCodeVO vo) {
        AjaxResult ajax = AjaxResult.success();
        String token = registerService.registerByPhoneNumber(vo.getPhoneNumber(), vo.getPassword(), vo.getCode(),
                ESystemRole.SUPPLIER);
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    @ApiOperation(value = "卖家注册")
    @PostMapping("/registerSeller")
    public AjaxResult registerSeller(@Validated @RequestBody RegisterBySmsCodeVO vo) {
        AjaxResult ajax = AjaxResult.success();
        String token = registerService.registerByPhoneNumber(vo.getPhoneNumber(), vo.getPassword(), vo.getCode(),
                ESystemRole.SELLER);
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    @ApiOperation(value = "发送登录短信验证码")
    @PostMapping("/sendSmsVerificationCode")
    public R sendSmsVerificationCode(@Validated @RequestBody LoginSmsReqVO vo) {
        registerService.sendSmsVerificationCode(vo.getPhoneNumber(), vo.getCode(), vo.getUuid());
        return R.ok();
    }

    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterBody user) {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            return error("当前系统没有开启注册功能！");
        }
        String msg = registerService.register(user);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }
}
