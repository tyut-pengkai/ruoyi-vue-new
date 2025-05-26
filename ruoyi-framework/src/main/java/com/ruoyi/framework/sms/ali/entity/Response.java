package com.ruoyi.framework.sms.ali.entity;

import cn.hutool.core.lang.Assert;
import lombok.Getter;

/**
 * @author liangyq
 */
@Getter
public abstract class Response {
    /**
     * api名称
     */
    private final String apiName;
    /**
     * 平台信息
     */
    private final String platform;
    /**
     * 参数
     */
    private final String params;
    /**
     * 返回信息
     */
    private final String responseText;
    /**
     * 结果记录数
     */
    private int count;

    public Response(String apiName, String platform, String params, String responseText) {
        Assert.notEmpty(apiName);
        Assert.notNull(platform);
        this.apiName = apiName;
        this.platform = platform;
        this.responseText = responseText;
        this.params = params;
    }

    /**
     * @return 是否成功
     */
    public abstract boolean success();

    /**
     * @return 是否调用的是本地历史结果(默认否)
     */
    public boolean isLocalCall() {
        return false;
    }

    /**
     * 增加结果记录数
     */
    public void increaseCount() {
        this.count++;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Response{" +
                "apiName='" + apiName + '\'' +
                ", platform='" + platform + '\'' +
                ", params='" + params + '\'' +
                ", responseText='" + responseText + '\'' +
                ", count=" + count +
                '}';
    }
}
