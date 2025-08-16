package com.ruoyi.xkt.thirdpart.zto;

import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liangyq
 * @date 2025-05-06 23:00
 */
@Data
public class ZtoTrackObj {

    @Data
    public static class Info {
        /**
         * 运单号
         */
        private String billCode;
        /**
         * 事件类型
         * <p>
         * GOT	                收件	    网点揽收
         *  DEPARTURE	        发件	    从网点或分拨中心发出
         *  ARRIVAL	            到件	    到达网点或分拨中心
         *  DISPATCH	        派件	    业务员派送
         *  RETURN_SCAN	        退件	    发生退回或改地址，具体类型见退改类型（returnType）字段
         *  RETURN_SIGNED	    退件签收	    已经退回至寄件客户
         *  INBOUND	            入站	    放入快递超市/自提柜/第三方代理点等
         *  HANDOVERSCAN_SIGNED 第三方妥投	入站后妥投成功
         *  DEPARTURE_SIGNED    出站签收	    客户从快递超市/自提柜/第三方代理点等取出
         *  SIGNED	            签收	    客户正常签收
         *  PROBLEM	            问题件	    网点或中心登记的问题件，问题件类型在问题件编码（problemCode）字段体现
         */
        private String action;
        /**
         * 操作节点编码
         */
        private String facilityCode;
        /**
         * 操作节点名称
         */
        private String facilityName;
        /**
         * 操作节点服务电话
         */
        private String facilityContactPhone;
        /**
         * 操作节点所属城市
         */
        private String city;
        /**
         * 操作时间
         */
        private String actionTime;
        /**
         * 下一站编码
         */
        private String nextNodeCode;
        /**
         * 下一站名称
         */
        private String nextNodeName;
        /**
         * 下一站城市
         */
        private String nextCity;
        /**
         * 末端品牌编码
         */
        private String comCode;
        /**
         * 末端品牌名称
         */
        private String comName;
        /**
         * 物流详情描述
         */
        private String desc;
        /**
         * 快递员名称
         */
        private String courier;
        /**
         * 快递员电话
         */
        private String courierPhone;
        /**
         * 签收人名称
         */
        private String expressSigner;
        /**
         * 地址
         */
        private String address;
        /**
         * 问题件编码
         */
        private String problemCode;
        /**
         * （旧）退改类型
         */
        private String returnType;
        /**
         * （新）退改类型
         */
        private String returnUpdateType;
        /**
         * 备注
         */
        private String remark;
        /**
         * 备注1
         */
        private String remark1;
        /**
         * 备注2
         */
        private String remark2;

    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
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

        public String toJsonStr() {
            return JSONUtil.toJsonStr(this);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        /**
         * 消息内容
         */
        private String data;
        /**
         * 消息签名
         * <p>
         * data_digest=base64(md5(data+AppSecret))
         */
        private String data_digest;
        /**
         * 消息类型（Traces）
         */
        private String msg_type;
        /**
         * 应用appKey
         */
        private String company_id;

        public Request(HttpServletRequest servletRequest) {
            this(servletRequest.getParameter("data"),
                    servletRequest.getParameter("data_digest"),
                    servletRequest.getParameter("msg_type"),
                    servletRequest.getParameter("company_id"));
        }
    }
}
