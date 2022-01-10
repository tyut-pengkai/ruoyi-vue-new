package com.ruoyi.api.v1.api.auth.general;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;

public class TestToken extends Function {

    @Override
    public void init() {
        this.setApi(new Api("testToken", "测试登录接口", true, Constants.API_TAG_GENERAL, "测试token接口"));
    }

    @Override
    public Object handle() {
        return this.getParams();
    }
}
