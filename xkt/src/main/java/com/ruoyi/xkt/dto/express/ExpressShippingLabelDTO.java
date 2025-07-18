package com.ruoyi.xkt.dto.express;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 快递面单
 *
 * @author liangyq
 * @date 2025-07-18
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpressShippingLabelDTO {
    /**
     * ID
     */
    private Long id;
    /**
     * 运单号
     */
    private String expressWaybillNo;
    /**
     * 物流ID
     */
    private Long expressId;
    /**
     * 服务类型
     */
    private String vasType;
    /**
     * 转运代码
     */
    private String mark;
    /**
     * 短转运代码
     */
    private String shortMark;
    /**
     * 集包地
     */
    private String bagAddr;
    /**
     * 最后打印时间
     */
    private Date lastPrintTime;
    /**
     * 打印次数
     */
    private Integer printCount;
    /**
     * 商品信息
     */
    private String goodsInfo;
    /**
     * 备注
     */
    private String remark;
    /**
     * 发货人-名称
     */
    private String originContactName;
    /**
     * 发货人-电话
     */
    private String originContactPhoneNumber;
    /**
     * 发货人-省
     */
    private String originProvinceName;
    /**
     * 发货人-市
     */
    private String originCityName;
    /**
     * 发货人-区县
     */
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
     * 收货人-省
     */
    private String destinationProvinceName;
    /**
     * 收货人-市
     */
    private String destinationCityName;
    /**
     * 收货人-区县
     */
    private String destinationCountyName;
    /**
     * 收货人-详细地址
     */
    private String destinationDetailAddress;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    public ExpressShippingLabelDTO(String originContactName, String originContactPhoneNumber, String originProvinceName,
                                   String originCityName, String originCountyName, String originDetailAddress,
                                   String destinationContactName, String destinationContactPhoneNumber,
                                   String destinationProvinceName, String destinationCityName,
                                   String destinationCountyName, String destinationDetailAddress) {
        this.originContactName = originContactName;
        this.originContactPhoneNumber = originContactPhoneNumber;
        this.originProvinceName = originProvinceName;
        this.originCityName = originCityName;
        this.originCountyName = originCountyName;
        this.originDetailAddress = originDetailAddress;
        this.destinationContactName = destinationContactName;
        this.destinationContactPhoneNumber = destinationContactPhoneNumber;
        this.destinationProvinceName = destinationProvinceName;
        this.destinationCityName = destinationCityName;
        this.destinationCountyName = destinationCountyName;
        this.destinationDetailAddress = destinationDetailAddress;
    }
}
