package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 档口商品文件对象 store_product_file
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class StoreProductFile extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口商品文件ID
     */
    @TableId
    private Long id;

    /**
     * 档口商品ID
     */
    @Excel(name = "档口商品ID")
    private Long storeProdId;

    /**
     * 档口ID
     */
    @Excel(name = "档口ID")
    private Long storeId;

    /**
     * 系统文件ID
     */
    @Excel(name = "系统文件ID")
    private Long fileId;

    /**
     * 文件类型（主图、视频、下载）
     */
    @Excel(name = "文件类型", readConverterExp = "主=图、视频、下载")
    private String fileType;

    /**
     * 文件大小（M）
     */
    @Excel(name = "文件大小", readConverterExp = "M=")
    private BigDecimal fileSize;

    /**
     * 排序
     */
    @Excel(name = "排序")
    private Integer orderNum;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("storeProdId", getStoreProdId())
                .append("fileId", getFileId())
                .append("fileType", getFileType())
                .append("fileSize", getFileSize())
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
