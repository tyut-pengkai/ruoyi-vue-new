package com.ruoyi.xkt.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 以图搜款对象 picture_search
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PictureSearch extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 文件搜索ID
     */
    private Long picSearchId;

    /**
     * 搜索的文件ID
     */
    @Excel(name = "搜索的文件ID")
    private Long searchFileId;

    /**
     * 使用以图搜款用户ID
     */
    @Excel(name = "使用以图搜款用户ID")
    private Long userId;

    /**
     * 搜索日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "搜索日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date voucherDate;

    /**
     * 版本号
     */
    @Excel(name = "版本号")
    private Long version;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("picSearchId", getPicSearchId())
                .append("searchFileId", getSearchFileId())
                .append("userId", getUserId())
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
