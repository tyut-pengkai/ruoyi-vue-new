package com.ruoyi.web.controller.xkt.vo.express;

import lombok.Data;

/**
 * @author liangyq
 * @date 2025-04-17 16:40
 */
@Data
public class ZtoPrintOrderReqVO {
    /**
     * 运单号
     */
    private String billCode;
    /**
     * 打印类型(0:JPG, 1:PDF.默认为PDF)
     */
    private String printType;
    /**
     * 图片或PDF的Base64编码
     */
    private String result;
    /**
     * 消息签名dataDigest=base64(md5(result+billCode))
     */
    private String dataDigest;
}
