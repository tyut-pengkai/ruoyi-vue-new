package com.ruoyi.framework.sms.ali.entity;

import com.alibaba.fastjson2.JSON;
import com.aliyuncs.CommonResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * @author liangyq
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public class AliSmsResponse extends Response {

    private CommonResponse response;

    public AliSmsResponse(String params, CommonResponse response) {
        super("ALI-SMS", "ALI", params, JSON.toJSONString(response));
        this.response = response;
    }

    @Override
    public boolean success() {
        return response.getHttpResponse().isSuccess();
    }

    public Result get() {
        if (!response.getHttpResponse().isSuccess()) {
            return new Result("异常");
        }
        log.info(response.getData());
        return JSON.parseObject(response.getData(), Result.class);
    }
}
