//package com.ruoyi.api.v1.api.noAuth.general;
//
//import com.ruoyi.api.v1.constants.Constants;
//import com.ruoyi.api.v1.domain.Api;
//import com.ruoyi.api.v1.domain.Function;
//
//public class TestNoToken extends Function {
//
//    @Override
//    public void init() {
//        this.setApi(new Api("testNoToken", "测试非登录接口", false, Constants.API_TAG_GENERAL, "测试noToken接口"));
//    }
//
//    @Override
//    public Object handle() {
//        return this.getParams();
//    }
//}
