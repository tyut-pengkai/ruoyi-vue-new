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

    /** 修改下级代理密码 */
    @Excel(name = "修改下级代理密码")
    private String enableUpdateSubagentPassword;

    /** 修改下级代理状态 */
    @Excel(name = "修改下级代理状态")
    private String enableUpdateSubagentStatus;

    /** 给下级代理加款 */
    @Excel(name = "给下级代理加款")
    private String enableUnbindAppUser;

    /** 用户解冻 */
    @Excel(name = "用户解冻")
    private String enableUpdateAppUserStatus0;

    /** 用户冻结 */
    @Excel(name = "用户冻结")
    private String enableUpdateAppUserStatus1;

    /** 修改用户时间 */
    @Excel(name = "修改用户时间")
    private String enableUpdateAppUserTime;

    /** 修改用户点数 */
    @Excel(name = "修改用户点数")
    private String enableUpdateAppUserPoint;

    /** 修改登录用户数量限制 */
    @Excel(name = "修改登录用户数量限制")
    private String enableUpdateAppUserLoginLimitU;

    /** 修改登录机器数量限制 */
    @Excel(name = "修改登录机器数量限制")
    private String enableUpdateAppUserLoginLimitM;

    /** 修改软件用户自定义参数 */
    @Excel(name = "修改软件用户自定义参数")
    private String enableUpdateAppUserCustomParams;

    /** 修改软件用户备注 */
    @Excel(name = "修改软件用户备注")
    private String enableUpdateAppUserRemark;

    /** 查看用户联系方式 */
    @Excel(name = "查看用户联系方式")
    private String enableViewUserContact;

    /** 修改用户联系方式 */
    @Excel(name = "修改用户联系方式")
    private String enableUpdateUserContact;

    /** 修改用户密码 */
    @Excel(name = "修改用户密码")
    private String enableUpdateUserPassword;

    /** 修改用户备注 */
    @Excel(name = "修改用户备注")
    private String enableUpdateUserRemark;

    /** 修改卡密时间 */
    @Excel(name = "修改卡密时间")
    private String enableUpdateCardTime;

    /** 修改卡密点数 */
    @Excel(name = "修改卡密点数")
    private String enableUpdateCardPoint;

    /** 修改卡密登录用户数量限制 */
    @Excel(name = "修改卡密登录用户数量限制")
    private String enableUpdateCardLoginLimitU;

    /** 修改卡密机器数量限制 */
    @Excel(name = "修改卡密机器数量限制")
    private String enableUpdateCardLoginLimitM;

    /** 修改卡密自定义参数 */
    @Excel(name = "修改卡密自定义参数")
    private String enableUpdateCardCustomParams;

    /** 修改卡密备注 */
    @Excel(name = "修改卡密备注")
    private String enableUpdateCardRemark;

    /** 卡密解冻 */
    @Excel(name = "卡密解冻")
    private String enableUpdateCardStatus0;

    /** 卡密冻结 */
    @Excel(name = "卡密冻结")
    private String enableUpdateCardStatus1;

    /** 批量换卡 */
    @Excel(name = "批量换卡")
    private String enableBatchCardReplace;

    /** 生成卡密 */
    @Excel(name = "生成卡密")
    private String enableAddCard;

    /** 删除下级代理 */
    @Excel(name = "删除下级代理")
    private String enableDeleteSubagent;

    /** 删除软件用户 */
    @Excel(name = "删除软件用户")
    private String enableDeleteAppUser;

    /** 删除卡密 */
    @Excel(name = "删除卡密")
    private String enableDeleteCard;

    public String getEnableDeleteSubagent() {
        return enableDeleteSubagent;
    }

    public void setEnableDeleteSubagent(String enableDeleteSubagent) {
        this.enableDeleteSubagent = enableDeleteSubagent;
    }

    public String getEnableDeleteAppUser() {
        return enableDeleteAppUser;
    }

    public void setEnableDeleteAppUser(String enableDeleteAppUser) {
        this.enableDeleteAppUser = enableDeleteAppUser;
    }

    public String getEnableDeleteCard() {
        return enableDeleteCard;
    }

    public void setEnableDeleteCard(String enableDeleteCard) {
        this.enableDeleteCard = enableDeleteCard;
    }

    public String getEnableUpdateSubagentPassword() {
        return enableUpdateSubagentPassword;
    }

    public void setEnableUpdateSubagentPassword(String enableUpdateSubagentPassword) {
        this.enableUpdateSubagentPassword = enableUpdateSubagentPassword;
    }

    public String getEnableUpdateSubagentStatus() {
        return enableUpdateSubagentStatus;
    }

    public void setEnableUpdateSubagentStatus(String enableUpdateSubagentStatus) {
        this.enableUpdateSubagentStatus = enableUpdateSubagentStatus;
    }

    public String getEnableUnbindAppUser() {
        return enableUnbindAppUser;
    }

    public void setEnableUnbindAppUser(String enableUnbindAppUser) {
        this.enableUnbindAppUser = enableUnbindAppUser;
    }

    public String getEnableUpdateAppUserStatus0() {
        return enableUpdateAppUserStatus0;
    }

    public void setEnableUpdateAppUserStatus0(String enableUpdateAppUserStatus0) {
        this.enableUpdateAppUserStatus0 = enableUpdateAppUserStatus0;
    }

    public String getEnableUpdateAppUserStatus1() {
        return enableUpdateAppUserStatus1;
    }

    public void setEnableUpdateAppUserStatus1(String enableUpdateAppUserStatus1) {
        this.enableUpdateAppUserStatus1 = enableUpdateAppUserStatus1;
    }

    public String getEnableUpdateAppUserTime() {
        return enableUpdateAppUserTime;
    }

    public void setEnableUpdateAppUserTime(String enableUpdateAppUserTime) {
        this.enableUpdateAppUserTime = enableUpdateAppUserTime;
    }

    public String getEnableUpdateAppUserPoint() {
        return enableUpdateAppUserPoint;
    }

    public void setEnableUpdateAppUserPoint(String enableUpdateAppUserPoint) {
        this.enableUpdateAppUserPoint = enableUpdateAppUserPoint;
    }

    public String getEnableUpdateAppUserLoginLimitU() {
        return enableUpdateAppUserLoginLimitU;
    }

    public void setEnableUpdateAppUserLoginLimitU(String enableUpdateAppUserLoginLimitU) {
        this.enableUpdateAppUserLoginLimitU = enableUpdateAppUserLoginLimitU;
    }

    public String getEnableUpdateAppUserLoginLimitM() {
        return enableUpdateAppUserLoginLimitM;
    }

    public void setEnableUpdateAppUserLoginLimitM(String enableUpdateAppUserLoginLimitM) {
        this.enableUpdateAppUserLoginLimitM = enableUpdateAppUserLoginLimitM;
    }

    public String getEnableUpdateAppUserCustomParams() {
        return enableUpdateAppUserCustomParams;
    }

    public void setEnableUpdateAppUserCustomParams(String enableUpdateAppUserCustomParams) {
        this.enableUpdateAppUserCustomParams = enableUpdateAppUserCustomParams;
    }

    public String getEnableUpdateAppUserRemark() {
        return enableUpdateAppUserRemark;
    }

    public void setEnableUpdateAppUserRemark(String enableUpdateAppUserRemark) {
        this.enableUpdateAppUserRemark = enableUpdateAppUserRemark;
    }

    public String getEnableViewUserContact() {
        return enableViewUserContact;
    }

    public void setEnableViewUserContact(String enableViewUserContact) {
        this.enableViewUserContact = enableViewUserContact;
    }

    public String getEnableUpdateUserContact() {
        return enableUpdateUserContact;
    }

    public void setEnableUpdateUserContact(String enableUpdateUserContact) {
        this.enableUpdateUserContact = enableUpdateUserContact;
    }

    public String getEnableUpdateUserPassword() {
        return enableUpdateUserPassword;
    }

    public void setEnableUpdateUserPassword(String enableUpdateUserPassword) {
        this.enableUpdateUserPassword = enableUpdateUserPassword;
    }

    public String getEnableUpdateUserRemark() {
        return enableUpdateUserRemark;
    }

    public void setEnableUpdateUserRemark(String enableUpdateUserRemark) {
        this.enableUpdateUserRemark = enableUpdateUserRemark;
    }

    public String getEnableUpdateCardTime() {
        return enableUpdateCardTime;
    }

    public void setEnableUpdateCardTime(String enableUpdateCardTime) {
        this.enableUpdateCardTime = enableUpdateCardTime;
    }

    public String getEnableUpdateCardPoint() {
        return enableUpdateCardPoint;
    }

    public void setEnableUpdateCardPoint(String enableUpdateCardPoint) {
        this.enableUpdateCardPoint = enableUpdateCardPoint;
    }

    public String getEnableUpdateCardLoginLimitU() {
        return enableUpdateCardLoginLimitU;
    }

    public void setEnableUpdateCardLoginLimitU(String enableUpdateCardLoginLimitU) {
        this.enableUpdateCardLoginLimitU = enableUpdateCardLoginLimitU;
    }

    public String getEnableUpdateCardLoginLimitM() {
        return enableUpdateCardLoginLimitM;
    }

    public void setEnableUpdateCardLoginLimitM(String enableUpdateCardLoginLimitM) {
        this.enableUpdateCardLoginLimitM = enableUpdateCardLoginLimitM;
    }

    public String getEnableUpdateCardCustomParams() {
        return enableUpdateCardCustomParams;
    }

    public void setEnableUpdateCardCustomParams(String enableUpdateCardCustomParams) {
        this.enableUpdateCardCustomParams = enableUpdateCardCustomParams;
    }

    public String getEnableUpdateCardRemark() {
        return enableUpdateCardRemark;
    }

    public void setEnableUpdateCardRemark(String enableUpdateCardRemark) {
        this.enableUpdateCardRemark = enableUpdateCardRemark;
    }

    public String getEnableUpdateCardStatus0() {
        return enableUpdateCardStatus0;
    }

    public void setEnableUpdateCardStatus0(String enableUpdateCardStatus0) {
        this.enableUpdateCardStatus0 = enableUpdateCardStatus0;
    }

    public String getEnableUpdateCardStatus1() {
        return enableUpdateCardStatus1;
    }

    public void setEnableUpdateCardStatus1(String enableUpdateCardStatus1) {
        this.enableUpdateCardStatus1 = enableUpdateCardStatus1;
    }

    public String getEnableBatchCardReplace() {
        return enableBatchCardReplace;
    }

    public void setEnableBatchCardReplace(String enableBatchCardReplace) {
        this.enableBatchCardReplace = enableBatchCardReplace;
    }

    public String getEnableAddCard() {
        return enableAddCard;
    }

    public void setEnableAddCard(String enableAddCard) {
        this.enableAddCard = enableAddCard;
    }

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
