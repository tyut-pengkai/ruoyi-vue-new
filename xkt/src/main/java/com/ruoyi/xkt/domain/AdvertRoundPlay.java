package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 广告营销每一轮播放 advert_round_play
 *
 * @author liujiang
 * @date 2025-05-03
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AdvertRoundPlay extends XktBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 广告轮次播放ID
     */
    @TableId
    private Long id;
    /**
     * 广告ID
     */
    private Long advertId;
    /**
     * 广告轮次ID
     */
    private Long advertRoundId;





    /**
     * 推广档口ID
     */
    private Long storeId;
    /**
     * 推广档口出价
     */
    private BigDecimal payPrice;
    /**
     * 竞价状态
     */
    private Integer biddingStatus;
    /**
     * 图片审核状态
     */
    private Integer picAuditStatus;
    /**
     * 图片是否设置 0 未设置 1已设置
     */
    private Integer picSet;
    /**
     * 推广图ID 对应sysFile.id
     */
    private Long picId;
    /**
     * 如果是推广商品，或者图及商品，则这里存放的是商品ID或者商品ID列表 eg:  1 或  1,2,3等
     */
    private String prodIdStr;
    /**
     * 图片设计（自主设计、平台设计）
     */
    private Integer picDesignType;
    /**
     * 系统拦截
     */
    private Integer sysIntercept;

}
