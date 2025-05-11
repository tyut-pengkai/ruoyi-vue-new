package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 广告营销每一轮 advert_round
 *
 * @author liujiang
 * @date 2025-05-03
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class AdvertRound extends XktBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 广告轮次位置ID
     */
    @TableId
    private Long id;
    /**
     * 推广展示类型 时间范围  位置枚举
     */
    private Integer showType;
    /**
     * 凭证日期
     */
    private Date voucherDate;
    /**
     * 广告ID
     */
    private Long advertId;
    /**
     * 推广类型
     */
    private Integer typeId;
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
     * 起拍价格
     */
    private BigDecimal startPrice;
    /**
     * 推广档口出价
     */
    private BigDecimal payPrice;
    /**
     * 只要有人出价：这个就被置为 已出价   晚上定时任务改为 竞价成功
     */
    private Integer biddingStatus;
    /**
     * 竞价状态 竞价成功  竞价失败的进入到 advert_round_record表中
     */
    private Integer biddingTempStatus;
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
