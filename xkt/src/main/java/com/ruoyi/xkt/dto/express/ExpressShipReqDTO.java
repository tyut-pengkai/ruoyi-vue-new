package com.ruoyi.xkt.dto.express;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-16 15:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpressShipReqDTO {
    /**
     * 请求号
     */
    private String expressReqNo;
    /**
     * 发货人-名称
     */
    private String originContactName;
    /**
     * 发货人-电话
     */
    private String originContactPhoneNumber;
    /**
     * 发货人-省编码
     */
    private String originProvinceCode;
    private String originProvinceName;
    /**
     * 发货人-市编码
     */
    private String originCityCode;
    private String originCityName;
    /**
     * 发货人-区县编码
     */
    private String originCountyCode;
    private String originCountyName;
    /**
     * 发货人-详细地址
     */
    private String originDetailAddress;
    /**
     * 收货人-名称
     */
    private String destinationContactName;
    /**
     * 收货人-电话
     */
    private String destinationContactPhoneNumber;
    /**
     * 收货人-省编码
     */
    private String destinationProvinceCode;
    private String destinationProvinceName;
    /**
     * 收货人-市编码
     */
    private String destinationCityCode;
    private String destinationCityName;
    /**
     * 收货人-区县编码
     */
    private String destinationCountyCode;
    private String destinationCountyName;
    /**
     * 收货人-详细地址
     */
    private String destinationDetailAddress;
    /**
     * 物品信息
     */
    private List<OrderItem> orderItems;
    /**
     * 备注
     */
    private String remark;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItem {
        /**
         * 商品货号
         */
        private String prodArtNum;
        /**
         * 颜色名称
         */
        private String colorName;
        /**
         * 商品尺码
         */
        private Integer prodSize;
        /**
         * 货品名称
         */
        private String name;
        /**
         * 商品分类
         */
        private String category;
        /**
         * 商品材质
         */
        private String material;
        /**
         * 大小（长,宽,高）(单位：厘米), 用半角的逗号来分隔长宽高
         */
        private String size;
        /**
         * 重量（单位：克)
         */
        private Long weight;
        /**
         * 单价(单位:元)
         */
        private Integer unitprice;
        /**
         * 货品数量
         */
        private Integer quantity;
        /**
         * 货品备注
         */
        private String remark;
    }

    public String getGoodsSummary() {
        if (CollUtil.isEmpty(orderItems)) {
            return null;
        }
        List<String> list = new ArrayList<>(2);
        for (int i = 0; i < orderItems.size(); i++) {
            if (i > 1) {
                list.add("...");
                break;
            }
            OrderItem item = orderItems.get(i);
            list.add(StrUtil.emptyIfNull(item.getProdArtNum()) + " " + item.getColorName() + " " + item.getProdSize() +
                    "*" + item.getQuantity());
        }
        return StrUtil.join(";", list);
    }

}
