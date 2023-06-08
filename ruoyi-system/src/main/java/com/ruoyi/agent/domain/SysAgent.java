package com.ruoyi.agent.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.agent.domain.vo.TemplateInfoVo;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.TreeEntity;
import com.ruoyi.common.core.domain.entity.SysUser;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 代理管理对象 sys_agent
 *
 * @author zwgu
 * @date 2022-06-11
 */
public class SysAgent extends TreeEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 代理ID
     */
    private Long agentId;

    /**
     * 上级代理ID
     */
    @Excel(name = "上级代理ID")
    private Long parentAgentId;

    /**
     * 代理树
     */
    @Excel(name = "代理树")
    private String path;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private Long userId;

    /**
     * 允许发展下级
     */
    @Excel(name = "允许发展下级")
    private String enableAddSubagent;

    /**
     * 代理过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "代理过期时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime;

    /**
     * 代理状态（0正常 1停用）
     */
    @Excel(name = "代理状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 所属账号信息
     */
    @Excels({
            @Excel(name = "用户账号", targetAttr = "userName", type = Excel.Type.EXPORT),
            @Excel(name = "用户昵称", targetAttr = "nickName", type = Excel.Type.EXPORT)
    })
    private SysUser user;

    /**
     * 上级账号信息
     */
    @Excels({
            @Excel(name = "上级账号", targetAttr = "userName", type = Excel.Type.EXPORT),
            @Excel(name = "上级昵称", targetAttr = "nickName", type = Excel.Type.EXPORT)
    })
    private SysUser parentUser;

    private List<TemplateInfoVo> templateList = new ArrayList<>();

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Long getParentAgentId() {
        return parentAgentId;
    }

    public void setParentAgentId(Long parentAgentId) {
        this.parentAgentId = parentAgentId;
    }

    public SysUser getParentUser() {
        return parentUser;
    }

    public void setParentUser(SysUser parentUser) {
        this.parentUser = parentUser;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @NotNull(message = "用户账号不能为空")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEnableAddSubagent() {
        return enableAddSubagent;
    }

    public void setEnableAddSubagent(String enableAddSubagent) {
        this.enableAddSubagent = enableAddSubagent;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public SysUser getUser() {
        return user;
    }

    public void setUser(SysUser user) {
        this.user = user;
    }

    public List<TemplateInfoVo> getTemplateList() {
        return templateList;
    }

    public void setTemplateList(List<TemplateInfoVo> templateList) {
        this.templateList = templateList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("agentId", getAgentId())
                .append("parentAgentId", getParentAgentId())
                .append("userId", getUserId())
                .append("enableAddSubagent", getEnableAddSubagent())
                .append("expireTime", getExpireTime())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
