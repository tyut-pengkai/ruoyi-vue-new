package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 广告营销 advert
 *
 * @author liujiang
 * @date 2025-05-03
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Advert extends XktBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 广告ID
     */
    @TableId
    private Long id;
    /**
     * 每一个广告位基础锁符号 10位大小写、字母组成的字符串
     */
    private String basicSymbol;
    /**
     * 每个档口可以购买当前广告位数量限制
     */
    private Integer storeBuyLimit;
    /**
     * 推广平台 电脑端 、APP
     */
    private Integer platformId;
    /**
     * 推广类型
     */
    private Integer typeId;
    /**
     * 推广展示类型 时间范围  位置枚举
     */
    private Integer showType;
    /**
     * 推广tab
     */
    private Integer tabId;
    /**
     * 上线状态
     */
    private Integer onlineStatus;
    /**
     * 展示类型 推广图、商品、推广图及商品
     */
    private Integer displayType;
    /**
     * 起拍价格
     */
    private BigDecimal startPrice;
    /**
     * 播放间隔（单位：天）
     */
    private Integer playInterval;
    /**
     * 播放轮次
     */
    private Integer playRound;
    /**
     * 播放数量
     */
    private Integer playNum;
    /**
     * 推广范例图片ID
     */
    private Long examplePicId;
    /**
     * 推广图片尺寸
     */
    private String picPixel;
    /**
     * 推广图片大小
     */
    private String picSize;
    /**
     * 如果是播放商品，或者图及商品  最多可容纳的商品数量
     */
    private Integer prodMaxNum;
    /**
     * 推广折扣类型（现金、直接打折）
     */
    private Integer discountType;
    /**
     * 折扣数额（现金：直接输入折扣金额；直接打折：输入折后数值）
     */
    private BigDecimal discountAmount;
    /**
     * 折扣生效时间 yyyy-MM-dd HH:mm:ss
     */
    private Date discountStartTime;
    /**
     * 折扣失效时间 yyyy-MM-dd HH:mm:ss
     */
    private Date discountEndTime;

}
