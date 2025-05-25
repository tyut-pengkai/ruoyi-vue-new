package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
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
@Accessors(chain = true)
public class PictureSearch extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 文件搜索ID
     */
    @TableId
    private Long id;

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


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
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
