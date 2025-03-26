package com.ruoyi.xkt.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 用户收货地址对象 user_address
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserAddress extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户地址ID
     */
    private Long userAddrId;

    /**
     * sys_user.id
     */
    @Excel(name = "sys_user.id")
    private Long userId;

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
    private String cityCode;

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

    /**
     * 版本号
     */
    @Excel(name = "版本号")
    private Long version;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("userAddrId", getUserAddrId())
                .append("userId", getUserId())
                .append("receiveName", getReceiveName())
                .append("receivePhone", getReceivePhone())
                .append("provinceCode", getProvinceCode())
                .append("cityCode", getCityCode())
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
