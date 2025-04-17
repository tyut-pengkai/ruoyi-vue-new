package com.ruoyi.xkt.thirdpart.yto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyq
 * @date 2025-03-14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YtoPublicRequest {
    /**
     * 签名
     */
    private String sign;
    /**
     * 时间戳
     */
    private Long timestamp;
    /**
     * 传递的参数
     */
    private String param;
    /**
     * param格式
     */
    private EFormat format;

    public enum EFormat {
        JSON,
        XML
    }

}
