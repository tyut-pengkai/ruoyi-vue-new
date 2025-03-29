package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
     * sys_user.id
     */
    @Excel(name = "sys_user.id")
    private Long userId;

    /**
     * sys_file.id
     */
    @Excel(name = "sys_file.id")
    private Long fileId;

    /**
     * store_prod_file.id
     */
    @Excel(name = "store_prod_file.id")
    private Long storeProdFileId;

    /**
     * store.id
     */
    @Excel(name = "store.id")
    private Long storeId;

    /**
     * 档口名称
     */
    @Excel(name = "档口名称")
    private String storeName;

    /**
     * 商品货号
     */
    @Excel(name = "商品货号")
    private String prodArtNum;

    /**
     * 商品价格
     */
    @Excel(name = "商品价格")
    private BigDecimal price;

    /**
     * 商品标题
     */
    @Excel(name = "商品标题")
    private String prodTitle;

    /**
     * 凭证日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "凭证日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date voucherDate;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("fileId", getFileId())
                .append("storeProdFileId", getStoreProdFileId())
                .append("storeId", getStoreId())
                .append("storeName", getStoreName())
                .append("prodArtNum", getProdArtNum())
                .append("price", getPrice())
                .append("prodTitle", getProdTitle())
                .append("voucherDate", getVoucherDate())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
