package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 全局变量对象 sys_global_variable
 *
 * @author zwgu
 * @date 2022-07-02
 */
public class SysGlobalVariable extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 变量ID
     */
    private Long id;

    /**
     * 变量名
     */
    @Excel(name = "变量名")
    private String name;

    /**
     * 变量值
     */
    @Excel(name = "变量值")
    private String value;

    /**
     * 变量描述
     */
    @Excel(name = "变量描述")
    private String description;

    /**
     * 是否检查token
     */
    @Excel(name = "是否检查token")
    private String checkToken;

    /**
     * 是否检查vip
     */
    @Excel(name = "是否检查vip")
    private String checkVip;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCheckToken() {
        return checkToken;
    }

    public void setCheckToken(String checkToken) {
        this.checkToken = checkToken;
    }

    public String getCheckVip() {
        return checkVip;
    }

    public void setCheckVip(String checkVip) {
        this.checkVip = checkVip;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("value", getValue())
                .append("description", getDescription())
                .append("checkToken", getCheckToken())
                .append("checkVip", getCheckVip())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
