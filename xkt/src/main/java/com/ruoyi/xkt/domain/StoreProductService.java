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
 * 档口商品服务对象 store_product_service
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class StoreProductService extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口商品服务ID
     */
    @TableId
    private Long id;

    /**
     * 档口商品ID
     */
    @Excel(name = "档口商品ID")
    private Long storeProdId;

    /**
     * 大小码及定制款可退 （0未勾选 1代表勾选）
     */
    @Excel(name = "大小码及定制款可退")
    private String customRefund;

    /**
     * 30天包退（大小码及定制款不可退） （0未勾选 1代表勾选）
     */
    @Excel(name = "30天包退", readConverterExp = "大=小码及定制款不可退")
    private String thirtyDayRefund;

    /**
     * 一件起批 （0未勾选 1代表勾选）
     */
    @Excel(name = "一件起批")
    private String oneBatchSale;

    /**
     * 退款72小时到账 （0未勾选 1代表勾选）
     */
    @Excel(name = "退款72小时到账")
    private String refundWithinThreeDay;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
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
