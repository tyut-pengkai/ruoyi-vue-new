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
public class FeiShuTokenResp {
    @JSONField(name = "tenant_access_token")
    private String tenantAccessToken;
    @JSONField(name = "expire")
    private String expire;
}
