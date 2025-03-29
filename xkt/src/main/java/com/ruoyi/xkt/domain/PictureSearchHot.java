package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 图搜热款对象 picture_search_hot
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PictureSearchHot extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 图搜热款ID
     */
    @TableId
    private Long id;

    /**
     * store_prod_file.id
     */
    @Excel(name = "store_prod_file.id")
    private Long storeProdFileId;

    /**
     * sys_file.id
     */
    @Excel(name = "sys_file.id")
    private Long fileId;

    /**
     * store.id
     */
    @Excel(name = "store.id")
    private Long storeId;

    /**
     * 搜索次数（包括主动搜索和搜索结果）
     */
    @Excel(name = "搜索次数", readConverterExp = "包=括主动搜索和搜索结果")
    private Integer searchCount;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("storeProdFileId", getStoreProdFileId())
                .append("fileId", getFileId())
                .append("storeId", getStoreId())
                .append("searchCount", getSearchCount())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
