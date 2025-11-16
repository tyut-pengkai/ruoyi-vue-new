package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 档口会员 store_member
 *
 * @author liujiang
 * @date 2025-06-25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreMember extends XktBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 广告ID
     */
    @TableId
    private Long id;
    /**
     * 档口ID
     */
    private Long storeId;
    /**
     * 会员等级
     */
    private Integer level;
    /**
     * 会员状态 1 未购买待审核 2 已购买待审核  3 审核通过（正式使用） 4 审核拒绝
     */
    private Integer memberStatus;
    /**
     * 支付金额
     */
    private BigDecimal payPrice;
    /**
     * 生效日期 yyyy-MM-dd
     */
    private Date startTime;
    /**
     * 截止日期 yyyy-MM-dd
     */
    private Date endTime;
    /**
     * 单据日期
     */
    private Date voucherDate;

}
