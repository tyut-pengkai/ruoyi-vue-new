package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户浏览历史对象 user_browsing_history
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserBrowsingHistory extends XktBaseEntity {

    private static final long serialVersionUID = 1L;
    /**
     * 用户浏览足迹ID
     */
    @TableId
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 档口ID
     */
    private Long storeId;
    /**
     * 档口名称
     */
    private String storeName;
    /**
     * 商品ID
     */
    private Long storeProdId;
    /**
     * 商品货号
     */
    private String prodArtNum;
    /**
     * 商品标题
     */
    private String prodTitle;
    /**
     * 第一张主图路径
     */
    private String mainPicUrl;
    /**
     * 商品价格
     */
    private BigDecimal price;
    /**
     * 浏览日期
     */
    private Date browsingTime;

}
