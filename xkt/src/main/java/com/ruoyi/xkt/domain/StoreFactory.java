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
 * 档口合作工厂对象 store_factory
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class StoreFactory extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口工厂ID
     */
    @TableId
    private Long id;
    /**
     * store.id
     */
    @Excel(name = "store.id")
    private Long storeId;
    /**
     * 工厂名称
     */
    @Excel(name = "工厂名称")
    private String facName;
    /**
     * 工厂地址
     */
    @Excel(name = "工厂地址")
    private String facAddress;
    /**
     * 联系人
     */
    private String facContact;
    /**
     * 工厂联系电话
     */
    @Excel(name = "工厂联系电话")
    private String facPhone;
    /**
     * 备注
     */
    private String remark;

}
