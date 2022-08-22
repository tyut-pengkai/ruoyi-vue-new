package com.ruoyi.api.v1.api.auth.general;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.Resp;

public class Heartbeat extends Function {

    @Override
    public void init() {
        this.setApi(new Api("heartbeat.ag", "心跳", true, Constants.API_TAG_GENERAL,
                "刷新token过期时间，在软件配置的心跳时间内应至少请求一次本接口，否则系统将自动下线当前用户，请求后返回下次心跳截止时间",
                Constants.AUTH_TYPE_ALL, Constants.BILL_TYPE_ALL, null, new Resp(Resp.DataType.string, "下次心跳截止时间")));
    }

    @Override
    public Object handle() {
        return this.getLoginUser().getExpireTime();
    }
}
