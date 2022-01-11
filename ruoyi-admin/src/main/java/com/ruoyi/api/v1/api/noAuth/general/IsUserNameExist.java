package com.ruoyi.api.v1.api.noAuth.general;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.Param;
import com.ruoyi.system.service.ISysUserService;

import javax.annotation.Resource;

public class IsUserNameExist extends Function {

    @Resource
    private ISysUserService userService;

    @Override
    public void init() {
        this.setApi(new Api("isUserNameExist.ng", "用户名是否存在", false, Constants.API_TAG_GENERAL,
                "检查用户名是否已存在，存在返回1，否则返回0",
                new Param[]{
                        new Param("username", true, "要检查的用户名")
                }));
    }

    @Override
    public Object handle() {
        return userService.checkUserNameUnique(getParams().get("username"));
    }

}
