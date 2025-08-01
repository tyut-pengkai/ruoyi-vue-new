package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
     * 文件类型（1主图、2视频、3下载）
     */
    @Excel(name = "文件类型", readConverterExp = "主=图、视频、下载")
    private Integer fileType;

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

    /**
     * 图包处理状态 图包状态[1:非图包/不处理 2:待处理 3:处理中 4:已处理 5:处理异常]
     */
    private Integer picZipStatus;

}
