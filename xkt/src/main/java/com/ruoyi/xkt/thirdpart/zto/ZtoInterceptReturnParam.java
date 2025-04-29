package com.ruoyi.xkt.thirdpart.zto;

import lombok.Data;

/**
 * @author liangyq
 * @date 2025-04-29 18:13
 */
@Data
public class ZtoInterceptReturnParam {
    /**
     * 运单号
     */
    private String billCode;
    /**
     * 请求ID（幂等校验）
     */
    private String requestId;
    /**
     * 拦截类型:1 返回收件网点；2 返回寄件人地址；3 修改派送地址；4 退回指定地址（必填）
     */
    private Integer destinationType;
    /**
     * 外部业务单号
     */
    private String thirdBizNo;
}
