package com.ruoyi.xkt.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 档口商品服务对象 store_product_service
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreProductService extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口商品服务ID
     */
    private Long storeProdSvcId;

    /**
     * 档口商品ID
     */
    @Excel(name = "档口商品ID")
    private Long storeProdId;

    /**
     * 大小码及定制款可退
     */
    @Excel(name = "大小码及定制款可退")
    private String customRefund;

    /**
     * 30天包退（大小码及定制款不可退）
     */
    @Excel(name = "30天包退", readConverterExp = "大=小码及定制款不可退")
    private String thirtyDayRefund;

    /**
     * 一件起批
     */
    @Excel(name = "一件起批")
    private String oneBatchSale;

    /**
     * 退款72小时到账
     */
    @Excel(name = "退款72小时到账")
    private String refundWithinThreeDay;

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
                .append("storeProdSvcId", getStoreProdSvcId())
                .append("storeProdId", getStoreProdId())
                .append("customRefund", getCustomRefund())
                .append("thirtyDayRefund", getThirtyDayRefund())
                .append("oneBatchSale", getOneBatchSale())
                .append("refundWithinThreeDay", getRefundWithinThreeDay())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
