package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 档口客户对象 store_customer
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreCustomer extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口客户ID
     */
    @TableId
    private Long id;

    /**
     * 档口ID
     */
    @Excel(name = "档口ID")
    private Long storeId;

    /**
     * 客户名称
     */
    @Excel(name = "客户名称")
    private String cusName;

    /**
     * 客户联系电话
     */
    @Excel(name = "客户联系电话")
    private String phone;

    /**
     * 客户备注
     */
    @Excel(name = "客户备注")
    private String remark;

    /**
     * 大小码加价 0 不加 1加价
     */
    private Integer addOverPrice;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("storeId", getStoreId())
                .append("cusName", getCusName())
                .append("phone", getPhone())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
