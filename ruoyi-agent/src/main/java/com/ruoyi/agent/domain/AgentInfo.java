package com.ruoyi.agent.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 智能体信息对象 agent_info
 * 
 * @author ruoyi
 * @date 2025-06-20
 */
public class AgentInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 智能体id */
    private Long agentId;

    /** 智能体名称 */
    @Excel(name = "智能体名称")
    private String agentName;

    /** 智能体描述 */
    @Excel(name = "智能体描述")
    private String agentDesc;

    /** 模型名称 */
    @Excel(name = "模型名称")
    private String modleName;

    /** 系统提示词 */
    @Excel(name = "系统提示词")
    private String systemPrompt;

    /** 音色 */
    @Excel(name = "音色")
    private String voice;

    /** 音速 */
    @Excel(name = "音速")
    private String voiceSpeed;

    /** 记忆模型 */
    @Excel(name = "记忆模型")
    private String mem;

    /** 意图模型 */
    @Excel(name = "意图模型")
    private String intent;

    /** 交互语种编码 */
    @Excel(name = "交互语种编码")
    private String langCode;

    /** 交互语种名称 */
    @Excel(name = "交互语种名称")
    private String langName;

    /** 排序权重 */
    @Excel(name = "排序权重")
    private Integer sort;

    /** 智能体所属用户ID */
    @Excel(name = "智能体所属用户ID")
    private Long belongUserId;

    /** 智能体分享属性(0分享 1私有) */
    @Excel(name = "智能体分享属性(0分享 1私有)")
    private String shareType;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedAt;

    public void setAgentId(Long agentId) 
    {
        this.agentId = agentId;
    }

    public Long getAgentId() 
    {
        return agentId;
    }

    public void setAgentName(String agentName) 
    {
        this.agentName = agentName;
    }

    public String getAgentName() 
    {
        return agentName;
    }

    public void setAgentDesc(String agentDesc)
    {
        this.agentDesc = agentDesc;
    }

    public String getAgentDesc()
    {
        return this.agentDesc;
    }

    public void setModleName(String modleName) 
    {
        this.modleName = modleName;
    }

    public String getModleName() 
    {
        return modleName;
    }

    public void setSystemPrompt(String systemPrompt) 
    {
        this.systemPrompt = systemPrompt;
    }

    public String getSystemPrompt() 
    {
        return systemPrompt;
    }

    public void setVoice(String voice) 
    {
        this.voice = voice;
    }

    public String getVoice() 
    {
        return voice;
    }

    public void setVoiceSpeed(String voiceSpeed) 
    {
        this.voiceSpeed = voiceSpeed;
    }

    public String getVoiceSpeed() 
    {
        return voiceSpeed;
    }

    public void setMem(String mem) 
    {
        this.mem = mem;
    }

    public String getMem() 
    {
        return mem;
    }

    public void setIntent(String intent) 
    {
        this.intent = intent;
    }

    public String getIntent() 
    {
        return intent;
    }

    public void setLangCode(String langCode) 
    {
        this.langCode = langCode;
    }

    public String getLangCode() 
    {
        return langCode;
    }

    public void setLangName(String langName) 
    {
        this.langName = langName;
    }

    public String getLangName() 
    {
        return langName;
    }

    public void setSort(Integer sort) 
    {
        this.sort = sort;
    }

    public Integer getSort() 
    {
        return sort;
    }

    public void setBelongUserId(Long belongUserId) 
    {
        this.belongUserId = belongUserId;
    }

    public Long getBelongUserId() 
    {
        return belongUserId;
    }

    public void setShareType(String shareType) 
    {
        this.shareType = shareType;
    }

    public String getShareType() 
    {
        return shareType;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    public void setCreatedAt(Date createdAt) 
    {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() 
    {
        return createdAt;
    }

    public void setUpdatedAt(Date updatedAt) 
    {
        this.updatedAt = updatedAt;
    }

    public Date getUpdatedAt() 
    {
        return updatedAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("agentId", getAgentId())
            .append("agentName", getAgentName())
            .append("agentDesc", getAgentDesc())
            .append("modleName", getModleName())
            .append("systemPrompt", getSystemPrompt())
            .append("voice", getVoice())
            .append("voiceSpeed", getVoiceSpeed())
            .append("mem", getMem())
            .append("intent", getIntent())
            .append("langCode", getLangCode())
            .append("langName", getLangName())
            .append("sort", getSort())
            .append("belongUserId", getBelongUserId())
            .append("shareType", getShareType())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createdAt", getCreatedAt())
            .append("updatedAt", getUpdatedAt())
            .toString();
    }
}
