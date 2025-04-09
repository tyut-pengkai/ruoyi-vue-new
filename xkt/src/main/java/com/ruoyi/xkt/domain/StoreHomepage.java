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
 * 档口首页对象 store_homepage
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class StoreHomepage extends XktBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 档口首页ID
     */
    @TableId
    private Long id;

    /**
     * 档口ID
     */
    @Excel(name = "档口ID")
    private Long storeId;

    /**
     * 业务ID 1.不跳转 为null 2.跳转店铺 为storeId 3.跳转商品 为storeProdId
     */
    private Long bizId;

    /**
     * 跳转类型 1.不跳转 2.跳转店铺 3.跳转商品
     */
    private Integer jumpType;

    /**
     * 档口各位置类型
     */
    @Excel(name = "档口各位置类型")
    private Integer fileType;

    /**
     * 档口各位置文件ID
     */
    @Excel(name = "档口各位置文件ID")
    private Long fileId;

    /**
     * 排序
     */
    @Excel(name = "排序")
    private Integer orderNum;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("storeId", getStoreId())
                .append("fileType", getFileType())
                .append("fileId", getFileId())
                .append("orderNum", getOrderNum())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
