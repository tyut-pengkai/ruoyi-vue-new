package com.ruoyi.api.v1.api.noAuth.general;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.Resp;
import com.ruoyi.common.utils.DateUtils;

public class IsConnected extends Function {

    @Override
    public void init() {
        this.setApi(new Api("isConnected.ng", "是否连接到服务器", false, Constants.API_TAG_GENERAL,
                "判断是否正常连接到服务器，成功返回服务器13位时间戳（精确到毫秒）", Constants.AUTH_TYPE_ALL,
                Constants.BILL_TYPE_ALL, null, new Resp(Resp.DataType.string, "服务器13位时间戳（精确到毫秒）")));
    }

    @Override
    public Object handle() {
        return DateUtils.getNowDate().getTime();
    }

}
