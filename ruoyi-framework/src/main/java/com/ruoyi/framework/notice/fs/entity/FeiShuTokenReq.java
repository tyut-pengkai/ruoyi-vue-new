package com.ruoyi.framework.notice.fs.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyuqi
 * @date 2021/7/14 14:12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeiShuTokenReq {
    @JSONField(name = "app_id")
    private String appId;
    @JSONField(name = "app_secret")
    private String appSecret;
}
