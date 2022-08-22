package com.ruoyi.api.v1.api.auth.general;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.Resp;

public class IsOnline extends Function {

    @Override
    public void init() {
        this.setApi(new Api("isOnline.ag", "获取用户在线状态", true, Constants.API_TAG_GENERAL,
                "在线返回1", Constants.AUTH_TYPE_ALL, Constants.BILL_TYPE_ALL, null,
                new Resp(Resp.DataType.string, "在线值为1")));
    }

    @Override
    public Object handle() {
        return "1";
    }
}
