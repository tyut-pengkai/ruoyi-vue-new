package com.ruoyi.api.v1.api.noAuth.user;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.Param;
import com.ruoyi.api.v1.domain.Resp;
import com.ruoyi.common.enums.AuthType;
import com.ruoyi.system.service.ISysUserService;

import javax.annotation.Resource;

public class IsUserNameExist extends Function {

    @Resource
    private ISysUserService userService;

    @Override
    public void init() {
        this.setApi(new Api("isUserNameExist.nu", "用户名是否存在", false, Constants.API_TAG_ACCOUNT,
                "检查用户名是否已存在，存在返回1，否则返回0", new AuthType[]{AuthType.ACCOUNT}, Constants.BILL_TYPE_ALL,
                new Param[]{
                        new Param("username", true, "要检查的用户名")
                }, new Resp(Resp.DataType.string, "存在返回1，否则返回0")));
    }

    @Override
    public Object handle() {
        return userService.checkUserNameUnique(getParams().get("username"));
    }

}
