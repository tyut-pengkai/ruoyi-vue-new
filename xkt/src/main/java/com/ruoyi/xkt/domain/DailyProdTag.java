package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 档口商品每天的标签更新 daily_prod_tag
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Builder
public class DailyProdTag extends XktBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 每日标签统计ID
     */
    @TableId
    private Long id;
    /**
     * 档口ID
     */
    private Long storeId;
    /**
     * 档口商品ID
     */
    private Long storeProdId;
    /**
     * 标签类型
     */
    private Integer type;
    /**
     * 具体标签
     */
    private String tag;
    /**
     * 统计时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date voucherDate;

}
