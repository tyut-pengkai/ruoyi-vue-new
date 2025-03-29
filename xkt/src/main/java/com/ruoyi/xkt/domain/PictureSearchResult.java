package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 以图搜款结果对象 picture_search_result
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PictureSearchResult extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 文件搜索结果ID
     */
    @TableId
    private Long id;

    /**
     * 搜索的文件ID
     */
    @Excel(name = "搜索的文件ID")
    private Long picSearchId;

    /**
     * 搜索结果的文件ID
     */
    @Excel(name = "搜索结果的文件ID")
    private Long fileId;

    /**
     * 搜索结果分类ID
     */
    @Excel(name = "搜索结果分类ID")
    private Integer categoryId;

    /**
     * 搜索结果评分
     */
    @Excel(name = "搜索结果评分")
    private BigDecimal score;

    /**
     * 排序描述
     */
    @Excel(name = "排序描述")
    private String sortExprValues;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("picSearchId", getPicSearchId())
                .append("fileId", getFileId())
                .append("categoryId", getCategoryId())
                .append("score", getScore())
                .append("sortExprValues", getSortExprValues())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
