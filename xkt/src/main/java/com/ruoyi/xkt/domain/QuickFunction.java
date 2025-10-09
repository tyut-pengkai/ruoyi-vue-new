package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 档口快捷功能对象 store_quick_function
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class QuickFunction extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口快捷功能ID
     */
    @TableId
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 菜单ID
     */
    private Long menuId;

    /**
     * 排序
     */
    @Excel(name = "排序")
    private Integer orderNum;

}
