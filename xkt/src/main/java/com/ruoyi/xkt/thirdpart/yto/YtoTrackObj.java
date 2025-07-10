package com.ruoyi.xkt.thirdpart.yto;

import cn.hutool.core.util.XmlUtil;
import com.ruoyi.common.core.text.CharsetKit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author liangyq
 * @date 2025-05-06 23:00
 */
@Data
public class YtoTrackObj {

    private Info UpdateInfo;

    @Data
    public static class Info {
        /**
         * 物流公司ID（YTO）
         */
        private String logisticProviderID;
        /**
         * vip客户标识(客户编号)
         */
        private String clientID;
        /**
         * 运单号
         */
        private String mailNo;
        /**
         * 物流号
         */
        private String txLogisticID;
        /**
         * 通知类型STATUS：物流状态
         */
        private String infoType;
        /**
         * 操作，固定为：ACCEPT 接单;ORDER_BOOKING 修改预约取件时间;GOT 已揽收;NOT_SEND 揽收失败;ARRIVAL 已收入;DEPARTURE 已发出;SENT_SCAN 派件;INBOUND 自提柜入柜;SIGNED 签收成功;FAILED 签收失败;TMS_RETURN 退回;FORWARDING 转寄;AIRSEND 航空发货;AIRPICK 航空提货
         */
        private String infoContent;
        /**
         * 事件发生时间
         */
        private Date acceptTime;
        /**
         * 备注或失败原因（值为中文原因或备注）
         */
        private String remark;
        /**
         * 揽收重量
         */
        private String weight;
        /**
         * 网点名称
         */
        private String orgName;
        /**
         * 网点编号
         */
        private String orgCode;
        /**
         * 网点客服电话
         */
        private String orgPhone;
        /**
         * 业务员名称
         */
        private String empName;
        /**
         * 业务员编号
         */
        private String empCode;
        /**
         * 当前操作城市
         */
        private String city;
        /**
         * 当前操作区或者县
         */
        private String district;
        /**
         * 签收人
         */
        private String signedName;
        /**
         * 该状态操作人员，签收、派送、揽件
         */
        private String deliveryName;
        /**
         * 联系方式（包括手机，电话等）
         */
        private String contactInfo;
        /**
         * 异常原因
         */
        private String questionCause;
        /**
         * 新的预约取件开始时间
         */
        private Date bookingStartTime;
        /**
         * 新的预约取件结束时间
         */
        private Date bookingEndTime;
        /**
         * 扩展字段
         */
        private String extendFields;
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        /**
         * 物流公司ID
         */
        private String logisticProviderID;
        /**
         * 物流号
         */
        private String txLogisticID;
        /**
         * 成功失败标识 true-成功;false-失败
         */
        private Boolean success;
        /**
         * 失败原因
         */
        private String reason;

        public String toXmlStr() {
            return XmlUtil.toStr(XmlUtil.beanToXml(this), CharsetKit.UTF_8, false, true);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        /**
         * 消息内容
         */
        private String logistics_interface;
        /**
         * 消息签名
         */
        private String data_digest;
        /**
         * 客户编码（电商标识）
         */
        private String clientId;
        /**
         * 订单类型（online:在线下单，offline:线下下单）
         */
        private String type;

        public Request(HttpServletRequest servletRequest) {
            this(servletRequest.getParameter("logistics_interface"),
                    servletRequest.getParameter("data_digest"),
                    servletRequest.getParameter("clientId"),
                    servletRequest.getParameter("type"));
        }
    }
}
