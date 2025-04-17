package com.ruoyi.web.controller.xkt.vo.express;

import lombok.Data;

/**
 * @author liangyq
 * @date 2025-04-17 16:34
 */
@Data
public class ZtoPrintOrderRespVO {
    /**
     * 默认false，中通根据status=true来判断是否推送成功
     */
    private Boolean status;
    /**
     * 响应说明
     */
    private String message;
    /**
     * 响应结果
     */
    private String result;
    /**
     * 响应状态码
     */
    private String statusCode;

    public static ZtoPrintOrderRespVO success() {
        ZtoPrintOrderRespVO vo = new ZtoPrintOrderRespVO();
        vo.setStatus(true);
        vo.setMessage("success");
        vo.setResult("success");
        vo.setStatusCode("0");
        return vo;
    }

    public static ZtoPrintOrderRespVO failure() {
        ZtoPrintOrderRespVO vo = new ZtoPrintOrderRespVO();
        vo.setStatus(false);
        vo.setMessage("failure");
        vo.setResult("failure");
        vo.setStatusCode("0");
        return vo;
    }

}
