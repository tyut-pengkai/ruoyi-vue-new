package com.ruoyi.xkt.thirdpart.yto;

import lombok.Data;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-15 19:20
 */
@Data
public class YtoCreateOrderParam {
    /**
     * 物流单号，打印拉取运单号前，物流单号和渠道唯一确定一笔快递物流订单。注：最低长度为7
     */
    private String logisticsNo;
    /**
     * 寄件人姓名
     */
    private String senderName;
    /**
     * 寄件人省名称
     */
    private String senderProvinceName;
    /**
     * 寄件人市名称
     */
    private String senderCityName;
    /**
     * 寄件人区县名称
     */
    private String senderCountyName;
    /**
     * 寄件人乡镇名称
     */
    private String senderTownName;
    /**
     * 寄件人详细地址
     */
    private String senderAddress;
    /**
     * 寄件人联系电话
     */
    private String senderMobile;
    /**
     * 收件人姓名
     */
    private String recipientName;
    /**
     * 收件人省名称
     */
    private String recipientProvinceName;
    /**
     * 收件人市名称
     */
    private String recipientCityName;
    /**
     * 收件人区县名称
     */
    private String recipientCountyName;
    /**
     * 收件人乡镇名称
     */
    private String recipientTownName;
    /**
     * 收件人详细地址
     */
    private String recipientAddress;
    /**
     * 收件人联系电话
     */
    private String recipientMobile;
    /**
     * 备注，打印在面单上的备注内容
     */
    private String remark;
    /**
     * 寄件码或取件码
     */
    private String gotCode;
    /**
     * 增值服务列表
     */
    private List<OrderIncrement> increments;
    /**
     * 物品列表，注：支持20个以内
     */
    private List<OrderGoods> goods;
    /**
     * 预约上门取件开始时间，”yyyy-MM-dd HH:mm:ss”格式化，注：预约取件时间规则为下单当天的00:00:00至下单当天+6天的23:59:59。
     */
    private String startTime;
    /**
     * 预约上门取件结束时间，”yyyy-MM-dd HH:mm:ss”格式化，注：预约取件时间规则为下单当天的00:00:00至下单当天+6天的23:59:59。
     */
    private String endTime;
    /**
     * 客户业务类型，可以对客户订单进行业务或渠道区分。
     */
    private String cstBusinessType;
    /**
     * 客户的订单号
     */
    private String cstOrderNo;
    /**
     * 实名信息
     */
    private String realNameInfo;
    /**
     * 下单总重量，单位：千克，小数后三位
     */
    private String weight;
    /**
     * 产品编号：YZD-圆准达；XTCTK-同城特快；HKJ-航空件；PK-普快，默认PK
     */
    private String productCode;

    @Data
    public static class OrderIncrement {
        /**
         * 增值类型：1-代收货款；2-到付；4-保价，注：增值服务不能同时选择代收和到付。
         */
        private Integer type;
        /**
         * 金额，单位：元，代收货款金额：[3,20000]；到付金额：[1,5000]；保价金额：[100,30000]。
         */
        private String amount;
    }

    @Data
    public static class OrderGoods {
        /**
         * 物品名称
         */
        private String name;
        /**
         * 重量，单位：千克
         */
        private String weight;
        /**
         * 长，单位：米
         */
        private String length;
        /**
         * 宽，单位：米
         */
        private String width;
        /**
         * 高，单位：米
         */
        private String height;
        /**
         * 单价，单位：元
         */
        private String price;
        /**
         * 数量
         */
        private Integer quantity;
    }

    @Data
    public static class RealNameInfo {
        /**
         * 姓名
         */
        private String name;
        /**
         * 证件号
         */
        private String cerNum;
        /**
         * 证件类型： 11：居民身份证；12：临时居民身份证；13：户口簿；14：中国人民解放军军人身份证件；15：中国人民武装警察身份证件；16：港澳居民来往内地通行证；17：台湾居民通往大陆通行证；18：外国公民护照；19：中国公民护照；20：港澳台居民居住证；101：机构代码；102：税务登记号：103：统一社会信用代码
         */
        private String cerType;
    }


}
