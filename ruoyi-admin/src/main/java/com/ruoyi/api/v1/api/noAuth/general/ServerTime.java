package com.ruoyi.api.v1.api.noAuth.general;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.Resp;
import com.ruoyi.common.utils.DateUtils;

public class ServerTime extends Function {

    @Override
    public void init() {
        this.setApi(new Api("serverTime.ng", "获取服务器时间", false, Constants.API_TAG_GENERAL,
                "获取服务器时间，格式yyyy-MM-dd HH:mm:ss", Constants.AUTH_TYPE_ALL, Constants.BILL_TYPE_ALL, null,
                new Resp(Resp.DataType.string, "服务器时间")));
    }

    @Override
    public Object handle() {
        return DateUtils.getTime();
    }

}
