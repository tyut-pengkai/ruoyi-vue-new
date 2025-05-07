package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 广告营销每一轮竞价历史数据 advert_round_record
 *
 * @author liujiang
 * @date 2025-05-03
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class AdvertRoundRecord extends XktBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 广告营销每一轮历史竞价记录ID
     */
    @TableId
    private Long id;
    /**
     * 推广营销轮次位置ID
     */
    private Long advertRoundId;
    /**
     * 广告ID
     */
    private Long advertId;
    /**
     * 轮次ID
     */
    private Integer roundId;
    /**
     * 资源锁标识 位置枚举的推广位精确到A B C D E 等，时间范围的推广位精确到具体类型
     */
    private String symbol;
    /**
     * 投放状态
     */
    private Integer launchStatus;
    /**
     * 投放开始时间
     */
    private Date startTime;
    /**
     * 投放结束时间
     */
    private Date endTime;
    /**
     * 广告位置 A B C D E...  对应advert中的playNum
     */
    private String position;
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
