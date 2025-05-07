package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 档口客户每天销售数据统计 daily_sale_customer
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class DailySaleCustomer extends XktBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 每日销售统计ID
     */
    @TableId
    private Long id;
    /**
     * 档口ID
     */
    private Long storeId;
    /**
     * 档口客户ID
     */
    private Long storeCusId;
    /**
     * 销售金额
     */
    private BigDecimal saleAmount;
    /**
     * 退货金额
     */
    private BigDecimal returnAmount;
    /**
     * 销售数量
     */
    private Integer saleNum;
    /**
     * 退货数量
     */
    private Integer returnNum;
    /**
     * 统计时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date voucherDate;

}
