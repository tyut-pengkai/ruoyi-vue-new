package com.ruoyi.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品类目 sys_product_category
 *
 * @author ruoyi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysProductCategory extends XktBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 字典主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 字典名称
     */
    @Excel(name = "字典名称")
    private String name;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 显示顺序
     */
    private Integer orderNum;
    /**
     * 图标url
     */
    private String iconUrl;
    /**
     * 状态（0正常 1停用）
     */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 备注
     */
    private String remark;

    @Override
    public String toString() {
        return "SysProductCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", orderNum=" + orderNum +
                ", status='" + status + '\'' +
                ", remark='" + remark + '\'' +
                "} " + super.toString();
    }
}
