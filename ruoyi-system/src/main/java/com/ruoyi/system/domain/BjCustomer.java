package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 客户管理对象 bj_customer
 * 
 * @author ssq
 * @date 2024-10-05
 */
public class BjCustomer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 客户名称 */
    @Excel(name = "客户名称")
    private String name;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String tel;

    /** 负责人 */
    @Excel(name = "负责人")
    private String chargePerson;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setTel(String tel) 
    {
        this.tel = tel;
    }

    public String getTel() 
    {
        return tel;
    }
    public void setChargePerson(String chargePerson) 
    {
        this.chargePerson = chargePerson;
    }

    public String getChargePerson() 
    {
        return chargePerson;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("tel", getTel())
            .append("chargePerson", getChargePerson())
            .toString();
    }
}
