package com.ruoyi.api.v1.api.noAuth.user;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.Param;
import com.ruoyi.api.v1.domain.Resp;
import com.ruoyi.common.core.domain.model.RegisterBody;
import com.ruoyi.common.enums.AuthType;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.framework.web.service.SysRegisterService;

import javax.annotation.Resource;
import java.util.Objects;

public class Register extends Function {

    @Resource
    private SysRegisterService registerService;

    @Override
    public void init() {
        this.setApi(new Api("register.nu", "注册新账号", false, Constants.API_TAG_ACCOUNT,
                "注册新账号", new AuthType[]{AuthType.ACCOUNT}, Constants.BILL_TYPE_ALL,
                new Param[]{
                        new Param("username", true, "账号"),
                        new Param("password", true, "密码"),
                        new Param("passwordRepeat", true, "重复密码"),
                }, new Resp(Resp.DataType.string, "成功返回0")));
    }

    @Override
    public Object handle() {
        String username = this.getParams().get("username");
        String password = this.getParams().get("password");
        String passwordRepeat = this.getParams().get("passwordRepeat");
        if (!Objects.equals(passwordRepeat, password)) {
            throw new ApiException(ErrorCode.ERROR_PARAMETERS_ERROR, "新密码与重复新密码不一致");
        }
        RegisterBody user = new RegisterBody();
        user.setUsername(username);
        user.setPassword(password);
        String register = registerService.register(user, false);
        if (register.equals("")) {
            return "0";
        }
        throw new ApiException(ErrorCode.ERROR_REGISTER_FAILED, register);
    }
}
