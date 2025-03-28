package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 档口代发订单收件人对象 store_order_receive
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreOrderReceive extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 代发订单收件人
     */
    @TableId
    private Long storeOrderRcvId;

    /**
     * 代发订单档口ID
     */
    @Excel(name = "代发订单档口ID")
    private Long storeOrderId;

    /**
     * 代发地址ID
     */
    @Excel(name = "代发地址ID")
    private Long sysUserAddressId;

    /**
     * 收件人名称
     */
    @Excel(name = "收件人名称")
    private String receiveName;

    /**
     * 收件人电话
     */
    @Excel(name = "收件人电话")
    private String receivePhone;

    /**
     * 省code
     */
    @Excel(name = "省code")
    private String provinceCode;

    /**
     * 市code
     */
    @Excel(name = "市code")
    private String cityId;

    /**
     * 区(县)code
     */
    @Excel(name = "区(县)code")
    private String districtCode;

    /**
     * 详细地址
     */
    @Excel(name = "详细地址")
    private String detailAddress;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("storeOrderRcvId", getStoreOrderRcvId())
                .append("storeOrderId", getStoreOrderId())
                .append("sysUserAddressId", getSysUserAddressId())
                .append("receiveName", getReceiveName())
                .append("receivePhone", getReceivePhone())
                .append("provinceCode", getProvinceCode())
                .append("cityId", getCityId())
                .append("districtCode", getDistrictCode())
                .append("detailAddress", getDetailAddress())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
