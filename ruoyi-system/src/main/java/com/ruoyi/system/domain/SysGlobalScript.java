package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.enums.ScriptLanguage;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 全局脚本对象 sys_global_script
 *
 * @author zwgu
 * @date 2022-05-19
 */
public class SysGlobalScript extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 脚本ID
     */
    private Long scriptId;

    /**
     * 脚本名
     */
    @Excel(name = "脚本名")
    private String name;

    /**
     * 脚本描述
     */
    @Excel(name = "脚本描述")
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

    /**
     * 脚本内容
     */
    @Excel(name = "脚本内容")
    private String content;

    /**
     * 脚本语言
     */
    @Excel(name = "脚本语言")
    private ScriptLanguage language;

    /**
     * 脚本Key
     */
    @Excel(name = "脚本Key")
    private String scriptKey;

    /**
     * 脚本参数
     */
    @TableField(exist = false)
    private String scriptParams;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    public Long getScriptId() {
        return scriptId;
    }

    public void setScriptId(Long scriptId) {
        this.scriptId = scriptId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ScriptLanguage getLanguage() {
        return language;
    }

    public void setLanguage(ScriptLanguage language) {
        this.language = language;
    }

    public String getScriptKey() {
        return scriptKey;
    }

    public void setScriptKey(String scriptKey) {
        this.scriptKey = scriptKey;
    }

    public String getScriptParams() {
        return scriptParams;
    }

    public void setScriptParams(String scriptParams) {
        this.scriptParams = scriptParams;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("scriptId", getScriptId())
                .append("name", getName())
                .append("description", getDescription())
                .append("checkToken", getCheckToken())
                .append("checkVip", getCheckVip())
                .append("content", getContent())
                .append("language", getLanguage())
                .append("scriptKey", getScriptKey())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
